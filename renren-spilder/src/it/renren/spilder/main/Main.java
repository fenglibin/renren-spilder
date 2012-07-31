package it.renren.spilder.main;

/*
 * �����ļ�ģ�壺 <?xml version="1.0" encoding="gbk"?> <Rules> <MainUrl> <Values desc="���ӵ��б��ַ��Value�е��б��ַ�����Ƕ������Զ��ŷֿ�">
 * <!--Value desc="��ǰ��ҳ���Url��ַ"> <![CDATA[http://blog.csdn.net/zhengyun_ustc/category/30537.aspx?PageNumber=(*)]]>
 * </Value--> <BatValues desc="�������ӣ�֧��ͨ���÷�'(*)'"> <Value> <![CDATA[http://java.dzone.com/frontpage?page=(*)]]> </Value>
 * <WildcastType desc="ͨ���������:1��ʾ���֣�2��ʾa-z����ĸ"> <Value> <![CDATA[1]]> </Value> <Start> <Value> <![CDATA[1]]> </Value>
 * </Start> <End> <Value> <![CDATA[248]]> </Value> </End> </WildcastType> </BatValues> </Values> <DesArticleId>
 * <Value>23</Value> </DesArticleId> <AutoDetect>
 * <TypeMapMakeClass>it.renren.spilder.type.DedecmsTypesMap</TypeMapMakeClass> </AutoDetect> <MainRange desc="���ݷ�Χ">
 * <Start> <Value><![CDATA[</a> </li></ul>]]></Value> </Start> <End> <Value><![CDATA[</p></div></div> <script
 * ]]></Value> </End> </MainRange> <Charset desc="����"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <ImageDescUrl
 * desc="��ԭ�����е�ͼƬ��ַ�滻���û�����ͼƬ�����·��"> <Value> <![CDATA[/uploads/allimg/]]> </Value> </ImageDescUrl> <ImageSaveLocation
 * desc="����ͼƬ��·������ͼƬд����Ӳ����"> <Value> <![CDATA[/home/fenglibin/www/www.renren.it/uploads/allimg/]]> </Value>
 * </ImageSaveLocation> <Recommend desc="����Ƽ�����������ƪ�����Ƽ�һƪ.'0'��ʾ������Ƽ�"> <Value><![CDATA[3]]></Value> </Recommend>
 * <UrlFilter url="urlɸѡ"> <MustInclude desc="url�б���������ַ���"> <Value> <![CDATA[articles]]> </Value> </MustInclude>
 * <MustNotInclude desc="url�в��ܹ��������ַ���"> <Value> <![CDATA[]]> </Value> </MustNotInclude> </UrlFilter> <Translater
 * desc="��������.enָӢ��,cnָ��������,big5ָ��������"> <From desc="ԭ����"> <Value> <![CDATA[en]]> </Value> </From> <To desc="Ŀ������">
 * <Value> <![CDATA[cn]]> </Value> </To> </Translater> <OneUrlSleepTime> <Value>60000</Value> </OneUrlSleepTime>
 * </MainUrl> <Child> <Charset desc="����"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <Title desc="����"> <Start>
 * <Value><![CDATA[<h1>]]></Value> </Start> <End> <Value><![CDATA[</h1>]]></Value> </End> <Replace desc="�滻����">
 * <IsRegularExpression desc="�Ƿ�������ʽ��ֵֻ�ܹ���true��false"> <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From
 * desc="���滻���ⲿ��"> <Value> <![CDATA[]]> </Value> </From> <To desc="Ŀ�겿��"> <Value> <![CDATA[]]> </Value> </To> </Replace>
 * </Title> <Content desc="����"> <Start> <Value><![CDATA[<span class='print-link'></span>]]></Value> </Start> <End>
 * <Value><![CDATA[<div class="fivestar-static-form-item">]]></Value> </End> <Replace desc="�滻����"> <IsRegularExpression
 * desc="�Ƿ�������ʽ"> <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From desc="ԭ����"> <Value> <![CDATA[]]>
 * </Value> </From> <To desc="Ŀ������"> <Value> <![CDATA[]]> </Value> </To> </Replace> </Content> <AddUrl
 * desc="�Ƿ񽫵�ǰ���²ɼ�ҳ�ĵ�ַ�ӵ����ݵ�β����ֵֻ������true�л�false"> <Value> <![CDATA[true]]> </Value> </AddUrl> <KeepFileName
 * desc="�Ƿ���ԭ��ȡ��ҳ���ļ���"> <Value> <![CDATA[true]]> </Value> </KeepFileName> </Child> </Rules>
 */

