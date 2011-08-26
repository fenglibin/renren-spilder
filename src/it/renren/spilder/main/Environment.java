package it.renren.spilder.main;

public class Environment {

    // 是否只获取当前配置文件的第一页
    public static boolean dealOnePage                         = false;
    // 用于表示测试该分类下面的配置文件是否能够正常工作，关位进行数据库的操作，并且一个配置文件只检测一条记录
    public static boolean checkConfigFile                     = false;
    // 否使用代理
    public static boolean isUseProxy                          = false;
    // 代理服务器地址
    public static String  proxy                               = null;
    // 表前缀
    public static String  tablePrefix                         = null;
    // 当处理文章，失败到达一定的数量时，退出控制标识
    public static boolean exitByFailedDealPages               = Boolean.TRUE;
    // 是处理出错的时候，是否打印同获取到的内容
    public static boolean isOutputHtmlContentWhenErrorHappend = Boolean.FALSE;
}
