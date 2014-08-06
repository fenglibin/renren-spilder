package it.renren.spilder.main.config;

import it.renren.spilder.filter.BodyFilter;
import it.renren.spilder.filter.Filter;
import it.renren.spilder.filter.MainBodyFilter;
import it.renren.spilder.filter.TitleFilter;
import it.renren.spilder.filter.UrlListProvider;
import it.renren.spilder.filter.seperatepage.ISeparatePage;
import it.renren.spilder.filter.url.UrlSorter;
import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Environment;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.parser.MetaParser;
import it.renren.spilder.task.Task;
import it.renren.spilder.task.handler.Handler;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.util.ParserException;
import org.jdom.Document;

public class TaskExecuter extends Thread {

    private static Log4j    log4j  = new Log4j(TaskExecuter.class.getName());
    // �ļ�����Ŀ¼
    boolean                 isFile = false;
    // Ҫִ�е������ļ���Ŀ¼������
    String                  configName;
    long                    oneFileSleepTime;
    // ��Ҫִ�е������б�Ŀǰ�м������Ĳ�����������ת��Ϊ���壬ʵ������Ҫ�̳нӿ�it.renren.spilder.task.Task
    private List<Task>      taskList;
    private UrlListProvider urlListProvider;
    private UrlSorter       urlSorter;

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
                    if (!Environment.checkConfigFile) {
                        Thread.sleep(oneFileSleepTime);
                    }
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
            addUrlsFromUrlProvider(parentPageConfig);
            save(ruleXml, parentPageConfig, parentPageConfig.getUrlListPages().getListPages(), childPageConfig, configFile);
        } catch (Exception e) {
            log4j.logError("���������ļ�:" + configFile + " ���д��������쳣������", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /**
     * ��URL Provider��ȡurl
     * 
     * @param parentPageConfig
     */
    private void addUrlsFromUrlProvider(ParentPage parentPageConfig) {
        if (urlListProvider != null) {
            List<String> urlList = urlListProvider.getUrls();
            if (urlList != null && !urlList.isEmpty()) {
                parentPageConfig.getUrlListPages().getListPages().addAll(urlList);
            }
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
    private void save(Document ruleXml, ParentPage parentPageConfig, List<String> listPages, ChildPage childPageConfig, String configFile) throws ParserException, InterruptedException {
        boolean isBreak = false;

        for (String listPageUrl : listPages) {
            log4j.logDebug("��ǰ�����parent URL��" + listPageUrl);
            if (isBreak) {
                break;
            }
            String mainContent = Constants.EMPTY_STRING;
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
                mainContent = getMainContent(parentPageConfig, childPageConfig, mainContent);
            } catch (Exception e) {
                log4j.logError("�� url:" + listPageUrl + "�н�ȡ����Ҫ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            List<AHrefElement> childLinksList = AHrefParser.ahrefParser(mainContent, parentPageConfig.getUrlFilter().getMustInclude(), parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                        parentPageConfig.getCharset(), parentPageConfig.getUrlFilter().isCompByRegex());
            if (childLinksList.size() == 0) {
                log4j.logWarn("��ҳ����û�з�������Ҫ����URL������ƥ��ı��ʽ��");
            } else {
                UrlUtil.makeStadardUrl(listPageUrl, childLinksList);
            }
            if (urlSorter != null) {
                urlSorter.sortUrl(childLinksList);
            }
            int failedLinks = 0;
            List<AHrefElement> childLinksLists = new ArrayList<AHrefElement>();
            for (AHrefElement link : childLinksList) {
                // ��鵱ǰURL�Ƿ��Ѿ�������ˣ�����Ҫ������������Ƿ񶼴�����������������˾Ͳ��ý��к���Ĵ����ˣ�����Ҫ���� end
                if (isBreak) {
                    break;
                }
                if (Environment.checkConfigFile) {
                    isBreak = Boolean.TRUE;
                }
                ChildPageDetail detail = new ChildPageDetail();
                detail.setParentPageUrl(listPageUrl);
                String childUrl = link.getHref();
                log4j.logDebug("��ǰ�����child URL��" + childUrl);
                // ��鵱ǰURL�Ƿ��Ѿ�������ˣ�����Ҫ������������Ƿ񶼴�����������������˾Ͳ��ý��к���Ĵ����ˣ�����Ҫ���� begin
                if (!Environment.checkConfigFile) {
                    if (childPageConfig.isNeedToCheckUrlIsAlreadyOperate()) {
                        if (isDealed(childUrl)) {
                            continue;
                        }
                    }
                }
                detail.setUrl(childUrl);
                detail.setTitle(link.getHrefText());
                if (childPageConfig.isKeepFileName()) {
                    detail.setFileName(FileUtil.getFileName(childUrl));
                }
                List<AHrefElement> childLinks = dealPage(parentPageConfig, childPageConfig, configFile, detail);
                UrlUtil.makeStadardUrl(detail.getUrl(), childLinks);
                if (childPageConfig.isExpandUrl()) {
                    childLinksLists.addAll(childLinks);
                }
                if (!detail.isDealResult()) {
                    failedLinks++;
                    if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES && isDealOnePage(parentPageConfig)) {
                        isBreak = Boolean.TRUE;
                    }
                }
                detail = null;
                if (!Environment.checkConfigFile && parentPageConfig.getOneUrlSleepTime() >= 0) {
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* Ĭ����Ϣ10����һƪ���� */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime() + (long) (Math.random() * Constants.One_Url_Default_Sleep_Time));
                    }
                }
            }
            if (urlSorter != null) {
                urlSorter.sortUrl(childLinksLists);
            }
            // ������ҳ���������URL
            for (AHrefElement link : childLinksLists) {
                // ��鵱ǰURL�Ƿ��Ѿ�������ˣ�����Ҫ������������Ƿ񶼴�����������������˾Ͳ��ý��к���Ĵ����ˣ�����Ҫ���� end
                if (isBreak) {
                    break;
                }
                if (Environment.checkConfigFile) {
                    isBreak = Boolean.TRUE;
                }
                ChildPageDetail detail = new ChildPageDetail();
                String childUrl = link.getHref();
                log4j.logDebug("��ǰ�����URL��" + childUrl);
                // ��鵱ǰURL�Ƿ��Ѿ�������ˣ�����Ҫ������������Ƿ񶼴�����������������˾Ͳ��ý��к���Ĵ����ˣ�����Ҫ���� begin
                boolean isContinue = Boolean.FALSE;
                if (!Environment.checkConfigFile) {
                    if (!isDealed(childUrl)) {
                        isContinue = Boolean.TRUE;
                        break;
                    }
                } else {
                    isContinue = true;
                }
                if (!isContinue) {
                    continue;
                }
                detail.setUrl(childUrl);
                if (childPageConfig.isKeepFileName()) {
                    detail.setFileName(FileUtil.getFileName(childUrl));
                }
                if (!Environment.checkConfigFile && !parentPageConfig.isOnlyImage()) {
                    if (!isContinue) {
                        log4j.logDebug("��ǰURL " + childUrl + " �Ѿ��д�������Ҫ�ٴδ���");
                        continue;
                    }
                }
                dealPage(parentPageConfig, childPageConfig, configFile, detail);
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
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime() + (long) (Math.random() * Constants.One_Url_Default_Sleep_Time));
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
     * ��ҳ���ҳ�ģ�һ�������ҷ��ص�ǰҳ���з������ñ�׼��URL
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param configFile
     * @param detail
     */
    private List<AHrefElement> dealPage(ParentPage parentPageConfig, ChildPage childPageConfig, String configFile, ChildPageDetail detail) {
        boolean isPageAnalysisOk = Boolean.TRUE;
        // ���ڴ洢����ҳ���л�ȡ�ķ���Ҫ���URL
        List<AHrefElement> childLinksLists = new ArrayList<AHrefElement>();
        for (int currentSeparatePage = 1; currentSeparatePage <= childPageConfig.getContent().getSeparatePageMaxPages(); currentSeparatePage++) {
            try {
                String childUrl = "";
                childUrl = getSeparatePageUrl(detail.getUrl(), currentSeparatePage, childPageConfig);
                String htmlContent = HttpClientUtil.getGetResponseWithHttpClient(childUrl, childPageConfig.getCharset());

                List<AHrefElement> childLinksList = AHrefParser.ahrefParser(htmlContent, parentPageConfig.getUrlFilter().getMustInclude(), parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                            parentPageConfig.getCharset(), parentPageConfig.getUrlFilter().isCompByRegex());
                childLinksLists.addAll(childLinksList);

                htmlContent = UrlUtil.replaceRelativeUrl2AbsoluteUrl(childUrl, htmlContent, childPageConfig.getCharset());
                String htmlBody = getBody(parentPageConfig, childPageConfig, htmlContent);
                if (currentSeparatePage > 1) {// ֧�ַ�ҳ�ɼ�
                    detail.setContent(detail.getContent() + Constants.DEDE_SEPARATE_PAGE_STRING + htmlBody);
                } else {
                    detail.setOriginalContent(htmlContent);
                    detail.setContent(htmlBody);
                }
                if (currentSeparatePage == 1) {// ֻ�е�һҳ�Ż�ȡ���⡢�ؼ��֡�����������ķ�ҳ�Ͳ���Ҫ��ȡ�ˣ�ֱ��ʹ�õ�һҳ��ȡ�ľͿ�
                    String htmlTitle = getTitle(parentPageConfig, childPageConfig, htmlContent);
                    if (!StringUtil.isEmpty(htmlTitle)) {
                        if (htmlTitle.indexOf("404") > 0) {// ������ʱ��������404ҳ�����˳���
                            break;
                        }
                        detail.setTitle(htmlTitle);
                    }
                    String keywords = MetaParser.getMetaContent(htmlContent, childPageConfig.getCharset(), Constants.META_KEYWORDS);
                    if (Constants.EMPTY_STRING.equals(keywords)) {/* ���û�йؼ��֣���ȡ���µı���Ϊ�ؼ��� */
                        if (!StringUtil.isEmpty(htmlTitle)) {
                            keywords = htmlTitle;
                        } else {
                            keywords = detail.getTitle();
                        }
                    }
                    detail.setKeywords(keywords);

                    String childContentWithoutHtmlTagTrim = StringUtil.removeHtmlTags(htmlBody).trim();
                    if (StringUtil.isEmpty(htmlBody) || childContentWithoutHtmlTagTrim.length() < parentPageConfig.getContent().getMinLength()) {
                        throw new RuntimeException("��ǰ��ȡ�����ݳ���С�ڣ�" + parentPageConfig.getContent().getMinLength());
                    }
                    if (childContentWithoutHtmlTagTrim.length() > Constants.CONTENT_LEAST_LENGTH) {// ������߼��жϵ�ԭ������Ϊ��Щʱ��Ҫ��ȡ�����ݱ������С��100�ģ���ֻ��ȡҳ���еĵ����ʼ���ͨ��ͨ��������������ݳ��ȵļ�飬��˵����ǰ�����ǺϷ���
                        String description = childContentWithoutHtmlTagTrim.substring(0, Constants.CONTENT_LEAST_LENGTH);
                        detail.setDescription(description);
                    } else {
                        detail.setDescription(htmlTitle);
                    }
                    detail.setReplys(getReplyList(htmlContent, childPageConfig));
                    htmlContent = null;
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
                    isPageAnalysisOk = false;
                    log4j.logError("�����URLʱ�����쳣:" + detail.getUrl() + ",�����ļ�:" + configFile, e);
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
                }
                if (Environment.checkConfigFile) {
                    log4j.logError("��ǰ�����ļ���" + configFile + " ���ò��Գɹ���");
                }
                detail.setDealResult(Boolean.TRUE);
            }
        } catch (Exception e) {
            log4j.logError("�����URLʱ�����쳣:" + detail.getUrl() + ",�����ļ�:" + configFile, e);
        }
        return childLinksLists;
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

    /**
     * ��ȡ����
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param htmlContent
     * @return
     * @throws Exception
     */
    private static String getTitle(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent) throws Exception {
        Filter titleFilter = new TitleFilter();
        return titleFilter.filterContent(parentPageConfig, childPageConfig, htmlContent);
    }

    /**
     * ��ȡ��ҳ����body
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param htmlContent
     * @return
     * @throws Exception
     */
    private static String getBody(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent) throws Exception {
        Filter titleFilter = new BodyFilter();
        return titleFilter.filterContent(parentPageConfig, childPageConfig, htmlContent);
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
    private static void handleContent(ChildPage childPageConfig, ChildPageDetail detail) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        /* ��������ͨ��HANDLER���⴦�� */
        if (!StringUtil.isEmpty(childPageConfig.getContent().getHandler())) {
            Handler handler = (Handler) Class.forName(childPageConfig.getContent().getHandler()).newInstance();
            handler.execute(detail);
        }
    }

    /**
     * ��ȡ�����б������
     * 
     * @param mainContent
     * @param parentPageConfig
     * @return
     * @throws RuntimeException
     */
    private static String getMainContent(ParentPage parentPageConfig, ChildPage childPageConfig, String mainContent) throws Exception {
        Filter filter = new MainBodyFilter();
        return filter.filterContent(parentPageConfig, childPageConfig, mainContent);
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

        if (!StringUtil.isEmpty(childPageConfig.getReplys().getStart()) && !StringUtil.isEmpty(childPageConfig.getReplys().getEnd())) {// �ж��Ƿ��л�ȡ�ظ�������
            if (!StringUtil.isEmpty(childPageConfig.getReplys().getReply().getStart()) && !StringUtil.isEmpty(childPageConfig.getReplys().getReply().getEnd())) {/*
                                                                                                                                                                  * �������ӻظ��ڵ�
                                                                                                                                                                  * ��
                                                                                                                                                                  * �͸����ӻظ��ڵ������������
                                                                                                                                                                  * ��
                                                                                                                                                                  * ���������ڽ�ȡ�������л؏̓��ݵĲ���
                                                                                                                                                                  * ��
                                                                                                                                                                  * ��ȡ��ÿһ���؏̈́t����������
                                                                                                                                                                  */
                childBody = StringUtil.subString(childBody, childPageConfig.getReplys().getStart(), childPageConfig.getReplys().getEnd());
                childBody = StringUtil.replaceContent(childBody, childPageConfig.getReplys().getFrom(), childPageConfig.getReplys().getTo(), childPageConfig.getReplys().isIssRegularExpression());
                if (!StringUtil.isEmpty(childBody)) {
                    replysList = StringUtil.getListFromStart2End(childBody, childPageConfig.getReplys().getReply().getStart(), childPageConfig.getReplys().getReply().getEnd(),
                                                                 childPageConfig.getReplys().isFirstMainContent());
                }
            } else {/* ֻ�����������ã��Ǿ͸��������û�ȡ�ظ����� */
                if (!StringUtil.isEmpty(childBody)) {
                    childBody = StringUtil.replaceContent(childBody, childPageConfig.getReplys().getFrom(), childPageConfig.getReplys().getTo(), childPageConfig.getReplys().isIssRegularExpression());
                    replysList = StringUtil.getListFromStart2End(childBody, childPageConfig.getReplys().getStart(), childPageConfig.getReplys().getEnd(),
                                                                 childPageConfig.getReplys().isFirstMainContent());
                }
            }

        }

        return replysList;
    }

    /**
     * ��ȡ��ҳ��URL
     * 
     * @param mainUrl
     * @param currentSeparatePage
     * @param childPageConfig
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static String getSeparatePageUrl(String mainUrl, int currentSeparatePage, ChildPage childPageConfig) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ISeparatePage page = (ISeparatePage) Class.forName(childPageConfig.getContent().getSeparatePageClass()).newInstance();
        return page.getSeparatePageUrl(mainUrl, currentSeparatePage);
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

    public void setUrlListProvider(UrlListProvider urlListProvider) {
        this.urlListProvider = urlListProvider;
    }

    public void setUrlSorter(UrlSorter urlSorter) {
        this.urlSorter = urlSorter;
    }

}