import it.renren.spilder.main.config.TaskExecuter;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * ϵͳ���࣬ϵͳ����ͨ���������� ��Main.java��ʵ��������TODO ��ʵ������
 * 
 * @author fenglibin 2011-4-10 ����04:04:42
 */
public class Main {

    private static Log4j                          log4j = new Log4j(Main.class.getName());
    private static ConfigurableApplicationContext ctx   = null;

    /**
     * ϵͳ���в��� ��Main.java��ʵ��������TODO ��ʵ������
     * 
     * @author fenglibin 2011-4-10 ����04:02:38
     */
    public class Param {

        // �Ƿ��ļ�
        private boolean isFile           = false;
        // �Ƿ�Ŀ¼
        private boolean isDirectory      = false;
        // ���ļ�������£�ָ���ļ���
        private String  fileName         = "";
        // ��Ŀ¼������£�ָ��Ŀ¼��
        private String  directoryName    = "";
        // ���������ļ�ִ�����ϵͳ��ͣ��ʱ���,�Ժ���Ϊ��λ
        private String  oneFileSleepTime = "";
        // ��������ִ����ɺ��´��ٴ�ִ�еĵȴ�ʱ��,�Ժ���Ϊ��λ
        private String  loopSleepTime    = "";
        // Spring�������ļ�·��
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

        // �����ļ��У��Ƿ�ֻ��ȡ�����ļ���ָ���ĵ�һҳ����
        public void setDealOnePage(boolean dealOnePage) {
            Environment.dealOnePage = dealOnePage;
        }

