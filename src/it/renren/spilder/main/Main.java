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
 * <MustNotInclude desc="url�в��ܹ��������ַ���"> <Value> <![CDATA[]]> </Value> </MustNotInclude> </UrlFilter> <Database
 * desc="���ݿ�����"> <JdbcDriverClass> <Value> <![CDATA[com.mysql.jdbc.Driver]]> </Value> </JdbcDriverClass> <LinkString>
 * <Value> <![CDATA[jdbc:mysql://184.82.12.132:3306/renren?characterEncoding=gbk]]> </Value> </LinkString> <Username>
 * <Value> <![CDATA[fenglibin]]> </Value> </Username> <Password> <Value> <![CDATA[fenglibin]]> </Value> </Password>
 * </Database> <Translater desc="��������.enָӢ��,cnָ��������,big5ָ��������"> <From desc="ԭ����"> <Value> <![CDATA[en]]> </Value>
 * </From> <To desc="Ŀ������"> <Value> <![CDATA[cn]]> </Value> </To> </Translater> <OneUrlSleepTime> <Value>60000</Value>
 * </OneUrlSleepTime> </MainUrl> <Child> <Charset desc="����"> <Value> <![CDATA[utf-8]]> </Value> </Charset> <Title
 * desc="����"> <Start> <Value><![CDATA[<h1>]]></Value> </Start> <End> <Value><![CDATA[</h1>]]></Value> </End> <Replace
 * desc="�滻����"> <IsRegularExpression desc="�Ƿ�������ʽ��ֵֻ�ܹ���true��false"> <Value> <![CDATA[false]]> </Value>
 * </IsRegularExpression> <From desc="���滻���ⲿ��"> <Value> <![CDATA[]]> </Value> </From> <To desc="Ŀ�겿��"> <Value>
 * <![CDATA[]]> </Value> </To> </Replace> </Title> <Content desc="����"> <Start> <Value><![CDATA[<span
 * class='print-link'></span>]]></Value> </Start> <End> <Value><![CDATA[<div
 * class="fivestar-static-form-item">]]></Value> </End> <Replace desc="�滻����"> <IsRegularExpression desc="�Ƿ�������ʽ">
 * <Value> <![CDATA[false]]> </Value> </IsRegularExpression> <From desc="ԭ����"> <Value> <![CDATA[]]> </Value> </From> <To
 * desc="Ŀ������"> <Value> <![CDATA[]]> </Value> </To> </Replace> </Content> <AddUrl
 * desc="�Ƿ񽫵�ǰ���²ɼ�ҳ�ĵ�ַ�ӵ����ݵ�β����ֵֻ������true�л�false"> <Value> <![CDATA[true]]> </Value> </AddUrl> <KeepFileName
 * desc="�Ƿ���ԭ��ȡ��ҳ���ļ���"> <Value> <![CDATA[true]]> </Value> </KeepFileName> </Child> </Rules>
 */

import it.renren.spilder.util.log.Log4j;

import java.io.File;

import org.jdom.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {

    private static Document taskDoc;                                           // �������õ�XML�ĵ�
    private static String   taskFileName     = "task.xml";                     // ִ�����������
    private static String   dirOrFile        = Constants.EXECUTE_FILE;         // �ļ�����Ŀ¼
    private static String   dirOrFileName    = "";                             // ��ǰִ�е��������ļ�����Ŀ¼������
    private static String   oneFileSleepTime = "";                             // ִ��һ���ļ����ж�������ļ�ʱ�����������ļ�ִ�������Ϣ��ʱ��
    private static String   loopSleepTime    = "";                             // ѭ��ִ��ĳ�ļ�����ͣ��ʱ���Ժ���Ϊ��λ
    public static boolean   onePage          = false;                          // �Ƿ�ֻ��ȡ��ǰ�����ļ��ĵ�һҳ
    private static Log4j    log4j            = new Log4j(Main.class.getName());

    /**
     * ������Ĳ������д���
     * 
     * @param args �����ԭ��������
     */
    private static void initParameters(String[] args) {
        String value = "";
        for (int i = 0; i < args.length; i++) {
            value = args[i];
            if (value.startsWith("-f")) {// �ļ�
                dirOrFile = Constants.EXECUTE_FILE;
                dirOrFileName = value.replace("-f", "");
            } else if (value.startsWith("-d")) {// Ŀ¼
                dirOrFile = Constants.EXECUTE_DIR;
                dirOrFileName = value.replace("-d", "");
            } else if (value.startsWith("-o")) {// �����ļ�ִ�������ͣ��ʱ�䣬�Ժ���Ϊ��λ
                oneFileSleepTime = value.replace("-o", "");
            } else if (value.startsWith("-l")) {// ѭ��ִ��ĳ�ļ�����ͣ��ʱ���Ժ���Ϊ��λ
                loopSleepTime = value.replace("-l", "");
            } else if (value.startsWith("-t")) {// ָ�������ļ�
                taskFileName = value.replace("-t", "");
            } else if (value.startsWith("-p")) {// �����ļ��У��Ƿ�ֻ��ȡ�����ļ���ָ���ĵ�һҳ����
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
     * (��ѭ��)��ָ����Ŀ¼��ȡ�������������ļ�ִ�����Ĭ�ϵ���Ϣʱ��
     * 
     * @param configDirectory ��ǰ�����ļ���Ŀ¼
     * @throws Exception
     */
    private static void saveFromConfigDir(String configDirectory) throws Exception {
        saveFromConfigDir(configDirectory, Constants.One_File_Default_Sleep_Time);
    }

    /**
     * (��ѭ��)��ָ����Ŀ¼��ȡ����ָ�������ļ�ִ�������Ϣ��ʱ��
     * 
     * @param configDirectory ��ǰ�����ļ���Ŀ¼
     * @param oneFileSleepTime ��ָ�������ļ�ִ�������Ϣ��ʱ��
     * @throws Exception
     */
    private static void saveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
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
     * ִ��ĳ��Ŀ¼�����е������ļ�����ָ����ÿ�������ļ�ִ�������ͣ��ʱ��
     * 
     * @param configDirectory ��ǰ�����ļ�����Ŀ¼
     * @param oneFileSleepTime ÿ�������ļ�ִ�������ͣ��ʱ��
     * @throws Exception
     */
    private static void doSaveFromConfigDir(String configDirectory, long oneFileSleepTime) throws Exception {
        log4j.logDebug("Current Directory:" + configDirectory);
        File configFilePath = new File(configDirectory);
        if (!configFilePath.exists()) {
            System.err.println("Config Directory:" + configDirectory + " not exists!");
            return;
        }
        // �Ե�ǰĿ¼���д���
        Thread thread = new Thread(new TaskExecuter(configDirectory, oneFileSleepTime));
        thread.start();

        // ����Ŀ¼���д���
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
