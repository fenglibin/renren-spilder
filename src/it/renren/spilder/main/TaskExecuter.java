package it.renren.spilder.main;

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
import java.util.List;

import org.htmlparser.util.ParserException;
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
     * 
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
                        childUrl = UrlUtil.makeUrl(listPageUrl, childUrl);
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
                        if (childPageConfig.isAddUrl()) {
                            childContent = childContent + "<br>From：<a href=\"" + detail.getUrl()
                                           + "\" target=\"_blank\">" + detail.getUrl() + "</a>";
                        }
                        // 将文章中的相对URL地址，替换为绝对的URL地址（开始）
                        childContent = replaceRelativePath2AbsolutePate(childUrl, childContent,
                                                                        childPageConfig.getCharset());
                        // 将文章中的相对URL地址，替换为绝对的URL地址（结束）
                        detail.setContent(childContent);
                        handleContent(childPageConfig, detail);
                        if (!Environment.checkConfigFile) {
                            for (Task task : taskList) {
                                task.doTask(parentPageConfig, childPageConfig, detail);
                            }
                        }
                    } catch (Exception e) {
                        failedLinks++;
                        if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES && Environment.dealOnePage) {
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

    /**
     * 将内容中链接的相对路径替换为绝对路径
     * 
     * @param childUrl 当前页面的URL
     * @param childContent 当前页面的内容
     * @param charset 当前页面的编码
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
}
