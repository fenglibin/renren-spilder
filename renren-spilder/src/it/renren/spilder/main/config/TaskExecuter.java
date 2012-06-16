package it.renren.spilder.main.config;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Environment;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.parser.MetaParser;
import it.renren.spilder.task.Task;
import it.renren.spilder.task.handler.Handler;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.htmlparser.util.ParserException;
import org.jdom.Document;
import org.jdom.Element;

public class TaskExecuter extends Thread {

    private static Log4j log4j  = new Log4j(TaskExecuter.class.getName());
    // �ļ�����Ŀ¼
    boolean              isFile = false;
    // Ҫִ�е������ļ���Ŀ¼������
    String               configName;
    long                 oneFileSleepTime;
    // ��Ҫִ�е������б�Ŀǰ�м������Ĳ�����������ת��Ϊ���壬ʵ������Ҫ�̳нӿ�it.renren.spilder.task.Task
    private List<Task>   taskList;

    public TaskExecuter(){
        this.oneFileSleepTime = Constants.One_File_Default_Sleep_Time;
    }

    public void run() {
        try {
            if (isFile) {
                saveFromConfigFile(configName);
            } else {
                doSaveFromConfigDir(configName, oneFileSleepTime);
            }
        } catch (Exception e) {
            log4j.logError("��ǰִ���쳣:" + configName, e);
        }
    }

