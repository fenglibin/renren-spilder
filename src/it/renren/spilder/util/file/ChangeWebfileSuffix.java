package it.renren.spilder.util.file;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;

import java.io.File;
import java.util.List;

/**
 * 类ChangeWebfileSuffix.java的实现描述：改变WEB文件的后缀，并同时替换其页面中的链接为目标后缀。支持对目录及其子目录的操作
 * 
 * @author fenglibin 2011-8-9 下午01:33:02
 */
public class ChangeWebfileSuffix {

    private static final String   charset      = "gb2312";
    private static final String[] fileTypes    = { "asp", "html" };
    private static final String   sourceSuffix = ".asp";
    private static final String   descSuffix   = ".html";

    /**
     * 处理整个目录及其子目录
     * 
     * @param dir
     * @throws Exception
     */
    private static void tranDir(String dir) throws Exception {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                tranDir(file.getAbsolutePath());
            } else {
                if (tranFile(file.getAbsolutePath()) && !file.getAbsolutePath().equals(".html")) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 处理单个文件
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    private static boolean tranFile(String filePath) throws Exception {
        if (!isTranFile(filePath)) {
            return false;
        }

        String tempString = "____";
        // System.out.println("Tran:" + filePath);
        String content = FileUtil.getFileContent(filePath, charset);
        // 从linux拷贝到windows上，文件名的"?"全部被转换为了"_"，这里先对文件中的内容进行一个处理
        content = content.replace(sourceSuffix + "?", sourceSuffix + tempString);
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, charset);
        String descUrl = "";
        for (AHrefElement ahref : childLinks) {
            if (ahref.getHref().indexOf(sourceSuffix) > 0) {
                if (ahref.getHref().indexOf(sourceSuffix + tempString) > 0) {
                    // 取后半部份为文件名
                    descUrl = ahref.getHref().replace(sourceSuffix + tempString, "") + ".html";
                } else {
                    descUrl = ahref.getHref().replace(sourceSuffix, descSuffix);
                }
            }
            content = content.replace(ahref.getHref(), descUrl);
        }
        if (filePath.indexOf(sourceSuffix + "_") > 0 && filePath.indexOf("=") > 0) {
            filePath = filePath.replace(sourceSuffix + "_", "") + ".html";
        } else if (filePath.endsWith(sourceSuffix)) {
            filePath = filePath.replace(sourceSuffix, descSuffix);
        }
        FileUtil.writeFile(filePath, content, charset);
        return true;
    }

    private static boolean isTranFile(String file) {
        boolean is = Boolean.FALSE;
        for (String type : fileTypes) {
            if (file.indexOf(type) > 0) {
                is = Boolean.TRUE;
                break;
            }
        }
        return is;
    }

    /**
     * 处理整个目录及其子目录
     * 
     * @param dir
     * @throws Exception
     */
    private static void makeUrlRelativeDir(String dir) throws Exception {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                makeUrlRelativeDir(file.getAbsolutePath());
            } else {
                makeUrlRelativeFile(file.getAbsolutePath(), file.getParent());
            }
        }
    }

    /**
     * 处理单个文件
     * 
     * @param filePath
     * @param parentPath
     * @return
     * @throws Exception
     */
    private static boolean makeUrlRelativeFile(String filePath, String parentPath) throws Exception {
        if (!isTranFile(filePath)) {
            return false;
        }
        String content = FileUtil.getFileContent(filePath, charset);
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, charset);
        String descUrl = "";
        for (AHrefElement ahref : childLinks) {
            descUrl = getRelativeUrl(parentPath, ahref.getHref());
            content = content.replace(ahref.getHref(), descUrl);
        }
        FileUtil.writeFile(filePath, content, charset);
        return true;
    }

    /**
     * 将url的绝对路径修改为相对路径，并最多尝试5次
     * 
     * @param sourceUrl
     * @return
     */
    private static String getRelativeUrl(String parentPath, String sourceUrl) {
        if (!sourceUrl.startsWith("/") || StringUtil.isNull(sourceUrl)) {
            return sourceUrl;
        }
        String keepedSourceUrl = sourceUrl;
        int tryTimes = 1;
        while (!new File(parentPath + sourceUrl).exists() && tryTimes <= 5) {
            sourceUrl = "/.." + sourceUrl;
            tryTimes++;
        }
        if (tryTimes > 5) {
            System.out.println("link url not found:" + sourceUrl);
            sourceUrl = keepedSourceUrl;
        } else {
            sourceUrl = sourceUrl.substring(1);
        }
        return sourceUrl;
    }

    public static void main(String[] args) throws Exception {
        // String dir = "/home/fenglibin/proc/renren-spilder/www.w3school.com.cn";
        // tranDir(dir);
        // makeUrlRelativeDir(dir);
        String file = "/home/fenglibin/proc/renren-spilder/www.w3school.com.cn/index.html";
        tranFile(file);
        makeUrlRelativeFile(file, "/home/fenglibin/proc/renren-spilder/www.w3school.com.cn");
    }
}
