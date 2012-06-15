package it.renren.spilder.util.other.php2html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将指定的路径下面的文件，全部生成列表的html返回，并保证正确的连接 .
 * 
 * @author fenglibin 2012-6-15 下午08:45:21
 */
public class GenSingleLinkHtmlFileForAllPhpFiles {

    private static String       basePath          = "D:/test";
    private static String       baseUrl           = "http://www.renren.it/list";
    private static final String FOUR_BLANK_STRING = "&nbsp;&nbsp;&nbsp;&nbsp;";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
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
        listFiles(getBasePath(), "");
    }

    private static StringBuilder listFiles(String path, String blankString) throws IOException {
        StringBuilder sb = new StringBuilder("");
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                String url = getBaseUrl() + file.getAbsolutePath().replace("\\", "/").replace(getBasePath(), "");
                sb.append(blankString).append("<a href=\"").append(url).append("\">").append(file.getName()).append("</a>").append("<br>");
            }
            for (File file : dirList) {
                sb.append(blankString).append(file.getName()).append("<br>");
                sb.append(listFiles(file.getPath(), blankString + FOUR_BLANK_STRING));
            }
        }
        return sb;
    }

    private static List<File> getFileList(File[] files) {
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".phps")) {
                fileList.add(file);
            }
        }
        return fileList;
    }

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

    private static String getBaseUrl() {
        if (System.getProperty("baseUrl") != null) {
            baseUrl = System.getProperty("baseUrl");
        }
        return baseUrl;
    }
}
