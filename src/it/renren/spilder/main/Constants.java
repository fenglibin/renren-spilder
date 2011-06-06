package it.renren.spilder.main;

import java.io.File;

public class Constants {

    public static final String WILDCAST_TYPE_NUMBER                 = "1";
    public static final String WILDCAST_TYPE_STRING                 = "2";
    public static final String WILDCAST_STRING                      = "(*)";
    public static final String META_KEYWORDS                        = "keywords";
    public static final String META_DESCRIPTIONS                    = "description";
    // 包括图片的文章
    public static final String ARTICLE_TU                           = "p";
    // 头条
    public static final String ARTICLE_TOUTIAO                      = "h";
    // 推荐
    public static final String ARTICLE_TUIJIAN                      = "c";
    // 幻灯
    public static final String ARTICLE_HUANDENG                     = "f";
    // 特荐
    public static final String ARTICLE_TEJIAN                       = "a";
    // 滚动
    public static final String ARTICLE_GUNDONG                      = "s";
    // 加粗
    public static final String ARTICLE_JIACU                        = "b";
    // 文章最短字符长度判断
    public static final int    CONTENT_LEAST_LENGTH                 = 100;

    public final static int    K                                    = 1024;

    public static final String RenRen_URL                           = "http://www.renren.it";
    // 当前应用程序所在路径
    public static final String currentPath                          = new File("").getAbsolutePath() + File.separator;
    // 帮助提示信息
    public static final String USE_AGE                              = "Usage:java -jar renren.it_spilder "
                                                                      + "-f配置文件名|-d配置文件目录  "
                                                                      + "[-o单个文件执行完后休息的时间(毫秒为单位) "
                                                                      + "-l循环执行某目录暂停的时候(以毫秒为单位) "
                                                                      + "-check(用于对配置文件的检测，不需要指定参数) "
                                                                      + "-p(是否对配置目录 中的文件，只执行第一页，不需要指定参数)]";

    public static final String CHINESE_SIMPLIFIED                   = "cn";
    public static final String CHINESE_TRADITIONAL                  = "big5";
    public static final String ENGLISH                              = "en";
    public static final int    GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH = 5000;

    public static final long   One_Url_Default_Sleep_Time           = 10000;
    public static final long   One_File_Default_Sleep_Time          = 1500 * 60;
    public static final String SPRING_CONFIG_FILE                   = "beans.xml";
    // 根据一个配置文件处理时，如果其中处理失败的链接大于等于了当前设置值，就退出该配置文件的处理，因为后面的处理多半也是失败的，如已经处理过了
    public static final int    ONE_CONFIG_FILE_MAX_FAILED_TIMES     = 3;
    public static final String COMMA                                = ",";
}
