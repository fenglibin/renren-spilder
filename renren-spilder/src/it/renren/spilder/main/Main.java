package it.renren.spilder.main;

/*
 * 配制文件模板： <?xml version="1.0" encoding="gbk"?> <Rules> <MainUrl> <Values desc="链接的列表地址，Value中的列表地址可以是多条，以逗号分开">
 * <!--Value desc="当前主页面的Url地址"> <![CDATA[http://blog.csdn.net/zhengyun_ustc/category/30537.aspx?PageNumber=(*)]]>
 * </Value--> <BatValues desc="批量增加，支持通配置符'(*)'"> <Value> <![CDATA[http://java.dzone.com/frontpage?page=(*)]]> </Value>
 * <WildcastType desc="通配符的类型:1表示数字，2表示a-z的字母"> <Value> <![CDATA[1]]> </Value> <Start> <Value> <![CDATA[1]]> </Value>
 * </Start> <End> <Value> <![CDATA[248]]> </Value> </End> </WildcastType> </BatValues> </Values> <DesArticleId>
 * <Value>23</Value> </DesArticleId> <AutoDetect>
 * <TypeMapMakeClass>it.renren.spilder.type.DedecmsTypesMap</TypeMapMakeClass> </AutoDetect> <MainRange desc="内容范围">
 * <Start> <Value><![CDATA[</a> </li></ul>]]></Value> </Start> <End> <Value><![CDATA[</p></div></div> <script
 * ]]></Value> </End> </MainRange> <Charset desc="编码"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <ImageDescUrl
 * desc="将原内容中的图片地址替换、用户保存图片的相对路径"> <Value> <![CDATA[/uploads/allimg/]]> </Value> </ImageDescUrl> <ImageSaveLocation
 * desc="保存图片的路径，将图片写出到硬盘上"> <Value> <![CDATA[/home/fenglibin/www/www.renren.it/uploads/allimg/]]> </Value>
 * </ImageSaveLocation> <Recommend desc="随机推荐数，即多少篇文章推荐一篇.'0'表示不随机推荐"> <Value><![CDATA[3]]></Value> </Recommend>
 * <UrlFilter url="url筛选"> <MustInclude desc="url中必须包括的字符串"> <Value> <![CDATA[articles]]> </Value> </MustInclude>
 * <MustNotInclude desc="url中不能够包括的字符串"> <Value> <![CDATA[]]> </Value> </MustNotInclude> </UrlFilter> <Translater
 * desc="翻译配置.en指英语,cn指简体中文,big5指繁体中文"> <From desc="原语言"> <Value> <![CDATA[en]]> </Value> </From> <To desc="目标语言">
 * <Value> <![CDATA[cn]]> </Value> </To> </Translater> <OneUrlSleepTime> <Value>60000</Value> </OneUrlSleepTime>
 * </MainUrl> <Child> <Charset desc="编码"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <Title desc="标题"> <Start>
 * <Value><![CDATA[<h1>]]></Value> </Start> <End> <Value><![CDATA[</h1>]]></Value> </End> <Replace desc="替换标题">
 * <IsRegularExpression desc="是否正则表达式，值只能够是true或false"> <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From
 * desc="待替换标题部分"> <Value> <![CDATA[]]> </Value> </From> <To desc="目标部份"> <Value> <![CDATA[]]> </Value> </To> </Replace>
 * </Title> <Content desc="内容"> <Start> <Value><![CDATA[<span class='print-link'></span>]]></Value> </Start> <End>
 * <Value><![CDATA[<div class="fivestar-static-form-item">]]></Value> </End> <Replace desc="替换内容"> <IsRegularExpression
 * desc="是否正则表达式"> <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From desc="原内容"> <Value> <![CDATA[]]>
 * </Value> </From> <To desc="目标内容"> <Value> <![CDATA[]]> </Value> </To> </Replace> </Content> <AddUrl
 * desc="是否将当前文章采集页的地址加到内容的尾部，值只可以是true中或false"> <Value> <![CDATA[true]]> </Value> </AddUrl> <KeepFileName
 * desc="是否保留原获取网页的文件名"> <Value> <![CDATA[true]]> </Value> </KeepFileName> </Child> </Rules>
 */

