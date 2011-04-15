package it.renren.spilder.main;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.parser.ImageElement;
import it.renren.spilder.parser.ImageParser;
import it.renren.spilder.parser.MetaParser;
import it.renren.spilder.task.Task;
import it.renren.spilder.task.handler.Handler;
import it.renren.spilder.util.DateUtil;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import bsh.EvalError;

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

    private static ParentPage initParentPage(Document ruleXml) throws JDOMException, EvalError {
        ParentPage parentPageConfig = new ParentPage();
        parentPageConfig.setCharset(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/Charset/Value"));
        parentPageConfig.getUrlListPages().setValues((Element) (XPath.selectSingleNode(ruleXml, "/Rules/MainUrl/Values")));
        parentPageConfig.setImageDescUrl(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/ImageDescUrl/Value"));
        parentPageConfig.setImageSaveLocation(JDomUtil.getValueByXpath(ruleXml,
                                                                       "/Rules/MainUrl/ImageSaveLocation/Value"));
        parentPageConfig.setRandRecommandFrequency(Integer.parseInt(JDomUtil.getValueByXpath(ruleXml,
                                                                                             "/Rules/MainUrl/Recommend/Value").equals("") ? "0" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                           "/Rules/MainUrl/Recommend/Value")));
        parentPageConfig.setSRcommand(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/SRecommend/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                           "/Rules/MainUrl/SRecommend/Value")));
        parentPageConfig.setFilterDownloadUrl(JDomUtil.getValueByXpath(ruleXml,
                                                                       "/Rules/MainUrl/FilterDownloadUrl/Value") == null ? true : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                                "/Rules/MainUrl/FilterDownloadUrl/Value")));

        parentPageConfig.getContent().setStartList(XPath.selectNodes(ruleXml, "/Rules/MainUrl/MainRange/Start/Value"));
        parentPageConfig.getContent().setEndList(XPath.selectNodes(ruleXml, "/Rules/MainUrl/MainRange/End/Value"));

        parentPageConfig.getUrlFilter().setMustInclude(JDomUtil.getValueByXpath(ruleXml,
                                                                                "/Rules/MainUrl/UrlFilter/MustInclude/Value"));
        parentPageConfig.getUrlFilter().setMustNotInclude(JDomUtil.getValueByXpath(ruleXml,
                                                                                   "/Rules/MainUrl/UrlFilter/MustNotInclude/Value"));
        parentPageConfig.getUrlFilter().setCompByRegex(JDomUtil.getValueByXpath(ruleXml,
                                                                                "/Rules/MainUrl/UrlFilter/IsCompByRegex/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                                                "/Rules/MainUrl/UrlFilter/IsCompByRegex/Value")));
        parentPageConfig.setDesArticleId(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/DesArticleId/Value"));
        parentPageConfig.setAutoDetectTypeMapClass(JDomUtil.getValueByXpath(ruleXml,
                                                                            "/Rules/MainUrl/AutoDetect/TypeMapMakeClass") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                  "/Rules/MainUrl/AutoDetect/TypeMapMakeClass"));
        parentPageConfig.setOneUrlSleepTime(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/OneUrlSleepTime/Value") == null ? 0 : Long.parseLong(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                            "/Rules/MainUrl/OneUrlSleepTime/Value")));
        parentPageConfig.getTranslater().setFrom(JDomUtil.getValueByXpath(ruleXml,
                                                                          "/Rules/MainUrl/Translater/From/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                          "/Rules/MainUrl/Translater/From/Value"));
        parentPageConfig.getTranslater().setTo(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/Translater/To/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                               "/Rules/MainUrl/Translater/To/Value"));
        return parentPageConfig;
    }

    private static ChildPage initChildPage(Document ruleXml) throws JDOMException {
        ChildPage childPageConfig = new ChildPage();
        childPageConfig.setCharset(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Charset/Value"));
        childPageConfig.getTitle().setStart(JDomUtil.getValueByXpathNotTrim(ruleXml, "/Rules/Child/Title/Start/Value"));
        childPageConfig.getTitle().setEnd(JDomUtil.getValueByXpathNotTrim(ruleXml, "/Rules/Child/Title/End/Value"));
        childPageConfig.getTitle().setIssRegularExpression(Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                         "/Rules/Child/Title/Replace/IsRegularExpression/Value")));
        childPageConfig.getTitle().setFrom(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Title/Replace/From/Value"));
        childPageConfig.getTitle().setFrom(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Title/Replace/To/Value"));

        childPageConfig.getContent().setStartList(XPath.selectNodes(ruleXml, "/Rules/Child/Content/Start/Value"));
        childPageConfig.getContent().setEndList(XPath.selectNodes(ruleXml, "/Rules/Child/Content/End/Value"));

        childPageConfig.getContent().setIssRegularExpression(Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                           "/Rules/Child/Content/Replace/IsRegularExpression/Value")));
        childPageConfig.getContent().setFrom(JDomUtil.getValueByXpath(ruleXml,
                                                                      "/Rules/Child/Content/Replace/From/Value"));
        childPageConfig.getContent().setTo(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Content/Replace/To/Value"));
        childPageConfig.getContent().setWashContent(JDomUtil.getValueByXpath(ruleXml,
                                                                             "/Rules/Child/Content/WashContent/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                               "/Rules/Child/Content/WashContent/Value"));
        childPageConfig.getContent().setHandler(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Content/Handler/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                "/Rules/Child/Content/Handler/Value"));
        childPageConfig.setAddUrl(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/AddUrl/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                 "/Rules/Child/AddUrl/Value")));
        childPageConfig.setKeepFileName(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/KeepFileName/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                             "/Rules/Child/KeepFileName/Value")));
        return childPageConfig;
    }
    /**
     * ���������ļ���ȡ����
     * @param configFile
     * @throws Exception
     */
    private void saveFromConfigFile(String configFile) throws Exception {
        Document ruleXml = JDomUtil.getDocument(new File(configFile));
        ParentPage parentPageConfig = initParentPage(ruleXml);
        ChildPage childPageConfig = initChildPage(ruleXml);
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
                // System.out.println(mainContent);
                try {
                    mainContent = getMainContent(mainContent, parentPageConfig);
                } catch (Exception e) {
                    log4j.logError("�� url:" + listPageUrl + "�н�ȡ����Ҫ�����ݷ����쳣�������ļ�Ϊ��" + configFile);
                    throw new RuntimeException(e);
                }
                List<AHrefElement> childLinks = AHrefParser.ahrefParser(mainContent,
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
                        childUrl = makeUrl(listPageUrl, childUrl);
                        log4j.logDebug("��ǰ�����URL��" + childUrl);

                        detail.setUrl(childUrl);
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
                        detail.setContent(childContent);
                        String description = MetaParser.getMetaContent(childBody, childPageConfig.getCharset(),
                                                                       Constants.META_DESCRIPTIONS);
                        childBody = null;
                        if (description.equals("") && description.length() >= Constants.CONTENT_LEAST_LENGTH) {/*
                                                                                                                * ���û��ȡ����������
                                                                                                                * ��
                                                                                                                * ��ȡ�������ݵ�ǰ100���ַ�Ϊ����
                                                                                                                */
                            description = StringUtil.removeHtmlTags(childContent).trim().substring(0,
                                                                                                   Constants.CONTENT_LEAST_LENGTH);
                        }
                        detail.setDescription(description);
                        if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                            throw new RuntimeException("�����URL:" + childUrl + " ʱ����ȡ���������Ϊ��!");
                        }
                        childContent = replaceContent(childPageConfig, childContent);
                        childContent = saveImages(childUrl, childContent, parentPageConfig.getImageDescUrl(),
                                                  parentPageConfig.getImageSaveLocation(),
                                                  childPageConfig.getCharset(), detail);
                        if (childPageConfig.isAddUrl()) {
                            childContent = childContent + "<br>via��" + detail.getUrl();
                        }
                        detail.setContent(childContent);
                        handleContent(childPageConfig, detail);
                        if (!Environment.checkConfigFile) {
                            for (Task task : taskList) {
                                task.doTask(parentPageConfig, childPageConfig, detail.clone());
                            }
                        }
                    } catch (Exception e) {
                        failedLinks++;
                        if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES) {
                            isBreak = Boolean.TRUE;
                        }
                        log4j.logError("�����URLʱ�����쳣:" + childUrl, e);
                    }
                    detail.setContent(null);
                    detail.setDescription(null);
                    detail = null;
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* Ĭ����Ϣ10����һƪ���� */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime());
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
     * ���������ݽ������⴦�������������滻������ͨ��ʵ�ֵ�HANDLER����
     * 
     * @param childPageConfig
     * @param childContent
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static String replaceContent(ChildPage childPageConfig, String childContent) {
        /* ���������滻 */
        if (!childPageConfig.getContent().getFrom().equals("") && !childPageConfig.getContent().getTo().equals("")) {
            if (childPageConfig.getContent().isIssRegularExpression()) {
                childContent = childContent.replaceAll(childPageConfig.getContent().getFrom(),
                                                       childPageConfig.getContent().getTo());
            } else {
                childContent = childContent.replace(childPageConfig.getContent().getFrom(),
                                                    childPageConfig.getContent().getTo());
            }
        }
        return childContent;
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

    /**
     * ��ͼƬ���浽����
     * 
     * @param url ��ǰͼƬҳ�����ڵ�URL��ַ�������ǵ�ǰҳ��������ַ��������"http://www.renren.it" �� "http://www.renren.it/a/b.html"
     * @param content ����URL��ַ��ȡ�������ݣ�������ָ�����ݵ�����
     * @param imageDescUrl ��������ͼƬ��ַ��Ŀ���ַ����ͼƬ���ڷ�������ʲô�ط�����"/uploads/allimg/"��ע��·�����һ��Ҫ��"/"
     * @param imageSaveLocation ���ر���ͼƬ��·������"d:/t/"��ע��·�����һ��Ҫ��"/"
     * @return �滻ͼƬ·����ġ��������ȡ������
     * @throws Exception
     */
    private static String saveImages(String url, String content, String imageDescUrl, String imageSaveLocation,
                                     String charset, ChildPageDetail detail) throws Exception {
        String date = DateUtil.getNow("yyyy-MM-dd");
        imageDescUrl = imageDescUrl + date + "/";
        imageSaveLocation = imageSaveLocation + date + File.separator;
        File file = new File(imageSaveLocation);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<ImageElement> imageElements = ImageParser.imageParser(content, charset);
        boolean firstImage = true;
        for (ImageElement image : imageElements) {
            String imageSrc = image.getSrc();
            /* ��ȡ���ļ�����û��·�� */
            String newFileName = FileUtil.getNewFileName(imageSrc);
            /* ��װ��ǰ����ͼƬ��ŵ�·�� */
            String imageDes = imageDescUrl + newFileName;
            /* �滻ԭʼͼƬ��·�� */
            content = content.replace(imageSrc, imageDes);
            /* ����ȡ�����������ļ�����ʽд������ */
            /* ���ݵ�ǰ�ļ����ڷ��������Լ�ͼƬURL����ȡ��Զ�̷������ľ��Ե�ַ */
            String imageUrl = makeUrl(url, imageSrc);
            /* ��ȡԶ���ļ�������ָ��Ŀ¼�����棬�����Ϊĳ��ͼƬ��������Ǻ��Ըô��� */
            try {
                FileUtil.downloadFileByUrl(imageUrl, imageSaveLocation, newFileName);
                File savedImage = new File(imageSaveLocation + newFileName);
                if (savedImage.exists() && savedImage.length() > Constants.K) {// ֻ�д���1K��ͼƬ���ڵ�ʱ�򣬲Ž�����ͼƬ��Ϊ���棬����Ϊ����һ����ͼ������
                    detail.setPicArticle(true);
                    if (firstImage) {
                        detail.setLitpicAddress(imageDes);
                        firstImage = false;
                    }
                }
            } catch (Exception e) {/* �����ƴװ��ͼƬ��ַ�������쳣�����ٳ��Զ���ԭ��ַ���л�ȡ */
                FileUtil.downloadFileByUrl(imageSrc, imageSaveLocation, newFileName);
            }
        }

        return content;
    }

    /**
     * ���ݴ����url��ַ����ȡ�����ַ���紫��"http://www.163.com/a/b.html"���õ���ֵΪ"http://www.163.com"
     * 
     * @param url
     * @return
     */
    private static String getHost(String url) {
        String host = null;
        String urlTemp = url.replace("://", "");
        if (urlTemp.indexOf("/") > 0) {
            host = url.substring(0, urlTemp.indexOf("/") + 3);
        } else {
            host = url;
        }
        return host;
    }

    /**
     * ���ݵ�ǰҳ��url��ַ���Լ������ͼƬ��ַ(��Ҫ�������ͣ�һ������http��ʼ�ľ��Ե�ַ��һ������"/"��ͷ�ĵ�ַ������һ��ֱ�����ļ�������"aa.gif")��ƴװ��ͼƬ��url��ַ
     * 
     * @param url ��ǰҳ���url��ַ
     * @param fileUrl ��ǰͼƬ�ĵ�ַ
     * @return
     */
    private static String makeUrl(String url, String fileUrl) {
        String hostUrl = getHost(url);
        if (fileUrl.indexOf("://") > 0) {// ���Ե�ַ
            return fileUrl;
        } else if (!fileUrl.startsWith("/")) {
            String filename = FileUtil.getFileName(url);
            fileUrl = url.replace(filename, fileUrl);
        } else if (fileUrl.startsWith("/")) {
            if (hostUrl.endsWith("/")) {
                hostUrl = hostUrl.substring(0, hostUrl.length() - 2);
            }
            fileUrl = hostUrl + fileUrl;
        }
        return fileUrl;
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

        return childContent;
    }

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
}
