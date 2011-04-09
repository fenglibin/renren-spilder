package it.renren.spilder.main;

import java.io.File;

public class Constants {

    public static final String WILDCAST_TYPE_NUMBER                 = "1";
    public static final String WILDCAST_TYPE_STRING                 = "2";
    public static final String WILDCAST_STRING                      = "(*)";
    public static final String META_KEYWORDS                        = "keywords";
    public static final String META_DESCRIPTIONS                    = "description";

    public static final String ARTICLE_TU                           = "p";                                            // ����ͼƬ������
    public static final String ARTICLE_TOUTIAO                      = "h";                                            // ͷ��
    public static final String ARTICLE_TUIJIAN                      = "c";                                            // �Ƽ�
    public static final String ARTICLE_HUANDENG                     = "f";                                            // �õ�
    public static final String ARTICLE_TEJIAN                       = "a";                                            // �ؼ�
    public static final String ARTICLE_GUNDONG                      = "s";                                            // ����
    public static final String ARTICLE_JIACU                        = "b";                                            // �Ӵ�

    public static final int    CONTENT_LEAST_LENGTH                 = 100;                                             // ��������ַ������ж�

    public final static int    K                                    = 1024;

    public static final String RenRen_URL                           = "http://www.renren.it";
    // ��ǰӦ�ó�������·��
    public static final String currentPath                          = new File("").getAbsolutePath() + File.separator;
    public static final String USE_AGE                              = "Usage:java -jar renren.it_spilder "
                                                                      + "-f�����ļ���|-d�����ļ�Ŀ¼  "
                                                                      + "[-o�����ļ�ִ�������Ϣ��ʱ��(����Ϊ��λ) "
                                                                      + "-lѭ��ִ��ĳĿ¼��ͣ��ʱ��(�Ժ���Ϊ��λ) "
                                                                      + "-tָ�������ļ�(��task.xml) "
                                                                      + "-p(�Ƿ������Ŀ¼ �е��ļ���ִֻ�е�һҳ)]";

    public static final String CHINESE_SIMPLIFIED                   = "cn";
    public static final String CHINESE_TRADITIONAL                  = "big5";
    public static final String ENGLISH                              = "en";
    public static final int    GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH = 5000;

    public static final String EXECUTE_FILE                         = "FILE";
    public static final String EXECUTE_DIR                          = "DIR";

    public static final long   One_Url_Default_Sleep_Time           = 10000;
    public static final long   One_File_Default_Sleep_Time          = 1500 * 60;
    public static final String SPRING_CONFIG_FILE                   = "beans.xml";
}
