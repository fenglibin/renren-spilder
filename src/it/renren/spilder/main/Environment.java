package it.renren.spilder.main;

public class Environment {

    // 是否只获取当前配置文件的第一页
    public static boolean dealOnePage     = false;
    // 用于表示测试该分类下面的配置文件是否能够正常工作，关位进行数据库的操作，并且一个配置文件只检测一条记录
    public static boolean checkConfigFile = false;
    // 否使用代理
    public static boolean isUseProxy      = false;
    // 代理服务器地址
    public static String  proxy           = null;
}
