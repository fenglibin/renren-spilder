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
    // 文件还是目录
    boolean              isFile = false;
    // 要执行的配置文件或目录的名称
    String               configName;
    long                 oneFileSleepTime;
    // 需要执行的任务列表，目前有简体中文操作及将简体转换为繁体，实现类需要继承接口it.renren.spilder.task.Task
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
            log4j.logError("当前执行异常:" + configName, e);
        }
    }

    /**
     * 执行某个目录中所有的配置文件，且指定了每个配置文件执行完后，暂停的时间
     * 
     * @param configDirectory 当前配置文件所在目录
     * @param oneFileSleepTime 每个配置文件执行完后暂停的时间
     * @throws Exception
     */
    private void doSaveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        File configFilePath = new File(configDirectory);
        File[] files = configFilePath.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".xml")) {/* 只读取XML配置文件 */
                try {
                    log4j.logDebug("当前处理的文件:" + file.getAbsolutePath());
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
     * 根据配置文件获取内容
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
                    log4j.logError("从 url:" + listPageUrl + "获取得内容发生异常！配置文件为：" + configFile);
                    throw new RuntimeException(e);
                }
                // System.out.println(mainContent);
                try {
                    mainContent = getMainContent(mainContent, parentPageConfig);
                } catch (Exception e) {
                    log4j.logError("从 url:" + listPageUrl + "中截取得需要的内容发生异常！配置文件为：" + configFile);
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
                        log4j.logDebug("当前处理的URL：" + childUrl);

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
                        if (keywords.equals("")) {/* 如果没有关键字，就取文章的标题为关键字 */
                            keywords = childTitle;
                        }
                        detail.setKeywords(keywords);

                        String childContent = getChildContent(childBody, childPageConfig);
                        detail.setContent(childContent);
                        String description = MetaParser.getMetaContent(childBody, childPageConfig.getCharset(),
                                                                       Constants.META_DESCRIPTIONS);
                        childBody = null;
                        if (description.equals("") && description.length() >= Constants.CONTENT_LEAST_LENGTH) {/*
                                                                                                                * 如果没有取得文章描述
                                                                                                                * ，
                                                                                                                * 就取文章内容的前100个字符为描述
                                                                                                                */
                            description = StringUtil.removeHtmlTags(childContent).trim().substring(0,
                                                                                                   Constants.CONTENT_LEAST_LENGTH);
                        }
                        detail.setDescription(description);
                        if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                            throw new RuntimeException("处理该URL:" + childUrl + " 时，获取标题或内容为空!");
                        }
                        childContent = replaceContent(childPageConfig, childContent);
                        childContent = saveImages(childUrl, childContent, parentPageConfig.getImageDescUrl(),
                                                  parentPageConfig.getImageSaveLocation(),
                                                  childPageConfig.getCharset(), detail);
                        if (childPageConfig.isAddUrl()) {
                            childContent = childContent + "<br>via：" + detail.getUrl();
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
                        log4j.logError("处理该URL时发生异常:" + childUrl, e);
                    }
                    detail.setContent(null);
                    detail.setDescription(null);
                    detail = null;
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* 默认休息10秒钟一篇文章 */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime());
                    }
                }
            }
        } catch (Exception e) {
            log4j.logError("根据配置文件:" + configFile + " 进行处理内容异常发生：", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /* 标题替换 */
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
     * 对文章内容进行特殊处理，如文章内容替换，或者通过实现的HANDLER处理
     * 
     * @param childPageConfig
     * @param childContent
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static String replaceContent(ChildPage childPageConfig, String childContent) {
        /* 文章内容替换 */
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
     * 对文章内容通过实现类，进行特殊的处理
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
        /* 文章内容通过HANDLER特殊处理 */
        if (!StringUtil.isNull(childPageConfig.getContent().getHandler())) {
            Handler handler = (Handler) Class.forName(childPageConfig.getContent().getHandler()).newInstance();
            handler.execute(detail);
        }
    }

    /**
     * 将图片保存到本地
     * 
     * @param url 当前图片页面所在的URL地址，或者是当前页面的主域地址；可以是"http://www.renren.it" 或 "http://www.renren.it/a/b.html"
     * @param content 根据URL地址获取到的内容，或者是指定部份的内容
     * @param imageDescUrl 更改网络图片地址的目标地址，将图片放在服务器的什么地方，如"/uploads/allimg/"，注：路径最后一定要带"/"
     * @param imageSaveLocation 本地保存图片的路径，如"d:/t/"，注：路径最后一定要带"/"
     * @return 替换图片路径后的、从网络获取的内容
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
            /* 获取新文件名，没有路径 */
            String newFileName = FileUtil.getNewFileName(imageSrc);
            /* 组装当前下载图片存放的路径 */
            String imageDes = imageDescUrl + newFileName;
            /* 替换原始图片的路径 */
            content = content.replace(imageSrc, imageDes);
            /* 将获取到的内容以文件的形式写到本地 */
            /* 根据当前文件所在服务器，以及图片URL，获取其远程服务器的绝对地址 */
            String imageUrl = makeUrl(url, imageSrc);
            /* 获取远程文件到本地指定目录并保存，如果因为某张图片处理错误，那忽略该错误 */
            try {
                FileUtil.downloadFileByUrl(imageUrl, imageSaveLocation, newFileName);
                File savedImage = new File(imageSaveLocation + newFileName);
                if (savedImage.exists() && savedImage.length() > Constants.K) {// 只有大于1K的图片存在的时候，才将这张图片作为封面，并认为这是一个带图的文章
                    detail.setPicArticle(true);
                    if (firstImage) {
                        detail.setLitpicAddress(imageDes);
                        firstImage = false;
                    }
                }
            } catch (Exception e) {/* 如果对拼装的图片地址处理发生异常，那再尝试对其原地址进行获取 */
                FileUtil.downloadFileByUrl(imageSrc, imageSaveLocation, newFileName);
            }
        }

        return content;
    }

    /**
     * 根据传入的url地址，获取主域地址，如传入"http://www.163.com/a/b.html"，得到的值为"http://www.163.com"
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
     * 根据当前页的url地址，以及传入的图片地址(主要三种类型，一种是以http开始的绝对地址；一种是以"/"开头的地址；另外一种直接是文件名，如"aa.gif")，拼装该图片的url地址
     * 
     * @param url 当前页面的url地址
     * @param fileUrl 当前图片的地址
     * @return
     */
    private static String makeUrl(String url, String fileUrl) {
        String hostUrl = getHost(url);
        if (fileUrl.indexOf("://") > 0) {// 绝对地址
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
            log4j.logError("根据URL获取名称发生异常。", e);
            url = "";
        }
        return url;
    }

    /**
     * 针对文章列表中的具体文章内容的形式进行不同的处理，以确定能够最终获取到文章。
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
                    log4j.logDebug("第 " + (i + 1) + " 次获取文章内容出错！");
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
                    log4j.logDebug("第 " + (i + 1) + " 次获取文章内容出错！");
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