import it.renren.spilder.main.config.TaskExecuter;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 系统主类，系统启动通过该类启动 类Main.java的实现描述：TODO 类实现描述
 * 
 * @author fenglibin 2011-4-10 下午04:04:42
 */
public class Main {

    private static Log4j                          log4j = new Log4j(Main.class.getName());
    private static ConfigurableApplicationContext ctx   = null;

    /**
     * 系统运行参数 类Main.java的实现描述：TODO 类实现描述
     * 
     * @author fenglibin 2011-4-10 下午04:02:38
     */
    public class Param {

        // 是否文件
        private boolean isFile           = false;
        // 是否目录
        private boolean isDirectory      = false;
        // 是文件的情况下，指定文件名
        private String  fileName         = "";
        // 是目录的情况下，指定目录名
        private String  directoryName    = "";
        // 单个配置文件执行完后，系统暂停的时间表,以毫秒为单位
        private String  oneFileSleepTime = "";
        // 所有配置执行完成后，下次再次执行的等待时间,以毫秒为单位
        private String  loopSleepTime    = "";
        // Spring的配置文件路径
        private String  springConfigFile;

        public boolean isFile() {
            return isFile;
        }

        public void setFile(boolean isFile) {
            this.isFile = isFile;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public void setDirectory(boolean isDirectory) {
            this.isDirectory = isDirectory;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getDirectoryName() {
            return directoryName;
        }

        public void setDirectoryName(String directoryName) {
            this.directoryName = directoryName;
        }

        public String getOneFileSleepTime() {
            return oneFileSleepTime;
        }

        public void setOneFileSleepTime(String oneFileSleepTime) {
            this.oneFileSleepTime = oneFileSleepTime;
        }

        public String getLoopSleepTime() {
            return loopSleepTime;
        }

        public void setLoopSleepTime(String loopSleepTime) {
            this.loopSleepTime = loopSleepTime;
        }

        // 单个文件中，是否只获取配置文件中指定的第一页内容
        public void setDealOnePage(boolean dealOnePage) {
            Environment.dealOnePage = dealOnePage;
        }

        // 用于表示测试该分类下面的配置文件是否能够正常工作，关位进行数据库的操作，并且一个配置文件只检测一条记录
        public void setCheckConfigFile(boolean checkConfigFile) {
            Environment.checkConfigFile = checkConfigFile;
        }

        public String getSpringConfigFile() {
            return springConfigFile;
        }

        public void setSpringConfigFile(String springConfigFile) {
            this.springConfigFile = springConfigFile;
        }

        public void setUseProxyTrue() {
            Environment.isUseProxy = Boolean.TRUE;
        }

        public void setProxyHost(String proxy) {
            Environment.proxy = proxy;
        }

        public void setTablePrefix(String tablePrefix) {
            Environment.tablePrefix = tablePrefix;
        }

        public void setExitByFailedDealPages(String exitByFailedDealPages) {
            Environment.exitByFailedDealPages = Boolean.parseBoolean(exitByFailedDealPages);
        }

        public void enableOutputHtmlContentWhenErrorHappend() {
            Environment.isOutputHtmlContentWhenErrorHappend = Boolean.TRUE;
        }

        public void setSaveImage2CurrentHtmlFileTileNameDir() {
            Environment.isSaveImage2CurrentHtmlFileTileNameDir = Boolean.TRUE;
        }

        public void setImageSite() {
            Environment.isImageSite = Boolean.TRUE;
        }

        public void setWaterImageLocation(String waterImageLocation) {
            Environment.waterImageLocation = waterImageLocation;
        }

        public void setCookFile(String cookFile) {
            Environment.cookFile = cookFile;
        }

    }

    /**
     * 对输入的参数进行处理
     * 
     * @param args 传入的原参数数组
     */
    private Param initParameters(String[] args) {
        String value = "";
        Main.Param param = (new Main()).new Param();
        for (int i = 0; i < args.length; i++) {
            value = args[i];
            if (value != null && value.indexOf("help") >= 0) {
                System.err.println(Constants.USE_AGE);
                System.exit(0);
            }
            if (value.startsWith("-f")) {// 文件
                param.setFile(Boolean.TRUE);
                param.setFileName(value.replace("-f=", "").replace("-f", ""));
            } else if (value.startsWith("-d")) {// 目录
                param.setDirectory(Boolean.TRUE);
                param.setDirectoryName(value.replace("-d=", "").replace("-d", ""));
            } else if (value.startsWith("-o")) {// 单个文件执行完后，暂停的时间，以毫秒为单位
                param.setOneFileSleepTime(value.replace("-o=", "").replace("-o", ""));
            } else if (value.startsWith("-l")) {// 循环执行某文件，暂停的时候，以毫秒为单位
                param.setLoopSleepTime(value.replace("-l=", "").replace("-l", ""));
            } else if (value.startsWith("-p")) {// 单个文件中，是否只获取配置文件中指定的第一页内容
                param.setDealOnePage(Boolean.TRUE);
            } else if (value.startsWith("-check")) {// 用于表示测试该分类下面的配置文件是否能够正常工作，关位进行数据库的操作，并且一个配置文件只检测一条记录
                param.setCheckConfigFile(Boolean.TRUE);
            } else if (value.startsWith("-spring")) {// 指定SPRING的配置文件
                param.setSpringConfigFile(value.replace("-spring=", "").replace("-spring", ""));
            } else if (value.startsWith("-proxy")) {// 使用代理
                param.setUseProxyTrue();
                param.setProxyHost(value.replace("-proxy", ""));
            } else if (value.startsWith("-tablePrefix")) {// 表前缀，支持在不修改配置的情况下，在往表中写入内容时，指定不同的表前缀
                param.setTablePrefix(value.replace("-tablePrefix=", "").replace("-tablePrefix", ""));
            } else if (value.startsWith("-exitByFailedDealPages")) {// 当处理文章，失败到达一定的数量时，退出控制标识
                param.setExitByFailedDealPages(value.replace("-exitByFailedDealPages", ""));
            } else if (value.startsWith("-OutputHtmlContentWhenErrorHappend")) {// 当处理文章，失败到达一定的数量时，退出控制标识
                param.enableOutputHtmlContentWhenErrorHappend();
            } else if (value.startsWith("-SaveImage2CurrentHtmlFileTileNameDir")) {// 保存图片时先根据标题建立文件夹
                param.setSaveImage2CurrentHtmlFileTileNameDir();
            } else if (value.startsWith("-isImageSite")) {// 是否图片站，这个用于控制在获取的每片文章都加上头条及特荐
                param.setImageSite();
            } else if (value.startsWith("-waterImageLocation")) {// 为采集的图片增加水印的水印图片地址
                param.setWaterImageLocation(value.replace("-waterImageLocation=", "").replace("-waterImageLocation", ""));
            } else if (value.startsWith("-cookFile")) {// 设置cookie存放的文件，因为cookie内容比较多，就不传到参数里了，直接放到文件文件中读取
                param.setCookFile(value.replace("-cookFile=", "").replace("-cookFile", ""));
            }

        }
        return param;
    }

    public static void main(String[] args) {
        log4j.logDebug("log4jdebuug");
        Main main = new Main();
        Main.Param param = main.new Param();
        param = main.initParameters(args);
        if (args.length == 0) {
            args = new String[1];
            // 文件测试
            param.setFile(true);
            param.setFileName("config/google.org.cn/rule_google.org.cn_wordpressblog.xml");
            param.setFileName("config/www.blogjava.net/model.xml");
            param.setFileName("config/developer.51cto.com.xml");
            param.setFileName("config/blog.oschina.net.xml");
            param.setFileName("config/moandroid.com.xml");
            param.setFileName("config/normal/headnews/rule_sina_tech_headnews.xml");
            param.setFileName("config/normal/headnews/cnbeta_index.xml");
            // Environment.isImageSite = Boolean.TRUE;
            // Environment.isSaveImage2CurrentHtmlFileTileNameDir = Boolean.TRUE;
            // 文件夹测试
            // param.setDirectory(true);
            // param.setDirectoryName("config/headnews");
            // param.setDealOnePage(true);

            param.setSpringConfigFile("beans.xml");
            param.setCheckConfigFile(Boolean.TRUE);

            // param.setUseProxyTrue();
            // param.setProxyHost("221.7.145.42:8080");
        }
        String springConfigFile = param.getSpringConfigFile();
        if (StringUtil.isEmpty(springConfigFile)) {
            springConfigFile = Constants.SPRING_CONFIG_FILE;
        }
        ctx = new FileSystemXmlApplicationContext(new String[] { springConfigFile });
        try {
            main.startWork(param);
        } catch (Exception e) {
            log4j.logError(e);
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ctx.close();
                log4j.logWarn("the server is shutdown!");
            }
        });
    }

