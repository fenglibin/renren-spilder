package it.renren.spilder.util.html;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将指定的路径下面的文件，全部生成列表的html返回，并保证正确的连接 .并且会根据指定或者默认的pageSize的值进行分页
 * 
 * @author fenglibin 2012-6-15 下午08:45:21
 */
public class GenHtmlListPages {

    private static final String  LIST_PAGE_HTML_MODEL = "/it/renren/spilder/util/html/listPageModel.html";
    private static String        basePath             = "D:/test";
    private static String        baseUrl              = "http://www.renren.it/list";
    private static String        charset              = "GBK";
    private static final String  FOUR_BLANK_STRING    = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private static StringBuilder htmlString           = new StringBuilder("");
    private static int           pageSize             = 20;
    private static int           currentPage          = 1;
    private static int           currentSize          = 0;
    private static String        modelContent;
    private static String        listOutDir           = "D:/testout";

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
        modelContent = FileUtil.readFromClassPath(LIST_PAGE_HTML_MODEL, "UTF-8");
        genHtmlList(getBasePath());
        checkAndMakeHtmlFile(true);
    }

    private static void genHtmlList(String path) throws IOException {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                currentSize++;
                String url = getBaseUrl() + file.getAbsolutePath().replace("\\", "/").replace(getBasePath(), "");
                htmlString.append("<a href=\"").append(url).append("\">").append(getTitle(file)).append("</a>").append("<br>");
                checkAndMakeHtmlFile();
            }
            for (File file : dirList) {
                genHtmlList(file.getPath());
            }
        }
    }

    private static String getTitle(File htmlFile) throws IOException {
        String html = FileUtil.getFileContent(htmlFile, getCharset());
        String title = "";
        title = StringUtil.subString(html, "<title>", "_");
        return title;
    }

    private static void checkAndMakeHtmlFile() throws IOException {
        checkAndMakeHtmlFile(false);
    }

    private static void checkAndMakeHtmlFile(boolean finalCheck) throws IOException {
        if (finalCheck) {
            if (currentSize < getPageSize() && currentPage > 0) {
                genHtml(true);
            }
        } else {
            if (currentSize == getPageSize()) {
                genHtml(false);
                currentSize = 0;
                htmlString.delete(0, htmlString.length());
                currentPage++;
            }
        }
    }

    private static void genHtml(boolean finalCheck) throws IOException {
        htmlString.append(getPageNav(finalCheck));
        FileUtil.write2File(modelContent.replace("${content}", htmlString.toString()), getListOutDir() + "/list_"
                                                                                       + currentPage + ".html");
    }

    /**
     * 获取上页下页
     * 
     * @return
     */
    private static String getPageNav(boolean finalCheck) {
        StringBuilder pageNav = new StringBuilder("<br>");
        if (currentPage > 1) {
            pageNav.append("<a href=\"list_" + (currentPage - 1) + ".html\">Pre</a>");
            pageNav.append(FOUR_BLANK_STRING);
        }
        if (!finalCheck) {
            pageNav.append("<a href=\"list_" + (currentPage + 1) + ".html\">Next</a>");
        }
        return pageNav.toString();
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
            if (file.isFile() && file.getName().endsWith(".html")) {
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

    private static String getBaseUrl() {
        if (System.getProperty("baseUrl") != null) {
            baseUrl = System.getProperty("baseUrl");
        }
        return baseUrl;
    }

    private static String getCharset() {
        if (System.getProperty("charset") != null) {
            charset = System.getProperty("charset");
        }
        return charset;
    }

    private static String getListOutDir() {
        if (System.getProperty("listOutDir") != null) {
            listOutDir = System.getProperty("listOutDir");
        }
        return listOutDir;
    }

    private static int getPageSize() {
        if (System.getProperty("pageSize") != null) {
            pageSize = Integer.parseInt(System.getProperty("pageSize"));
        }
        return pageSize;
    }
}
