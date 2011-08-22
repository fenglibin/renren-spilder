package it.renren.spilder.util.file;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.FontUtil;

import java.io.File;

/**
 * 类TranDir.java的实现描述：将指定文件夹下面的文件内容，全部翻译成其它语言，如简体翻译为繁体
 * 
 * @author fenglibin 2011-8-9 下午01:31:43
 */
public class TranDir {

    private static String         charset   = "utf-8";
    private static final String[] fileTypes = { "php", "html", "txt", "htm" };
    private static final int      JIAN_FAN  = 0;                              // 值为1表示简体转为繁体，值为0表示繁体转换为简体

    private static void tranDir(String dir) throws Exception {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                tranDir(file.getAbsolutePath());
            } else {
                tranFile(file.getAbsolutePath());
            }
        }
    }

    private static void tranFile(String file) throws Exception {
        if (!isTranFile(file)) {
            return;
        }
        System.out.println("Tran:" + file);
        String content = FileUtil.getFileContent(file, charset);
        if (JIAN_FAN == 1) {
            content = FontUtil.jian2fan(content);
        } else if (JIAN_FAN == 0) {
            content = FontUtil.fan2jian(content);
        }
        FileUtil.writeFile(file, content, charset);
    }

    private static boolean isTranFile(String file) {
        boolean is = Boolean.FALSE;
        for (String type : fileTypes) {
            if (file.endsWith(type)) {
                is = Boolean.TRUE;
                break;
            }
        }
        return is;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String dir = "E:\\work\\mywork\\www.w3school.com.cn";
        charset = "gbk";
        tranDir(dir);
    }

}
