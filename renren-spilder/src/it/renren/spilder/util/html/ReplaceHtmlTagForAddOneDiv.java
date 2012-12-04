package it.renren.spilder.util.html;

import it.renren.spilder.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 替换指定目录下面及其子目录下面文件中的图片地址
 * 
 * @author fenglibin 2012-6-15 下午08:45:21
 */
public class ReplaceHtmlTagForAddOneDiv {

    private static String  basePath = "/home/fenglibin/www/www.stackdoc.com/a";
    private static String  charset  = "utf-8";
    private static boolean debug    = false;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                String[] keyValue = arg.split("=");
                String value = "";
                if (keyValue.length == 1) {
                    value = "";
                } else {
                    value = keyValue[1];
                }
                System.setProperty(keyValue[0], value);
            }
        }
        if (System.getProperty("debug") != null && "true".equals(System.getProperty("debug"))) {
            debug = true;
        }
        replace(getBasePath());
    }

    private static void replace(String path) throws Exception {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                if (debug) {
                    System.out.println("file:" + file.getAbsolutePath());
                }
                String content = FileUtil.getFileContent(file.getAbsolutePath(), getCharset());
                if (content.indexOf("<div id=\"outad\"></div>") < 0 && content.indexOf("<!-- //top -->") > 0) {
                    content = content.replace("<!-- //top -->", "<!-- //top --><div id=\"outad\"></div>");
                    FileUtil.writeFile(file.getAbsolutePath(), content, getCharset());
                }
            }
            fileList.clear();
            fileList = null;
            for (File file : dirList) {
                if (debug) {
                    System.out.println("path:" + file.getAbsolutePath());
                }
                replace(file.getPath());
            }
        }
    }

    /**
     * 获取文件的list列表，不包括文件夹
     * 
     * @param files
     * @return
     */
    private static List<File> getFileList(File[] files) {
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".html") && !file.getName().startsWith("list")) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 获取文件夹的list列表，不包括文件夹
     * 
     * @param files
     * @return
     */
    private static List<File> getDirList(File[] files) {
        List<File> dirList = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().endsWith(".svn")) {
                dirList.add(file);
            }
        }
        return dirList;
    }

    private static String getBasePath() {
        if (System.getProperty("basePath") != null) {
            basePath = System.getProperty("basePath");
        }
        return basePath;
    }

    private static String getCharset() {
        if (System.getProperty("charset") != null) {
            charset = System.getProperty("charset");
        }
        return charset;
    }
}
