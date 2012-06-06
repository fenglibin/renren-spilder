package it.renren.spilder.main;

public class Environment {

    // 是否只获取当前配置文件的第一页
    public static boolean dealOnePage                            = false;
    // 用于表示测试该分类下面的配置文件是否能够正常工作，关位进行数据库的操作，并且一个配置文件只检测一条记录
    public static boolean checkConfigFile                        = false;
    // 否使用代理
    public static boolean isUseProxy                             = false;
    // 代理服务器地址
    public static String  proxy                                  = null;
    // 表前缀
    public static String  tablePrefix                            = null;
    // 当处理文章，失败到达一定的数量时，退出控制标识
    public static boolean exitByFailedDealPages                  = Boolean.TRUE;
    // 是处理出错的时候，是否打印同获取到的内容
    public static boolean isOutputHtmlContentWhenErrorHappend    = Boolean.FALSE;
    // 在保存图片时，先建立与当前HTML的TITLE相同的目录，再保存当前图片到此目录中，默认值为false
    public static boolean isSaveImage2CurrentHtmlFileTileNameDir = Boolean.FALSE;
    // 是否图片站，这个用于控制在获取的每片文章都加上头条及特荐
    public static boolean isImageSite                            = Boolean.FALSE;
    // 为采集的图片增加水印的图片地址
    public static String  waterImageLocation                     = null;
    // 存放cook内容的文件
    public static String  cookFile                               = null;
}