    /**
     * ִ��ĳ��Ŀ¼�����е������ļ�����ָ����ÿ�������ļ�ִ�������ͣ��ʱ��
     * 
     * @param configDirectory ��ǰ�����ļ�����Ŀ¼
     * @param oneFileSleepTime ÿ�������ļ�ִ�������ͣ��ʱ��
     * @throws Exception
     */
    private void doSaveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        File configFilePath = new File(configDirectory);
        File[] files = configFilePath.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".xml")) {/* ֻ��ȡXML�����ļ� */
                try {
                    log4j.logDebug("��ǰ������ļ�:" + file.getAbsolutePath());
                    saveFromConfigFile(file.getAbsolutePath());
                    Thread.sleep(oneFileSleepTime);
                } catch (Exception e) {
                    log4j.logError("Current File :" + file.getAbsolutePath() + " Deal Error!!!", e);
                }
            }
        }
    }

    /**
     * ���������ļ���ȡ����
     * 
     * @param configFile
     * @throws Exception
     */
    private void saveFromConfigFile(String configFile) throws Exception {
        Document ruleXml = JDomUtil.getDocument(new File(configFile));
        ParentPage parentPageConfig = Config.initParentPage(ruleXml);
        ChildPage childPageConfig = Config.initChildPage(ruleXml);
        try {
            // ���ڱ����Ѿ������˵�BLOG��ҳURL��MAP
            ConcurrentMap<String, String> blogHomeUrlMap = new ConcurrentHashMap<String, String>();
            save(ruleXml, parentPageConfig, parentPageConfig.getUrlListPages().getListPages(), childPageConfig,
                 configFile, blogHomeUrlMap);
            if (blogHomeUrlMap.size() == 0) {
                log4j.logWarn("Can not get any child url,please check the rules.");
            }
            // �����ǻ�ȡ��ǰ���͵Ĳ�����ҳ���������
            // ��MAPת��ΪList
            List<String> blogHomeUrlList = new ArrayList<String>();
            if (blogHomeUrlMap.size() > 0) {
                for (String url : blogHomeUrlMap.keySet()) {
                    blogHomeUrlList.add(url);
                }
            }
            blogHomeUrlMap = null;
            save(ruleXml, parentPageConfig, blogHomeUrlList, childPageConfig, configFile, blogHomeUrlMap);
        } catch (Exception e) {
            log4j.logError("���������ļ�:" + configFile + " ���д��������쳣������", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /**
     * ����
     * 
     * @param ruleXml
     * @param parentPageConfig
     * @param listPages
     * @param childPageConfig
     * @param configFile
     * @param blogHomeUrlMap
     * @throws ParserException
     * @throws InterruptedException
     */
    private void save(Document ruleXml, ParentPage parentPageConfig, List<String> listPages, ChildPage childPageConfig,
                      String configFile, ConcurrentMap<String, String> blogHomeUrlMap) throws ParserException,
                                                                                      InterruptedException {
        boolean isBreak = false;

        for (String listPageUrl : listPages) {
            if (isBreak) {
                break;
            }
            String mainContent = "";
            try {
                mainContent = HttpClientUtil.getGetResponseWithHttpClient(listPageUrl, parentPageConfig.getCharset());
            } catch (Exception e) {
                log4j.logError("�� url:" + listPageUrl + "��ȡ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            try {
                mainContent = getMainContent(mainContent, parentPageConfig);
            } catch (Exception e) {
                log4j.logError("�� url:" + listPageUrl + "�н�ȡ����Ҫ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            List<AHrefElement> childLinksList = AHrefParser.ahrefParser(mainContent,
                                                                        parentPageConfig.getUrlFilter().getMustInclude(),
                                                                        parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                        parentPageConfig.getCharset(),
                                                                        parentPageConfig.getUrlFilter().isCompByRegex());
            if (childLinksList.size() == 0) {
                log4j.logWarn("��ҳ����û�з�������Ҫ����URL������ƥ��ı��ʽ��");
            }
            int failedLinks = 0;
            for (AHrefElement link : childLinksList) {
                ChildPageDetail detail = new ChildPageDetail();
                String childUrl = link.getHref();
                childUrl = UrlUtil.makeUrl(listPageUrl, childUrl);
                log4j.logDebug("��ǰ�����URL��" + childUrl);
                if (isBreak) {
                    break;
                }
                if (Environment.checkConfigFile) {
                    isBreak = Boolean.TRUE;
                }
                detail.setUrl(childUrl);
                String blogHomeUrl = analysisBlogHomeUrl(parentPageConfig.getBlogType(), childUrl);
                if (blogHomeUrlMap != null && !StringUtil.isEmpty(blogHomeUrl)) {
                    if (!StringUtil.isEmpty(parentPageConfig.getHomeUrlAddStr())) {
                        blogHomeUrl += parentPageConfig.getHomeUrlAddStr();
                    }
                    blogHomeUrlMap.put(blogHomeUrl, blogHomeUrl);
                }
                if (childPageConfig.isKeepFileName()) {
                    detail.setFileName(getUrlName(childUrl));
                }
                if (!Environment.checkConfigFile && !parentPageConfig.isOnlyImage()) {
                    if (isDealed(childUrl)) {
                        log4j.logDebug("��ǰURL " + childUrl + " �Ѿ��д�������Ҫ�ٴδ���");
                        continue;
                    }
                }
                if (childPageConfig.getContent().getSeparatePageMaxPages() > 1) {// ���з�ҳ��ҳ�洦��
                    dealSeparatePage(parentPageConfig, childPageConfig, configFile, detail);
                } else {// ��ҳ����
                    dealSinglePage(parentPageConfig, childPageConfig, configFile, detail);
                }
                if (!detail.isDealResult()) {
                    failedLinks++;
                    if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES && isDealOnePage(parentPageConfig)) {
                        isBreak = Boolean.TRUE;
                    }
                }
                detail = null;
                if (!Environment.checkConfigFile) {
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* Ĭ����Ϣ10����һƪ���� */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime()
                                     + (long) (Math.random() * Constants.One_Url_Default_Sleep_Time));
                    }
                }
            }
        }
    }

    /**
     * �жϵ�ǰ�����Ƿ�ֻ����һҳ��<br>
     * 1������û��������ļ��������˽ڵ�/Rules/MainUrl/DealOnePage�������û����õ�ֵΪ׼����ֵֻҪ��Ϊtrue���򷵻�false;<br>
     * 2������û�û�����ýڵ�/Rules/MainUrl/DealOnePage�������������Ƿ���"-p"����ȷ��
     * 
     * @param parentPageConfig
     * @return
     */
    private boolean isDealOnePage(ParentPage parentPageConfig) {
        boolean result = false;
        String dealOnePage = parentPageConfig.getDealOnePage();
        if (StringUtil.isEmpty(dealOnePage)) {// û������
            result = Environment.dealOnePage;
        } else {
            if (dealOnePage.equalsIgnoreCase("true")) {
                result = true;
            }
        }
        return result;
    }

    /**
     * ����û�з�ҳ������
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param configFile
     * @param blogHomeUrlMap
     * @param detail
     */
    private void dealSinglePage(ParentPage parentPageConfig, ChildPage childPageConfig, String configFile,
                                ChildPageDetail detail) {

        String childUrl = detail.getUrl();
        try {
            String childBody = HttpClientUtil.getGetResponseWithHttpClient(childUrl, childPageConfig.getCharset());
            String childContent = getChildContent(childBody, childPageConfig);
            detail.setContent(childContent);
            handleContent(childPageConfig, detail);

            String childTitle = StringUtil.subString(childBody, childPageConfig.getTitle().getStart(),
                                                     childPageConfig.getTitle().getEnd());
            childTitle = StringUtil.removeHtmlTags(childTitle);
            childTitle = replaceTitle(childPageConfig, childTitle);
            detail.setTitle(childTitle);
            String keywords = MetaParser.getMetaContent(childBody, childPageConfig.getCharset(),
                                                        Constants.META_KEYWORDS);
            if (keywords.equals("")) {/* ���û�йؼ��֣���ȡ���µı���Ϊ�ؼ��� */
                keywords = childTitle;
            }
            detail.setKeywords(keywords);

            String childContentWithoutHtmlTagTrim = StringUtil.removeHtmlTags(childContent).trim();
            if (StringUtil.isEmpty(childContent)
                || childContentWithoutHtmlTagTrim.length() < parentPageConfig.getContent().getMinLength()) {
                throw new RuntimeException("��ǰ��ȡ�����ݳ���С�ڣ�" + parentPageConfig.getContent().getMinLength());
            }
            if (childContentWithoutHtmlTagTrim.length() > Constants.CONTENT_LEAST_LENGTH) {// ������߼��жϵ�ԭ������Ϊ��Щʱ��Ҫ��ȡ�����ݱ������С��100�ģ���ֻ��ȡҳ���еĵ����ʼ���ͨ��ͨ��������������ݳ��ȵļ�飬��˵����ǰ�����ǺϷ���
                String description = childContentWithoutHtmlTagTrim.substring(0, Constants.CONTENT_LEAST_LENGTH);
                detail.setDescription(description);
            } else {
                detail.setDescription(childTitle);
            }
            detail.setReplys(getReplyList(childBody, childPageConfig));
            childBody = null;
            if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                throw new RuntimeException("�����URL:" + childUrl + " ʱ����ȡ���������Ϊ��!");
            }

            if (parentPageConfig.isSaveImage() && !Environment.checkConfigFile) {
                // ����ͼƬ
                UrlUtil.saveImages(parentPageConfig, childPageConfig, detail);
            }
            if (!Environment.checkConfigFile && !parentPageConfig.isOnlyImage()) {
                for (Task task : taskList) {
                    task.doTask(parentPageConfig, childPageConfig, detail);
                }
            } else {
                log4j.logError("��ǰ�����ļ���" + configFile + " ���ò��Գɹ���");
            }
            detail.setDealResult(Boolean.TRUE);
        } catch (Exception e) {
            log4j.logError("�����URLʱ�����쳣:" + childUrl, e);
        }
    }

    /**
     * �Է�ҳҳ��Ĵ���
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param configFile
     * @param detail
     */
    private void dealSeparatePage(ParentPage parentPageConfig, ChildPage childPageConfig, String configFile,
                                  ChildPageDetail detail) {
        boolean isPageAnalysisOk = Boolean.TRUE;
        for (int currentSeparatePage = 1; currentSeparatePage <= childPageConfig.getContent().getSeparatePageMaxPages(); currentSeparatePage++) {
            try {
                String childUrl = "";
                if (currentSeparatePage > 1) {
                    childUrl = getSeparatePageUrl(detail.getUrl(), currentSeparatePage,
                                                  childPageConfig.getContent().getSeparatePageUrlSuffix());
                } else {
                    childUrl = detail.getUrl();
                }
                String childBody = HttpClientUtil.getGetResponseWithHttpClient(childUrl, childPageConfig.getCharset());
                String childContent = getChildContent(childBody, childPageConfig);
                if (currentSeparatePage > 1) {// ֧�ַ�ҳ�ɼ�
                    detail.setContent(detail.getContent() + Constants.DEDE_SEPARATE_PAGE_STRING + childContent);
                } else {
                    detail.setContent(childContent);
                }
                if (currentSeparatePage == 1) {// ֻ�е�һҳ�Ż�ȡ���⡢�ؼ��֡�����������ķ�ҳ�Ͳ���Ҫ��ȡ�ˣ�ֱ��ʹ�õ�һҳ��ȡ�ľͿ�
                    String childTitle = StringUtil.subString(childBody, childPageConfig.getTitle().getStart(),
                                                             childPageConfig.getTitle().getEnd());
                    childTitle = StringUtil.removeHtmlTags(childTitle);
                    childTitle = replaceTitle(childPageConfig, childTitle);
                    if (childTitle.indexOf("404") > 0) {// ������ʱ��������404ҳ�����˳���
                        break;
                    }
                    detail.setTitle(childTitle);
                    String keywords = MetaParser.getMetaContent(childBody, childPageConfig.getCharset(),
                                                                Constants.META_KEYWORDS);
                    if (keywords.equals("")) {/* ���û�йؼ��֣���ȡ���µı���Ϊ�ؼ��� */
                        keywords = childTitle;
                    }
                    detail.setKeywords(keywords);

                    String childContentWithoutHtmlTagTrim = StringUtil.removeHtmlTags(childContent).trim();
                    if (StringUtil.isEmpty(childContent)
                        || childContentWithoutHtmlTagTrim.length() < parentPageConfig.getContent().getMinLength()) {
                        throw new RuntimeException("��ǰ��ȡ�����ݳ���С�ڣ�" + parentPageConfig.getContent().getMinLength());
                    }
                    if (childContentWithoutHtmlTagTrim.length() > Constants.CONTENT_LEAST_LENGTH) {// ������߼��жϵ�ԭ������Ϊ��Щʱ��Ҫ��ȡ�����ݱ������С��100�ģ���ֻ��ȡҳ���еĵ����ʼ���ͨ��ͨ��������������ݳ��ȵļ�飬��˵����ǰ�����ǺϷ���
                        String description = childContentWithoutHtmlTagTrim.substring(0, Constants.CONTENT_LEAST_LENGTH);
                        detail.setDescription(description);
                    } else {
                        detail.setDescription(childTitle);
                    }
                    detail.setReplys(getReplyList(childBody, childPageConfig));
                    childBody = null;
                    if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                        throw new RuntimeException("�����URL:" + childUrl + " ʱ����ȡ���������Ϊ��!");
                    }
                }
                if (parentPageConfig.isSaveImage() && !Environment.checkConfigFile) {
                    // ����ͼƬ
                    UrlUtil.saveImages(parentPageConfig, childPageConfig, detail);
                }
            } catch (Exception e) {
                if (currentSeparatePage > 1) {
                    break;
                } else {
                    log4j.logError("�����URLʱ�����쳣:" + detail.getUrl(), e);
                }
            }
        }
        try {
            if (isPageAnalysisOk) {
                handleContent(childPageConfig, detail);
                if (!Environment.checkConfigFile && !parentPageConfig.isOnlyImage()) {
                    for (Task task : taskList) {
                        task.doTask(parentPageConfig, childPageConfig, detail);
                    }
                } else {
                    log4j.logError("��ǰ�����ļ���" + configFile + " ���ò��Գɹ���");
                }
                detail.setDealResult(Boolean.TRUE);
            }
        } catch (Exception e) {
            log4j.logError("�����URLʱ�����쳣:" + detail.getUrl(), e);
        }
    }

    /**
     * ��ǰUrl�Ƿ��Ѿ��������
     * 
     * @param url
     * @return
     */
    private boolean isDealed(String url) {
        boolean result = Boolean.FALSE;
        for (Task task : taskList) {
            result = task.isDealed(url);
            if (result == Boolean.FALSE) {// ֻҪ����һ��û�д������Ϊ��û�д���
                return result;
            }
        }
        return result;
    }

    /* �����滻 */
    private static String replaceTitle(ChildPage childPageConfig, String childTitle) {
        if (!StringUtil.isEmpty(childPageConfig.getTitle().getFrom())
            && !StringUtil.isEmpty(childPageConfig.getTitle().getTo())) {
            if (childPageConfig.getTitle().isIssRegularExpression()) {
                childTitle = childTitle.replaceAll(childPageConfig.getTitle().getFrom(),
                                                   childPageConfig.getTitle().getTo());
            } else {
                childTitle = childTitle.replace(childPageConfig.getTitle().getFrom(),
                                                childPageConfig.getTitle().getTo());
            }
        }
        return childTitle;
    }

    /**
     * ����������ͨ��ʵ���࣬��������Ĵ���
     * 
     * @param childPageConfig
     * @param detail
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static void handleContent(ChildPage childPageConfig, ChildPageDetail detail) throws InstantiationException,
                                                                                        IllegalAccessException,
                                                                                        ClassNotFoundException {
        /* ��������ͨ��HANDLER���⴦�� */
        if (!StringUtil.isEmpty(childPageConfig.getContent().getHandler())) {
            Handler handler = (Handler) Class.forName(childPageConfig.getContent().getHandler()).newInstance();
            handler.execute(detail);
        }
    }

    private static String getUrlName(String url) {
        try {
            String[] str = url.split("/");
            url = str[str.length - 1];
            str = url.split("\\.");
            url = str[0];
        } catch (Exception e) {
            log4j.logError("����URL��ȡ���Ʒ����쳣��", e);
            url = "";
        }
        return url;
    }

    /**
     * ��������б��еľ����������ݵ���ʽ���в�ͬ�Ĵ�����ȷ���ܹ����ջ�ȡ�����¡�
     * 
     * @param childBody
     * @param childPageConfig
     * @return
     * @throws RuntimeException
     */
    private static String getChildContent(String childBody, ChildPage childPageConfig) throws RuntimeException {
        String childContent = "";
        int startSize = childPageConfig.getContent().getStartList().size();
        for (int i = 0; i < startSize; i++) {
            try {
                childContent = StringUtil.subString(childBody,
                                                    ((Element) childPageConfig.getContent().getStartList().get(i)).getText(),
                                                    ((Element) childPageConfig.getContent().getEndList().get(i)).getText());
                break;
            } catch (Exception e) {
                if (i + 1 == startSize) {
                    throw new RuntimeException(e);
                } else {
                    log4j.logDebug("�� " + (i + 1) + " �λ�ȡ�������ݳ���");
                }
            }
        }
        /** ȥ��script��ǩ */
        childContent = StringUtil.removeScriptAndHrefTags(childContent);
        childContent = StringUtil.replaceContent(childContent, childPageConfig.getContent().getFrom(),
                                                 childPageConfig.getContent().getTo(),
                                                 childPageConfig.getContent().isIssRegularExpression());

        // �������е����URL��ַ���滻Ϊ���Ե�URL��ַ����ʼ��
        // childContent = replaceRelativePath2AbsolutePate(childUrl,
        // childContent,childPageConfig.getCharset());
        // �������е����URL��ַ���滻Ϊ���Ե�URL��ַ��������
        // �滻Ŀǰ���ֵ�һЩ���⣬���ȡ���������а˸��ʺŵ�
        childContent = childContent.replace("???", "");
        childContent = childContent.replace("??", "");
        return childContent;
    }

    /**
     * ��ȡ�����б������
     * 
     * @param mainContent
     * @param parentPageConfig
     * @return
     * @throws RuntimeException
     */
    private static String getMainContent(String mainContent, ParentPage parentPageConfig) throws RuntimeException {
        String content = "";
        int startSize = parentPageConfig.getContent().getStartList().size();
        for (int i = 0; i < startSize; i++) {
            try {
                content = StringUtil.subString(mainContent,
                                               ((Element) parentPageConfig.getContent().getStartList().get(i)).getText(),
                                               ((Element) parentPageConfig.getContent().getEndList().get(i)).getText());
                break;
            } catch (Exception e) {
                if (i + 1 == startSize) {
                    throw new RuntimeException(e);
                } else {
                    log4j.logDebug("�� " + (i + 1) + " �λ�ȡ�������ݳ���");
                }
            }
        }

        return content;
    }

    /**
     * ��ȡ�ظ�
     * 
     * @param childBody
     * @param childPageConfig
     * @return
     * @throws RuntimeException
     */
    private static List<String> getReplyList(String childBody, ChildPage childPageConfig) throws RuntimeException {
        List<String> replysList = new ArrayList<String>();

        if (!StringUtil.isEmpty(childPageConfig.getReplys().getStart())
            && !StringUtil.isEmpty(childPageConfig.getReplys().getEnd())) {// �ж��Ƿ��л�ȡ�ظ�������
            if (!StringUtil.isEmpty(childPageConfig.getReplys().getReply().getStart())
                && !StringUtil.isEmpty(childPageConfig.getReplys().getReply().getEnd())) {/*
                                                                                          * �������ӻظ��ڵ㣬�͸����ӻظ��ڵ������������
                                                                                          * ���������ڽ�ȡ�������л؏̓��ݵĲ���
                                                                                          * ����ȡ��ÿһ���؏̈́t����������
                                                                                          */
                childBody = StringUtil.subString(childBody, childPageConfig.getReplys().getStart(),
                                                 childPageConfig.getReplys().getEnd());
                childBody = StringUtil.replaceContent(childBody, childPageConfig.getReplys().getFrom(),
                                                      childPageConfig.getReplys().getTo(),
                                                      childPageConfig.getReplys().isIssRegularExpression());
                if (!StringUtil.isEmpty(childBody)) {
                    replysList = StringUtil.getListFromStart2End(childBody,
                                                                 childPageConfig.getReplys().getReply().getStart(),
                                                                 childPageConfig.getReplys().getReply().getEnd(),
                                                                 childPageConfig.getReplys().isFirstMainContent());
                }
            } else {/* ֻ�����������ã��Ǿ͸��������û�ȡ�ظ����� */
                if (!StringUtil.isEmpty(childBody)) {
                    childBody = StringUtil.replaceContent(childBody, childPageConfig.getReplys().getFrom(),
                                                          childPageConfig.getReplys().getTo(),
                                                          childPageConfig.getReplys().isIssRegularExpression());
                    replysList = StringUtil.getListFromStart2End(childBody, childPageConfig.getReplys().getStart(),
                                                                 childPageConfig.getReplys().getEnd(),
                                                                 childPageConfig.getReplys().isFirstMainContent());
                }
            }

        }

        return replysList;
    }

    /**
     * ���ݵ�ǰ�Ĳ������ͣ��Լ���ǰ���͵�һ��URL��ַ����ȡ��ǰ���͵���ҳ��ַ
     * 
     * @param blogType �������ͣ�Ŀǰ1��ʾ��CSDN�Ĳ��ͣ����Ͳ��Ƕ�����������2��ʾ��ITEYE�Ĳ��ͣ������ж���������
     * @param childUrl ��ǰ���͵�һ��URL
     * @return ��ǰ���͵���ҳ��ַ
     */
    private static String analysisBlogHomeUrl(int blogType, String childUrl) {
        String blogHomeUrl = "";
        if (blogType > 0) {
            if (blogType == 1) {
                String host = UrlUtil.getHost(childUrl);
                childUrl = childUrl.replace(host, "");
                String user = StringUtil.subStringSmart(childUrl, Constants.URL_SEPARATOR, Constants.URL_SEPARATOR);
                blogHomeUrl = host + Constants.URL_SEPARATOR + user;
            } else if (blogType == 2) {
                blogHomeUrl = UrlUtil.getHost(childUrl);
            }
        }
        return blogHomeUrl;
    }

    private static String getSeparatePageUrl(String mainUrl, int currentSeparatePage, String suffix) {
        String childUrl = "";
        String urlHead = StringUtil.substringBeforeLastWithSeparator(mainUrl, Constants.URL_SEPARATOR);
        String pageUrlName = StringUtil.subStringFromLastStart(mainUrl, Constants.URL_SEPARATOR, suffix);
        String param = StringUtil.subStringFromLastStart(mainUrl, suffix, null);
        childUrl = urlHead + pageUrlName + Constants.SEPARATE_PAGE_SEPARATOR + currentSeparatePage + suffix + param;
        return childUrl;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setOneFileSleepTime(long oneFileSleepTime) {
        this.oneFileSleepTime = oneFileSleepTime;
    }

    public static void main(String[] args) {
        String url = "http://java.dzone.com/articles/cdi-aop.htm";
        url = getUrlName(url);
        System.out.println(url);
        url = "http://java.dzone.com/articles/cdi-aop.htm";
        System.out.println(analysisBlogHomeUrl(1, url));
    }

}
