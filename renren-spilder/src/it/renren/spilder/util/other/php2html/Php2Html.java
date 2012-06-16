package it.renren.spilder.util.other.php2html;

import it.renren.spilder.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * ��PHPԴ�ļ�����Ϊ��PHP�﷨������HTML�ļ���ʵ��ԭ�� <br>
 * 1�������е�.php�ļ����޸�Ϊ.phps�ļ�������apache����Ĭ�ϴ����Ը�������ʽ���ص�ǰ�ļ���Դ�ļ���<br>
 * 2�������е�.phps�ļ�����ȡ�����أ���дΪ.php.html�ļ�����a.phps������a.php.html�����ʱ��д����ʱ�򣬻ᱣ֤�����ļ���·�������䡣<br>
 * ������Ҫapache��֧�֣� ��Php2Html.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2012-6-15 ����08:36:40
 */
public class Php2Html {

    private static int          fileNum      = 0;
    private static final String basePath     = "E:/bruce/soft/xampp/htdocs/dede";
    private static final String baseURL      = "http://localhost/dede";
    private static final String outPath      = "E:/bruce/temp";
    private static final String htmlHead     = "<html><head><title>test</title></head><body>";
    private static final String htmlEnd      = "</body></html>";
    private static final String htmlPageFile = "result.html";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        long start = System.currentTimeMillis();
        php2phps(basePath);
        long end = System.currentTimeMillis();
        System.out.println("Cost Time:" + (end - start));
        String blankString = "";
        start = System.currentTimeMillis();
        String result = file2HTMLPageList(basePath, blankString);
        end = System.currentTimeMillis();
        System.out.println("fileNum:" + fileNum);
        System.out.println("Cost Time:" + (end - start));
        start = System.currentTimeMillis();
        end = System.currentTimeMillis();
        System.out.println("fileNum:" + fileNum);
        System.out.println("Cost Time:" + (end - start));
        phps2html(basePath);
        try {
            write2File(result, htmlPageFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * rename .php and .inc files to .phps in the give path, deal including the chind path
     * 
     * @param path
     * @param blankString
     * @return
     */
    private static void php2phps(String path) {
        FileUtil.renameFilesInDir(path, ".php", ".php.phps", Boolean.TRUE);
        FileUtil.renameFilesInDir(path, ".inc", ".inc.phps", Boolean.TRUE);
    }

    private static void phps2html(String path) {
        FileUtil.renameFilesInDir(path, ".phps", ".html", Boolean.TRUE);
    }

    private static String file2HTMLPageList(String path, String blankString) throws IOException {
        StringBuilder result = listFiles(path, blankString);
        result.insert(0, htmlHead).append(htmlEnd);
        return result.toString();
    }

    private static StringBuilder listFiles(String path, String blankString) throws IOException {
        StringBuilder sb = new StringBuilder("");
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                String tPath = file.getAbsolutePath();
                tPath = tPath.replace("\\", "/");
                tPath = tPath.replace(basePath, "");
                tPath = baseURL + tPath;
                write(tPath);
                sb.append(blankString).append("<a href=\"").append(tPath).append("\">").append(file.getName()).append("</a>").append("<br>");
                fileNum++;
            }
            for (File file : dirList) {
                sb.append(blankString).append(file.getName()).append("<br>");
                sb.append(listFiles(file.getPath(), blankString + "&nbsp;&nbsp;"));
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

    /**
     * read content from given url
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private static String readUrlContents(String url) throws IOException {
        BufferedReader in = null;
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("gbk")));
        StringBuilder sb = new StringBuilder("");
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        return sb.toString();
    }

    private static void write(String url) throws IOException {
        String path = url.replace(baseURL, "");
        String content = readUrlContents(url);
        content = htmlHead + content + htmlEnd;
        String outFile = outPath + path;
        makeDir(outFile);
        write2File(content, outFile);
    }

    private static void makeDir(String path) {
        path = path.replace("\\", "/");
        String[] pathArray = path.split("/");
        path = path.replace(pathArray[pathArray.length - 1], "");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void write2File(String content, String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(content);
        fw.flush();
        fw.close();
    }
}