    private void startWork(Param param) throws Exception {
        if (param.isFile()) {
            if (!(new File(param.getFileName()).exists())) {
                System.err.println("Config File:" + param.getFileName() + " not exists!");
                System.exit(0);
            }
            TaskExecuter taskExecuter = (TaskExecuter) ctx.getBean("taskExecuter");
            taskExecuter.setConfigName(param.getFileName());
            taskExecuter.setFile(true);
            Thread thread = new Thread(taskExecuter);
            thread.start();
        } else if (param.isDirectory()) {
            if (param.getOneFileSleepTime().equals("") && param.getLoopSleepTime().equals("")) {
                saveFromConfigDir(param.getDirectoryName());
            } else if (!param.getOneFileSleepTime().equals("") && param.getLoopSleepTime().equals("")) {
                saveFromConfigDir(param.getDirectoryName(), Long.parseLong(param.getOneFileSleepTime()));
            } else if (!param.getOneFileSleepTime().equals("") && !param.getLoopSleepTime().equals("")) {
                saveFromConfigDir(param.getDirectoryName(), Long.parseLong(param.getOneFileSleepTime()),
                                  Long.parseLong(param.getLoopSleepTime()));
            }
        }
    }

    /**
     * (非循环)从指定的目录获取，并给定单个文件执行完后默认的休息时间
     * 
     * @param configDirectory 当前配置文件的目录
     * @throws Exception
     */
    private void saveFromConfigDir(String configDirectory) throws Exception {
        saveFromConfigDir(configDirectory, Constants.One_File_Default_Sleep_Time);
    }

