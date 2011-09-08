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

    /**
     * 根据配置文件获取内容
     * 
     * @param configFile
     * @throws Exception
     */
    private void saveFromConfigFile(String configFile) throws Exception {
        Document ruleXml = JDomUtil.getDocument(new File(configFile));
        ParentPage parentPageConfig = Config.initParentPage(ruleXml);
        ChildPage childPageConfig = Config.initChildPage(ruleXml);
        try {
            // 用于保存已经处理了的BLOG主页URL的MAP
            ConcurrentMap<String, String> blogHomeUrlMap = new ConcurrentHashMap<String, String>();
            save(ruleXml, parentPageConfig, parentPageConfig.getUrlListPages().getListPages(), childPageConfig,
                 configFile, blogHomeUrlMap);

            // 以下是获取当前博客的博客主页上面的文章
            // 将MAP转换为List
            List<String> blogHomeUrlList = new ArrayList<String>();
            if (blogHomeUrlMap.size() > 0) {
                for (String url : blogHomeUrlMap.keySet()) {
                    blogHomeUrlList.add(url);
                }
            }
            blogHomeUrlMap = null;
            save(ruleXml, parentPageConfig, blogHomeUrlList, childPageConfig, configFile, blogHomeUrlMap);
        } catch (Exception e) {
            log4j.logError("根据配置文件:" + configFile + " 进行处理内容异常发生：", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /**
     * 保存
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
                log4j.logError("从 url:" + listPageUrl + "获取得内容发生异常！配置文件为：" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            try {
                mainContent = getMainContent(mainContent, parentPageConfig);
            } catch (Exception e) {
                log4j.logError("从 url:" + listPageUrl + "中截取得需要的内容发生异常！配置文件为：" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            List<AHrefElement> childLinksList = AHrefParser.ahrefParser(
                                                                        mainContent,
                                                                        parentPageConfig.getUrlFilter().getMustInclude(),
                                                                        parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                        parentPageConfig.getCharset(),
                                                                        parentPageConfig.getUrlFilter().isCompByRegex());
            int failedLinks = 0;
            for (AHrefElement link : childLinksList) {
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
                    String blogHomeUrl = analysisBlogHomeUrl(parentPageConfig.getBlogType(), childUrl);
                    if (blogHomeUrlMap != null && !StringUtil.isNull(blogHomeUrl)) {
                        blogHomeUrlMap.put(blogHomeUrl, blogHomeUrl);
                    }
                    detail.setUrl(childUrl);
                    if (!Environment.checkConfigFile) {
                        if (isDealed(childUrl)) {
                            log4j.logDebug("当前URL " + childUrl + " 已经有处理，不需要再次处理。");
                            continue;
                        }
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
                    if (keywords.equals("")) {/* 如果没有关键字，就取文章的标题为关键字 */
                        keywords = childTitle;
                    }
                    detail.setKeywords(keywords);

                    String childContent = getChildContent(childBody, childPageConfig);
                    /** 去掉script标签 */
                    childContent = StringUtil.removeScriptAndHrefTags(childContent);
                    if (StringUtil.isNull(childContent)
                        || StringUtil.removeHtmlTags(childContent).trim().length() <= Constants.CONTENT_LEAST_LENGTH) {
                        throw new RuntimeException("当前获取到内容长度小于：" + Constants.CONTENT_LEAST_LENGTH);
                    }
                    detail.setContent(childContent);
                    detail.setReplys(getReplyList(childBody, childPageConfig));
                    childBody = null;
                    String description = StringUtil.removeHtmlTags(childContent).trim().substring(
                                                                                                  0,
                                                                                                  Constants.CONTENT_LEAST_LENGTH);
                    detail.setDescription(description);
                    if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                        throw new RuntimeException("处理该URL:" + childUrl + " 时，获取标题或内容为空!");
                    }
                    childContent = StringUtil.replaceContent(childContent, childPageConfig.getContent().getFrom(),
                                                             childPageConfig.getContent().getTo(),
                                                             childPageConfig.getContent().isIssRegularExpression());

                    // 将文章中的相对URL地址，替换为绝对的URL地址（开始）
                    // childContent = replaceRelativePath2AbsolutePate(childUrl,
                    // childContent,childPageConfig.getCharset());
                    // 将文章中的相对URL地址，替换为绝对的URL地址（结束）
                    // 替换目前发现的一些问题，如获取到文章中有八个问号等
                    childContent = childContent.replace("????", "");
                    detail.setContent(childContent);
                    handleContent(childPageConfig, detail);
                    // 保存图片
                    UrlUtil.saveImages(parentPageConfig, childPageConfig, detail);
                    if (!Environment.checkConfigFile) {
                        for (Task task : taskList) {
                            task.doTask(parentPageConfig, childPageConfig, detail);
                        }
                    } else {
                        log4j.logError("当前配置文件：" + configFile + " 配置测试成功。");
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
                if (!Environment.checkConfigFile) {
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* 默认休息10秒钟一篇文章 */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime());
                    }
                }
            }
        }
    }

    /**
     * 当前Url是否已经处理过了
     * 
     * @param url
     * @return
     */
    private boolean isDealed(String url) {
        boolean result = Boolean.FALSE;
        for (Task task : taskList) {
            result = task.isDealed(url);
            if (result == Boolean.FALSE) {// 只要是有一种没有处理就认为是没有处理
                return result;
            }
        }
        return result;
    }

    /* 标题替换 */
    private static String replaceTitle(ChildPage childPageConfig, String childTitle) {
        if (!StringUtil.isNull(childPageConfig.getTitle().getFrom())
            && !StringUtil.isNull(childPageConfig.getTitle().getTo())) {
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
                childContent = StringUtil.subString(
                                                    childBody,
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

    /**
     * 获取文章列表的内容
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

    /**
     * 获取回复
     * 
     * @param childBody
     * @param childPageConfig
     * @return
     * @throws RuntimeException
     */
    private static List<String> getReplyList(String childBody, ChildPage childPageConfig) throws RuntimeException {
        List<String> replysList = new ArrayList<String>();

        if (!StringUtil.isNull(childPageConfig.getReplys().getStart())
            && !StringUtil.isNull(childPageConfig.getReplys().getEnd())) {// 判断是否有获取回复的配置
            if (!StringUtil.isNull(childPageConfig.getReplys().getReply().getStart())
                && !StringUtil.isNull(childPageConfig.getReplys().getReply().getEnd())) {/*
                                                                                          * 配置了子回复节点，就根据子回复节点的配置来处理，
                                                                                          * 主配置用于截取帶有所有回復內容的部份
                                                                                          * ，而取出每一個回復則在子配置中
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
            } else {/* 只配置了主配置，那就根据主配置获取回复内容 */
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
     * 根据当前的博客类型，以及当前博客的一个URL地址，获取当前博客的主页地址
     * 
     * @param blogType 博客类型，目前1表示类CSDN的博客，博客不是独立的域名；2表示类ITEYE的博客，博客有独立的域名
     * @param childUrl 当前博客的一个URL
     * @return 当前博客的主页地址
     */
    private static String analysisBlogHomeUrl(int blogType, String childUrl) {
        String blogHomeUrl = "";
        if (blogType > 0) {
            if (blogType == 1) {
                String host = UrlUtil.getHost(childUrl);
                childUrl = childUrl.replace(host, "");
                String user = StringUtil.subStringSmart(childUrl, "/", "/");
                blogHomeUrl = host + "/" + user;
            } else if (blogType == 2) {
                blogHomeUrl = UrlUtil.getHost(childUrl);
            }
        }
        return blogHomeUrl;
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
