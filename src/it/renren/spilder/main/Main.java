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
 * <MustNotInclude desc="url中不能够包括的字符串"> <Value> <![CDATA[]]> </Value> </MustNotInclude> </UrlFilter> <Database
 * desc="数据库配置"> <JdbcDriverClass> <Value> <![CDATA[com.mysql.jdbc.Driver]]> </Value> </JdbcDriverClass> <LinkString>
 * <Value> <![CDATA[jdbc:mysql://184.82.12.132:3306/renren?characterEncoding=gbk]]> </Value> </LinkString> <Username>
 * <Value> <![CDATA[fenglibin]]> </Value> </Username> <Password> <Value> <![CDATA[fenglibin]]> </Value> </Password>
 * </Database> <Translater desc="翻译配置.en指英语,cn指简体中文,big5指繁体中文"> <From desc="原语言"> <Value> <![CDATA[en]]> </Value>
 * </From> <To desc="目标语言"> <Value> <![CDATA[cn]]> </Value> </To> </Translater> <OneUrlSleepTime> <Value>60000</Value>
 * </OneUrlSleepTime> </MainUrl> <Child> <Charset desc="编码"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <Title
 * desc="标题"> <Start> <Value><![CDATA[<h1>]]></Value> </Start> <End> <Value><![CDATA[</h1>]]></Value> </End> <Replace
 * desc="替换标题"> <IsRegularExpression desc="是否正则表达式，值只能够是true或false"> <Value> <![CDATA[false]]> </Value>
 * </IsRegularExpression> <From desc="待替换标题部分"> <Value> <![CDATA[]]> </Value> </From> <To desc="目标部份"> <Value>
 * <![CDATA[]]> </Value> </To> </Replace> </Title> <Content desc="内容"> <Start> <Value><![CDATA[<span
 * class='print-link'></span>]]></Value> </Start> <End> <Value><![CDATA[<div
 * class="fivestar-static-form-item">]]></Value> </End> <Replace desc="替换内容"> <IsRegularExpression desc="是否正则表达式">
 * <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From desc="原内容"> <Value> <![CDATA[]]> </Value> </From> <To
 * desc="目标内容"> <Value> <![CDATA[]]> </Value> </To> </Replace> </Content> <AddUrl
 * desc="是否将当前文章采集页的地址加到内容的尾部，值只可以是true中或false"> <Value> <![CDATA[true]]> </Value> </AddUrl> <KeepFileName
 * desc="是否保留原获取网页的文件名"> <Value> <![CDATA[true]]> </Value> </KeepFileName> </Child> </Rules>
 */

import it.renren.spilder.util.log.Log4j;

import java.io.File;

import org.jdom.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {

    private static Document taskDoc;                                           // 任务配置的XML文档
    private static String   taskFileName     = "task.xml";                     // 执行任务的配置
    private static String   dirOrFile        = Constants.EXECUTE_FILE;         // 文件还是目录
    private static String   dirOrFileName    = "";                             // 当前执行的配置是文件或是目录的名称
    private static String   oneFileSleepTime = "";                             // 执行一个文件夹中多个配置文件时，单个配置文件执行完后休息的时间
    private static String   loopSleepTime    = "";                             // 循环执行某文件，暂停的时候，以毫秒为单位
    public static boolean   onePage          = false;                          // 是否只获取当前配置文件的第一页
    private static Log4j    log4j            = new Log4j(Main.class.getName());

    /**
     * 对输入的参数进行处理
     * 
     * @param args 传入的原参数数组
     */
    private static void initParameters(String[] args) {
        String value = "";
        for (int i = 0; i < args.length; i++) {
            value = args[i];
            if (value.startsWith("-f")) {// 文件
                dirOrFile = Constants.EXECUTE_FILE;
                dirOrFileName = value.replace("-f", "");
            } else if (value.startsWith("-d")) {// 目录
                dirOrFile = Constants.EXECUTE_DIR;
                dirOrFileName = value.replace("-d", "");
            } else if (value.startsWith("-o")) {// 单个文件执行完后，暂停的时间，以毫秒为单位
                oneFileSleepTime = value.replace("-o", "");
            } else if (value.startsWith("-l")) {// 循环执行某文件，暂停的时候，以毫秒为单位
                loopSleepTime = value.replace("-l", "");
            } else if (value.startsWith("-t")) {// 指定任务文件
                taskFileName = value.replace("-t", "");
            } else if (value.startsWith("-p")) {// 单个文件中，是否只获取配置文件中指定的第一页内容
                onePage = true;
            }
        }
    }

    public static Document getTaskDoc() {
        return taskDoc;
    }

    public static void main(String[] args) {
        log4j.logDebug("log4jdebuug");
        initParameters(args);
        if (args.length == 0) {
            args = new String[1];
            dirOrFile = Constants.EXECUTE_FILE;
            dirOrFileName = "Z:/proc/test/renren-spilder/config/blog.www.eryi.org/rule_blog_eryi.org-zblog.xml";
            dirOrFileName = "E:/work/mywork/renren-spilder/config/javaeye.com/rule_javaeye_blog_c_C.xml";
            dirOrFileName = "/home/fenglibin/proc/renren-spilder/config/javaeye.com/rule_javaeye_blog_c_C.xml";
        }
        if (args.length < 1) {
            System.err.println(Constants.USE_AGE);
            log4j.logDebug(Constants.USE_AGE);
            System.exit(0);
        }
        final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                       new String[] { Constants.SPRING_CONFIG_FILE });
        try {
            if (args.length >= 1) {
                String value = args[0];
                if (value != null && value.indexOf("help") >= 0) {
                    System.err.println(Constants.USE_AGE);
                    System.exit(0);
                }
                if (dirOrFile.equals(Constants.EXECUTE_FILE)) {
                    if (!(new File(dirOrFileName).exists())) {
                        System.err.println("Config File:" + dirOrFileName + " not exists!");
                        System.exit(0);
                    }
                    Thread thread = new Thread(new TaskExecuter(dirOrFileName, true));
                    thread.start();
                } else if (dirOrFile.equals(Constants.EXECUTE_DIR)) {
                    if (oneFileSleepTime.equals("") && loopSleepTime.equals("")) {
                        saveFromConfigDir(dirOrFileName);
                    } else if (!oneFileSleepTime.equals("") && loopSleepTime.equals("")) {
                        saveFromConfigDir(dirOrFileName, Long.parseLong(oneFileSleepTime));
                    } else if (!oneFileSleepTime.equals("") && !loopSleepTime.equals("")) {
                        saveFromConfigDir(dirOrFileName, Long.parseLong(oneFileSleepTime),
                                          Long.parseLong(loopSleepTime));
                    }
                }
            }
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

    /**
     * (非循环)从指定的目录获取，并给定单个文件执行完后默认的休息时间
     * 
     * @param configDirectory 当前配置文件的目录
     * @throws Exception
     */
    private static void saveFromConfigDir(String configDirectory) throws Exception {
        saveFromConfigDir(configDirectory, Constants.One_File_Default_Sleep_Time);
    }

    /**
     * (非循环)从指定的目录获取，并指定单个文件执行完后休息的时间
     * 
     * @param configDirectory 当前配置文件的目录
     * @param oneFileSleepTime 并指定单个文件执行完后休息的时间
     * @throws Exception
     */
    private static void saveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
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
    private static void saveFromConfigDir(String configDirectory, long oneFileSleepTime, long loopSleepTime)
                                                                                                            throws Exception {
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
    private static void doSaveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        log4j.logDebug("Current Directory:" + configDirectory);
        File configFilePath = new File(configDirectory);
        if (!configFilePath.exists()) {
            System.err.println("Config Directory:" + configDirectory + " not exists!");
            return;
        }
        // 对当前目录进行处理
        Thread thread = new Thread(new TaskExecuter(configDirectory, oneFileSleepTime));
        thread.start();

        // 对子目录进行处理
        File[] files = configFilePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doSaveFromConfigDir(file.getAbsolutePath(), oneFileSleepTime);
            }
        }
    }

    public static String getTaskFileName() {
        return taskFileName;
    }

    public static boolean isOnePage() {
        return onePage;
    }

    public static void setTaskDoc(Document taskDoc) {
        Main.taskDoc = taskDoc;
    }
}