    /**
     * (非循环)从指定的目录获取，并指定单个文件执行完后休息的时间
     * 
     * @param configDirectory 当前配置文件的目录
     * @param oneFileSleepTime 并指定单个文件执行完后休息的时间
     * @throws Exception
     */
    private void saveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        saveFromConfigDir(configDirectory, oneFileSleepTime, -1);
    }

    /**
     * (循环)从指定的目录获取，并指定单个文件执行完后休息的时间，以及循环执行该目录的休息时间
     * 
     * @param configDirectory 当前配置文件的目录
     * @param oneFileSleepTime 并指定单个文件执行完后休息的时间
     * @param loopSleepTime 循环执行该目录的休息时间
     * @throws Exception
     */
    private void saveFromConfigDir(String configDirectory, long oneFileSleepTime, long loopSleepTime) throws Exception {
        boolean loop = true;
        while (loop) {
            doSaveFromConfigDir(configDirectory, oneFileSleepTime);
            if (loopSleepTime == -1) {
                loop = false;
            } else {
                Thread.sleep(loopSleepTime);
            }
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
        log4j.logDebug("Current Directory:" + configDirectory);
        File configFilePath = new File(configDirectory);
        if (!configFilePath.exists()) {
            System.err.println("Config Directory:" + configDirectory + " not exists!");
            return;
        }
        TaskExecuter taskExecuter = (TaskExecuter) ctx.getBean("taskExecuter");
        taskExecuter.setConfigName(configDirectory);
        taskExecuter.setOneFileSleepTime(oneFileSleepTime);
        // 对当前目录进行处理
        Thread thread = new Thread(taskExecuter);
        thread.start();

        // 对子目录进行处理
        File[] files = configFilePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doSaveFromConfigDir(file.getAbsolutePath(), oneFileSleepTime);
            }
        }
    }
}
