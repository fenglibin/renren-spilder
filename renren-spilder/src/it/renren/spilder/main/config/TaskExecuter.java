package it.renren.spilder.main.config;

import it.renren.spilder.filter.BodyFilter;
import it.renren.spilder.filter.Filter;
import it.renren.spilder.filter.MainBodyFilter;
import it.renren.spilder.filter.TitleFilter;
import it.renren.spilder.filter.seperatepage.ISeparatePage;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.htmlparser.util.ParserException;
import org.jdom.Document;

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
            save(ruleXml, parentPageConfig, parentPageConfig.getUrlListPages().getListPages(), childPageConfig,
                 configFile);
        } catch (Exception e) {
            log4j.logError("根据配置文件:" + configFile + " 进行处理内容异常发生：", e);
        } finally {
            childPageConfig = null;
            parentPageConfig = null;
            ruleXml = null;
        }

    }

    /**
     * 组成标准的URL
     * 
     * @param pageUrl
     * @param childLinks
     */
    private void makeStadardUrl(String pageUrl, Set<AHrefElement> childLinks) {
        for (AHrefElement link : childLinks) {
            String childUrl = link.getHref();
            childUrl = UrlUtil.makeUrl(pageUrl, childUrl);
            link.setHref(childUrl);
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
                      String configFile) throws ParserException, InterruptedException {
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
                mainContent = getMainContent(parentPageConfig, childPageConfig, mainContent);
            } catch (Exception e) {
                log4j.logError("从 url:" + listPageUrl + "中截取得需要的内容发生异常！配置文件为：" + configFile);
                if (Environment.isOutputHtmlContentWhenErrorHappend) {
                    log4j.logError("the content is :\n" + mainContent);
                }
                throw new RuntimeException(e);
            }
            Set<AHrefElement> childLinksList = AHrefParser.ahrefParser(mainContent,
                                                                       parentPageConfig.getUrlFilter().getMustInclude(),
                                                                       parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                       parentPageConfig.getCharset(),
                                                                       parentPageConfig.getUrlFilter().isCompByRegex());
            if (childLinksList.size() == 0) {
                log4j.logWarn("从页面中没有分析出需要的子URL，请检查匹配的表达式。");
            } else {
                makeStadardUrl(listPageUrl, childLinksList);
            }
            int failedLinks = 0;
            Set<AHrefElement> childLinksLists = new HashSet<AHrefElement>();
            for (AHrefElement link : childLinksList) {
                // 检查当前URL是否已经处理过了，这里要检查所有任务是否都处理过，如果都处理过了就不用进行后面的处理了，否则还要继续 end
                if (isBreak) {
                    break;
                }
                if (Environment.checkConfigFile) {
                    isBreak = Boolean.TRUE;
                }
                ChildPageDetail detail = new ChildPageDetail();
                String childUrl = link.getHref();
                log4j.logDebug("当前处理的URL：" + childUrl);
                // 检查当前URL是否已经处理过了，这里要检查所有任务是否都处理过，如果都处理过了就不用进行后面的处理了，否则还要继续 begin
                boolean isContinue = Boolean.FALSE;
                if (!Environment.checkConfigFile) {
                    for (Task task : taskList) {
                        if (!task.isDealed(childUrl)) {
                            isContinue = Boolean.TRUE;
                            break;
                        }
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
                    if (isDealed(childUrl)) {
                        log4j.logDebug("当前URL " + childUrl + " 已经有处理，不需要再次处理。");
                        continue;
                    }
                }
                Set<AHrefElement> childLinks = dealPage(parentPageConfig, childPageConfig, configFile, detail);
                makeStadardUrl(detail.getUrl(), childLinks);
                childLinksLists.addAll(childLinks);
                if (!detail.isDealResult()) {
                    failedLinks++;
                    if (failedLinks >= Constants.ONE_CONFIG_FILE_MAX_FAILED_TIMES && isDealOnePage(parentPageConfig)) {
                        isBreak = Boolean.TRUE;
                    }
                }
                detail = null;
                if (!Environment.checkConfigFile) {
                    if (parentPageConfig.getOneUrlSleepTime() == 0) {
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* 默认休息10秒钟一篇文章 */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime()
                                     + (long) (Math.random() * Constants.One_Url_Default_Sleep_Time));
                    }
                }
            }
            // 处理子页面分析出的URL
            for (AHrefElement link : childLinksLists) {
                // 检查当前URL是否已经处理过了，这里要检查所有任务是否都处理过，如果都处理过了就不用进行后面的处理了，否则还要继续 end
                if (isBreak) {
                    break;
                }
                if (Environment.checkConfigFile) {
                    isBreak = Boolean.TRUE;
                }
                ChildPageDetail detail = new ChildPageDetail();
                String childUrl = link.getHref();
                log4j.logDebug("当前处理的URL：" + childUrl);
                // 检查当前URL是否已经处理过了，这里要检查所有任务是否都处理过，如果都处理过了就不用进行后面的处理了，否则还要继续 begin
                boolean isContinue = Boolean.FALSE;
                if (!Environment.checkConfigFile) {
                    for (Task task : taskList) {
                        if (!task.isDealed(childUrl)) {
                            isContinue = Boolean.TRUE;
                            break;
                        }
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
                    if (isDealed(childUrl)) {
                        log4j.logDebug("当前URL " + childUrl + " 已经有处理，不需要再次处理。");
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
                        Thread.sleep(Constants.One_Url_Default_Sleep_Time);/* 默认休息10秒钟一篇文章 */
                    } else {
                        Thread.sleep(parentPageConfig.getOneUrlSleepTime()
                                     + (long) (Math.random() * Constants.One_Url_Default_Sleep_Time));
                    }
                }
            }
        }
    }

    /**
     * 判断当前配置是否只处理一页。<br>
     * 1、如果用户的配置文件中配置了节点/Rules/MainUrl/DealOnePage，则以用户配置的值为准，该值只要不为true，则返回false;<br>
     * 2、如果用户没有配置节点/Rules/MainUrl/DealOnePage，则以命令行是否传入"-p"参数确定
     * 
     * @param parentPageConfig
     * @return
     */
    private boolean isDealOnePage(ParentPage parentPageConfig) {
        boolean result = false;
        String dealOnePage = parentPageConfig.getDealOnePage();
        if (StringUtil.isEmpty(dealOnePage)) {// 没有设置
            result = Environment.dealOnePage;
        } else {
            if (dealOnePage.equalsIgnoreCase("true")) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 分页与分页的，一起处理。并且返回当前页面中符合配置标准的URL
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param configFile
     * @param detail
     */
    private Set<AHrefElement> dealPage(ParentPage parentPageConfig, ChildPage childPageConfig, String configFile,
                                       ChildPageDetail detail) {
        boolean isPageAnalysisOk = Boolean.TRUE;
        // 用于存储从子页面中获取的符合要求的URL
        Set<AHrefElement> childLinksLists = new HashSet<AHrefElement>();
        for (int currentSeparatePage = 1; currentSeparatePage <= childPageConfig.getContent().getSeparatePageMaxPages(); currentSeparatePage++) {
            try {
                String childUrl = "";
                childUrl = getSeparatePageUrl(detail.getUrl(), currentSeparatePage, childPageConfig);
                String htmlContent = HttpClientUtil.getGetResponseWithHttpClient(childUrl, childPageConfig.getCharset());

                Set<AHrefElement> childLinksList = AHrefParser.ahrefParser(htmlContent,
                                                                           parentPageConfig.getUrlFilter().getMustInclude(),
                                                                           parentPageConfig.getUrlFilter().getMustNotInclude(),
                                                                           parentPageConfig.getCharset(),
                                                                           parentPageConfig.getUrlFilter().isCompByRegex());
                childLinksLists.addAll(childLinksList);

                String htmlBody = getBody(parentPageConfig, childPageConfig, htmlContent);
                if (currentSeparatePage > 1) {// 支持分页采集
                    detail.setContent(detail.getContent() + Constants.DEDE_SEPARATE_PAGE_STRING + htmlBody);
                } else {
                    detail.setContent(htmlBody);
                }
                if (currentSeparatePage == 1) {// 只有第一页才获取标题、关键字、描述，后面的分页就不需要获取了，直接使用第一页获取的就可
                    String htmlTitle = getTitle(parentPageConfig, childPageConfig, htmlContent);
                    if (htmlTitle.indexOf("404") > 0) {// 当请求时，返回了404页面则退出了
                        break;
                    }
                    detail.setTitle(htmlTitle);
                    String keywords = MetaParser.getMetaContent(htmlContent, childPageConfig.getCharset(),
                                                                Constants.META_KEYWORDS);
                    if (keywords.equals("")) {/* 如果没有关键字，就取文章的标题为关键字 */
                        keywords = htmlTitle;
                    }
                    detail.setKeywords(keywords);

                    String childContentWithoutHtmlTagTrim = StringUtil.removeHtmlTags(htmlBody).trim();
                    if (StringUtil.isEmpty(htmlBody)
                        || childContentWithoutHtmlTagTrim.length() < parentPageConfig.getContent().getMinLength()) {
                        throw new RuntimeException("当前获取到内容长度小于：" + parentPageConfig.getContent().getMinLength());
                    }
                    if (childContentWithoutHtmlTagTrim.length() > Constants.CONTENT_LEAST_LENGTH) {// 加这个逻辑判断的原因是因为有些时候要获取的内容本身就是小于100的，如只获取页面中的电子邮件。通过通过上面的文章内容长度的检查，那说明当前内容是合法的
                        String description = childContentWithoutHtmlTagTrim.substring(0, Constants.CONTENT_LEAST_LENGTH);
                        detail.setDescription(description);
                    } else {
                        detail.setDescription(htmlTitle);
                    }
                    detail.setReplys(getReplyList(htmlContent, childPageConfig));
                    htmlContent = null;
                    if (detail.getTitle().equals("") || detail.getContent().equals("")) {
                        throw new RuntimeException("处理该URL:" + childUrl + " 时，获取标题或内容为空!");
                    }
                }
                if (parentPageConfig.isSaveImage() && !Environment.checkConfigFile) {
                    // 保存图片
                    UrlUtil.saveImages(parentPageConfig, childPageConfig, detail);
                }
            } catch (Exception e) {
                if (currentSeparatePage > 1) {
                    break;
                } else {
                    isPageAnalysisOk = false;
                    log4j.logError("处理该URL时发生异常:" + detail.getUrl() + ",配置文件:" + configFile, e);
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
                    log4j.logError("当前配置文件：" + configFile + " 配置测试成功。");
                }
                detail.setDealResult(Boolean.TRUE);
            }
        } catch (Exception e) {
            log4j.logError("处理该URL时发生异常:" + detail.getUrl() + ",配置文件:" + configFile, e);
        }
        return childLinksLists;
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

    /**
     * 获取标题
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param htmlContent
     * @return
     * @throws Exception
     */
    private static String getTitle(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent)
                                                                                                              throws Exception {
        Filter titleFilter = new TitleFilter();
        return titleFilter.filterContent(parentPageConfig, childPageConfig, htmlContent);
    }

    /**
     * 获取网页内容body
     * 
     * @param parentPageConfig
     * @param childPageConfig
     * @param htmlContent
     * @return
     * @throws Exception
     */
    private static String getBody(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent)
                                                                                                             throws Exception {
        Filter titleFilter = new BodyFilter();
        return titleFilter.filterContent(parentPageConfig, childPageConfig, htmlContent);
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
        if (!StringUtil.isEmpty(childPageConfig.getContent().getHandler())) {
            Handler handler = (Handler) Class.forName(childPageConfig.getContent().getHandler()).newInstance();
            handler.execute(detail);
        }
    }

    /**
     * 获取文章列表的内容
     * 
     * @param mainContent
     * @param parentPageConfig
     * @return
     * @throws RuntimeException
     */
    private static String getMainContent(ParentPage parentPageConfig, ChildPage childPageConfig, String mainContent)
                                                                                                                    throws Exception {
        Filter filter = new MainBodyFilter();
        return filter.filterContent(parentPageConfig, childPageConfig, mainContent);
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

        if (!StringUtil.isEmpty(childPageConfig.getReplys().getStart())
            && !StringUtil.isEmpty(childPageConfig.getReplys().getEnd())) {// 判断是否有获取回复的配置
            if (!StringUtil.isEmpty(childPageConfig.getReplys().getReply().getStart())
                && !StringUtil.isEmpty(childPageConfig.getReplys().getReply().getEnd())) {/*
                                                                                           * 配置了子回复节点，就根据子回复节点的配置来处理，
                                                                                           * 主配置用于截取帶有所有回復內容的部份
                                                                                           * ，而取出每一個回復則在子配置中
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
            } else {/* 只配置了主配置，那就根据主配置获取回复内容 */
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
     * 获取分页的URL
     * 
     * @param mainUrl
     * @param currentSeparatePage
     * @param childPageConfig
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static String getSeparatePageUrl(String mainUrl, int currentSeparatePage, ChildPage childPageConfig)
                                                                                                                throws InstantiationException,
                                                                                                                IllegalAccessException,
                                                                                                                ClassNotFoundException {
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

}
