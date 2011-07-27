package it.renren.spilder.main.config;

import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dataobject.DownurlDO;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    DownurlDAO           downurlDAO;

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
            boolean isBreak = false;
            for (String listPageUrl : parentPageConfig.getUrlListPages().getListPages()) {
                if (isBreak) {
                    break;
                }
                String mainContent = "";
                try {
                    mainContent = HttpClientUtil.getGetResponseWithHttpClient(listPageUrl,
                                                                              parentPageConfig.getCharset());
                } catch (Exception e) {
                    log4j.logError("�� url:" + listPageUrl + "��ȡ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                    throw new RuntimeException(e);
                }
                try {
                    mainContent = getMainContent(mainContent, parentPageConfig);
                } catch (Exception e) {
                    log4j.logError("�� url:" + listPageUrl + "�н�ȡ����Ҫ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                    throw new RuntimeException(e);
                }
                List<AHrefElement> childLinks = AHrefParser.ahrefParser(
                                                                        mainContent,
                                                                        parentPageConfig.getUrlFilter().getMustInclude(),
                                                                        parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                        parentPageConfig.getCharset(),
                                                                        parentPageConfig.getUrlFilter().isCompByRegex());
                int failedLinks = 0;
                for (AHrefElement link : childLinks) {
                    if (isBreak) {
                        break;
                    }
                    if (Environment.checkConfigFile) {
                        isBreak = Boolean.TRUE;
                    }
                    ChildPageDetail detail = new ChildPageDetail();
                    String childUrl = link.getHref();
                    try {
                        childUrl = UrlUtil.makeUrl(listPageUrl, childUrl);
                        log4j.logDebug("��ǰ�����URL��" + childUrl);
                        detail.setUrl(childUrl);
                        if (!Environment.checkConfigFile) {
                            if (isDealed(childUrl)) {
                                log4j.logDebug("��ǰURL " + childUrl + " �Ѿ��д�������Ҫ�ٴδ���");
                                continue;
                            }
                            saveDownUrl(parentPageConfig, detail);
                        }
                        if (childPageConfig.isKeepFileName()) {
                            detail.setFileName(getUrlName(childUrl));
                        }
                        String childBody = HttpClientUtil.getGetResponseWithHttpClient(childUrl,
                                                                                       childPageConfig.getCharset());
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

                        String childContent = getChildContent(childBody, childPageConfig);
                        if (StringUtil.isNull(childContent) || childContent.length() <= Constants.CONTENT_LEAST_LENGTH) {
                            throw new RuntimeException("��ǰ��ȡ�����ݳ���С�ڣ�" + Constants.CONTENT_LEAST_LENGTH);
                        }
                        detail.setContent(childContent);
                        detail.setReplys(getReplyList(childBody, childPageConfig));
                        childBody = null;
                        String description = StringUtil.removeHtmlTags(childContent).trim().substring(
                                                                                                      0,
                                                                                                      Constants.CONTENT_LEAST_LENGTH);
                        detail.setDescription(description);
                        if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                            throw new RuntimeException("�����URL:" + childUrl + " ʱ����ȡ���������Ϊ��!");
                        }
                        childContent = StringUtil.replaceContent(childContent, childPageConfig.getContent().getFrom(),
                                                                 childPageConfig.getContent().getTo(),
                                                                 childPageConfig.getContent().isIssRegularExpression());
                        if (childPageConfig.isAddUrl()) {
                            childContent = childContent + "<br>From��<a href=\"" + detail.getUrl()
                                           + "\" target=\"_blank\">" + detail.getUrl() + "</a>";
                        }
                        // �������е����URL��ַ���滻Ϊ���Ե�URL��ַ����ʼ��
                        childContent = replaceRelativePath2AbsolutePate(childUrl, childContent,
                                                                        childPageConfig.getCharset());
                        // �滻Ŀǰ���ֵ�һЩ���⣬���ȡ���������а˸��ʺŵ�
                        childContent = childContent.replace("????????", "");
                        // �������е����URL��ַ���滻Ϊ���Ե�URL��ַ��������
                        detail.setContent(childContent);
                        handleContent(childPageConfig, detail);
                        // ����ͼƬ
                        String content = UrlUtil.saveImages(parentPageConfig, childPageConfig, detail);
                        detail.setContent(content);
                        if (!Environment.checkConfigFile) {
                            for (Task task : taskList) {
                                task.doTask(parentPageConfig, childPageConfig, detail);
                            }
                        } else {
                            log4j.logError("��ǰ�����ļ���" + configFile + " ���ò��Գɹ���");
                        }
                    } catch (Exception e) {
                        failedLinks++;
                        if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES && Environment.dealOnePage) {
                            isBreak = Boolean.TRUE;
                        }
                        log4j.logError("�����URLʱ�����쳣:" + childUrl, e);
                    }
                    detail.setContent(null);
                    detail.setDescription(null);
                    detail = null;
                    if (!Environment.checkConfigFile) {
                        if (parentPageConfig.getOneUrlSleepTime() == 0) {
                            Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* Ĭ����Ϣ10����һƪ���� */
                        } else {
                            Thread.sleep(parentPageConfig.getOneUrlSleepTime());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log4j.logError("���������ļ�:" + configFile + " ���д��������쳣������", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /* �����滻 */
    private static String replaceTitle(ChildPage childPageConfig, String childTitle) {
        if (!childPageConfig.getTitle().getFrom().equals("") && !childPageConfig.getTitle().getTo().equals("")) {
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
        if (!StringUtil.isNull(childPageConfig.getContent().getHandler())) {
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
                childContent = StringUtil.subString(
                                                    childBody,
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
                content = StringUtil.subString(
                                               mainContent,
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
     * �����������ӵ����·���滻Ϊ����·��
     * 
     * @param childUrl ��ǰҳ���URL
     * @param childContent ��ǰҳ�������
     * @param charset ��ǰҳ��ı���
     * @return
     * @throws ParserException
     */
    private String replaceRelativePath2AbsolutePate(String childUrl, String childContent, String charset)
                                                                                                         throws ParserException {
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(childContent, null, null, charset, Boolean.FALSE);
        for (AHrefElement href : childLinks) {
            String url = href.getHref();
            if (!url.startsWith("http")) {
                String urlAbsolute = UrlUtil.makeUrl(childUrl, url);
                childContent = childContent.replace(url, urlAbsolute);
            }
        }
        return childContent;
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

        if (!StringUtil.isNull(childPageConfig.getReplys().getStart())
            && !StringUtil.isNull(childPageConfig.getReplys().getEnd())) {// �ж��Ƿ��л�ȡ�ظ�������
            if (!StringUtil.isNull(childPageConfig.getReplys().getReply().getStart())
                && !StringUtil.isNull(childPageConfig.getReplys().getReply().getEnd())) {/*
                                                                                          * �������ӻظ��ڵ㣬�͸����ӻظ��ڵ������������
                                                                                          * ���������ڽ�ȡ�������л؏̓��ݵĲ���
                                                                                          * ����ȡ��ÿһ���؏̈́t����������
                                                                                          */
                childBody = StringUtil.subString(childBody, childPageConfig.getReplys().getStart(),
                                                 childPageConfig.getReplys().getEnd());
                childBody = StringUtil.replaceContent(childBody, childPageConfig.getReplys().getFrom(),
                                                      childPageConfig.getReplys().getTo(),
                                                      childPageConfig.getReplys().isIssRegularExpression());
                if (!StringUtil.isNull(childBody)) {
                    replysList = StringUtil.getListFromStart2End(childBody,
                                                                 childPageConfig.getReplys().getReply().getStart(),
                                                                 childPageConfig.getReplys().getReply().getEnd(),
                                                                 childPageConfig.getReplys().isFirstMainContent());
                }
            } else {/* ֻ�����������ã��Ǿ͸��������û�ȡ�ظ����� */
                if (!StringUtil.isNull(childBody)) {
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
     * ��ѯ��ǰURL�Ƿ��Ѿ������
     * 
     * @param url
     * @return
     */
    private boolean isDealed(String url) {
        boolean is = Boolean.FALSE;
        DownurlDO downurlDO = downurlDAO.selectDownurl(url);
        if (downurlDO != null) {
            is = Boolean.TRUE;
        }
        return is;
    }

    /* �����Ѿ���ȡ���ݵ�URL�����������������ظ����쳣��˵����URL�Ѿ���ȡ�����ݣ�Ϊ�������� */
    protected void saveDownUrl(ParentPage parentPageConfig, ChildPageDetail detail) throws SQLException {
        if (parentPageConfig.isFilterDownloadUrl()) {
            DownurlDO downurlDO = new DownurlDO();
            downurlDO.setUrl(detail.getUrl());
            downurlDAO.insertDownurl(downurlDO);
        }
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
    }

    public void setDownurlDAO(DownurlDAO downurlDAO) {
        this.downurlDAO = downurlDAO;
    }
}
