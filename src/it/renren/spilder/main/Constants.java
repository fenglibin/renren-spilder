package it.renren.spilder.main;

import java.io.File;

public class Constants {

    public static final String WILDCAST_TYPE_NUMBER                 = "1";
    public static final String WILDCAST_TYPE_STRING                 = "2";
    public static final String WILDCAST_STRING                      = "(*)";
    public static final String META_KEYWORDS                        = "keywords";
    public static final String META_DESCRIPTIONS                    = "description";
    // ����ͼƬ������
    public static final String ARTICLE_TU                           = "p";
    // ͷ��
    public static final String ARTICLE_TOUTIAO                      = "h";
    // �Ƽ�
    public static final String ARTICLE_TUIJIAN                      = "c";
    // �õ�
    public static final String ARTICLE_HUANDENG                     = "f";
    // �ؼ�
    public static final String ARTICLE_TEJIAN                       = "a";
    // ����
    public static final String ARTICLE_GUNDONG                      = "s";
    // �Ӵ�
    public static final String ARTICLE_JIACU                        = "b";
    // ��������ַ������ж�
    public static final int    CONTENT_LEAST_LENGTH                 = 100;

    public final static int    K                                    = 1024;

    public static final String RenRen_URL                           = "http://www.renren.it";
    // ��ǰӦ�ó�������·��
    public static final String currentPath                          = new File("").getAbsolutePath() + File.separator;
    // ������ʾ��Ϣ
    public static final String USE_AGE                              = "Usage:java -jar renren.it_spilder "
                                                                      + "-f�����ļ���|-d�����ļ�Ŀ¼  "
                                                                      + "[-o�����ļ�ִ�������Ϣ��ʱ��(����Ϊ��λ) "
                                                                      + "-lѭ��ִ��ĳĿ¼��ͣ��ʱ��(�Ժ���Ϊ��λ) "
                                                                      + "-check(���ڶ������ļ��ļ�⣬����Ҫָ������) "
                                                                      + "-p(�Ƿ������Ŀ¼ �е��ļ���ִֻ�е�һҳ������Ҫָ������)]";

    public static final String CHINESE_SIMPLIFIED                   = "cn";
    public static final String CHINESE_TRADITIONAL                  = "big5";
    public static final String ENGLISH                              = "en";
    public static final int    GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH = 5000;

    public static final long   One_Url_Default_Sleep_Time           = 10000;
    public static final long   One_File_Default_Sleep_Time          = 1500 * 60;
    public static final String SPRING_CONFIG_FILE                   = "beans.xml";
    // ����һ�������ļ�����ʱ��������д���ʧ�ܵ����Ӵ��ڵ����˵�ǰ����ֵ�����˳��������ļ��Ĵ�����Ϊ����Ĵ�����Ҳ��ʧ�ܵģ����Ѿ��������
    public static final int    ONE_CONFIG_FILE_MAX_FAILED_TIMES     = 3;
    public static final String COMMA                                = ",";
}