        // ���ڱ�ʾ���Ը÷�������������ļ��Ƿ��ܹ�������������λ�������ݿ�Ĳ���������һ�������ļ�ֻ���һ����¼
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
     * ������Ĳ������д���
     * 
     * @param args �����ԭ��������
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
            if (value.startsWith("-f")) {// �ļ�
                param.setFile(Boolean.TRUE);
                param.setFileName(value.replace("-f=", "").replace("-f", ""));
            } else if (value.startsWith("-d")) {// Ŀ¼
                param.setDirectory(Boolean.TRUE);
                param.setDirectoryName(value.replace("-d=", "").replace("-d", ""));
            } else if (value.startsWith("-o")) {// �����ļ�ִ�������ͣ��ʱ�䣬�Ժ���Ϊ��λ
                param.setOneFileSleepTime(value.replace("-o=", "").replace("-o", ""));
            } else if (value.startsWith("-l")) {// ѭ��ִ��ĳ�ļ�����ͣ��ʱ���Ժ���Ϊ��λ
                param.setLoopSleepTime(value.replace("-l=", "").replace("-l", ""));
            } else if (value.startsWith("-p")) {// �����ļ��У��Ƿ�ֻ��ȡ�����ļ���ָ���ĵ�һҳ����
                param.setDealOnePage(Boolean.TRUE);
            } else if (value.startsWith("-check")) {// ���ڱ�ʾ���Ը÷�������������ļ��Ƿ��ܹ�������������λ�������ݿ�Ĳ���������һ�������ļ�ֻ���һ����¼
                param.setCheckConfigFile(Boolean.TRUE);
            } else if (value.startsWith("-spring")) {// ָ��SPRING�������ļ�
                param.setSpringConfigFile(value.replace("-spring=", "").replace("-spring", ""));
            } else if (value.startsWith("-proxy")) {// ʹ�ô���
                param.setUseProxyTrue();
                param.setProxyHost(value.replace("-proxy", ""));
            } else if (value.startsWith("-tablePrefix")) {// ��ǰ׺��֧���ڲ��޸����õ�����£���������д������ʱ��ָ����ͬ�ı�ǰ׺
                param.setTablePrefix(value.replace("-tablePrefix=", "").replace("-tablePrefix", ""));
            } else if (value.startsWith("-exitByFailedDealPages")) {// ���������£�ʧ�ܵ���һ��������ʱ���˳����Ʊ�ʶ
                param.setExitByFailedDealPages(value.replace("-exitByFailedDealPages", ""));
            } else if (value.startsWith("-OutputHtmlContentWhenErrorHappend")) {// ���������£�ʧ�ܵ���һ��������ʱ���˳����Ʊ�ʶ
                param.enableOutputHtmlContentWhenErrorHappend();
            } else if (value.startsWith("-SaveImage2CurrentHtmlFileTileNameDir")) {// ����ͼƬʱ�ȸ��ݱ��⽨���ļ���
                param.setSaveImage2CurrentHtmlFileTileNameDir();
            } else if (value.startsWith("-isImageSite")) {// �Ƿ�ͼƬվ��������ڿ����ڻ�ȡ��ÿƬ���¶�����ͷ�����ؼ�
                param.setImageSite();
            } else if (value.startsWith("-waterImageLocation")) {// Ϊ�ɼ���ͼƬ����ˮӡ��ˮӡͼƬ��ַ
                param.setWaterImageLocation(value.replace("-waterImageLocation=", "").replace("-waterImageLocation", ""));
            } else if (value.startsWith("-cookFile")) {// ����cookie��ŵ��ļ�����Ϊcookie���ݱȽ϶࣬�Ͳ������������ˣ�ֱ�ӷŵ��ļ��ļ��ж�ȡ
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
            // �ļ�����
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
            // �ļ��в���
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
     * (��ѭ��)��ָ����Ŀ¼��ȡ�������������ļ�ִ�����Ĭ�ϵ���Ϣʱ��
     * 
     * @param configDirectory ��ǰ�����ļ���Ŀ¼
     * @throws Exception
     */
    private void saveFromConfigDir(String configDirectory) throws Exception {
        saveFromConfigDir(configDirectory, Constants.One_File_Default_Sleep_Time);
    }

    /**
     * (��ѭ��)��ָ����Ŀ¼��ȡ����ָ�������ļ�ִ�������Ϣ��ʱ��
     * 
     * @param configDirectory ��ǰ�����ļ���Ŀ¼
     * @param oneFileSleepTime ��ָ�������ļ�ִ�������Ϣ��ʱ��
     * @throws Exception
     */
    private void saveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        saveFromConfigDir(configDirectory, oneFileSleepTime, -1);
    }

    /**
     * (ѭ��)��ָ����Ŀ¼��ȡ����ָ�������ļ�ִ�������Ϣ��ʱ�䣬�Լ�ѭ��ִ�и�Ŀ¼����Ϣʱ��
     * 
     * @param configDirectory ��ǰ�����ļ���Ŀ¼
     * @param oneFileSleepTime ��ָ�������ļ�ִ�������Ϣ��ʱ��
     * @param loopSleepTime ѭ��ִ�и�Ŀ¼����Ϣʱ��
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
     * ִ��ĳ��Ŀ¼�����е������ļ�����ָ����ÿ�������ļ�ִ�������ͣ��ʱ��
     * 
     * @param configDirectory ��ǰ�����ļ�����Ŀ¼
     * @param oneFileSleepTime ÿ�������ļ�ִ�������ͣ��ʱ��
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
        // �Ե�ǰĿ¼���д���
        Thread thread = new Thread(taskExecuter);
        thread.start();

        // ����Ŀ¼���д���
        File[] files = configFilePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doSaveFromConfigDir(file.getAbsolutePath(), oneFileSleepTime);
            }
        }
    }
}
