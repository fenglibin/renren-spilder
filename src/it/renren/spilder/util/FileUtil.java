package it.renren.spilder.util;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.log.Log4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class FileUtil {

    private static Log4j       log4j = new Log4j(FileUtil.class.getName());
    private static PrintWriter appendWriter;

    public static synchronized void downloadFileByUrl(String srcUrl, String filePath) {
        downloadFileByUrl(srcUrl, filePath, null);
    }

    public static synchronized void downloadFileByUrl(String srcUrl, String fileSavePath, String newName) {
        org.apache.commons.httpclient.HttpClient httpclient = HttpClientUtil.getHttpClient();
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", "Mozilla/3.0 (compatible; MSIE 6.0; Windows NT 6.1)"));
        httpclient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        GetMethod get = new GetMethod(srcUrl);
        FileOutputStream out = null;
        String fileName = null;
        try {
            fileName = newName == null ? getFileName(srcUrl) : newName;
            File wdFile = new File(fileSavePath + fileName);
            out = new FileOutputStream(wdFile);

            httpclient.executeMethod(get);

            InputStream instream = get.getResponseBodyAsStream();
            int l;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
        } catch (HttpException e) {
            log4j.logError(e);
        } catch (IOException e) {
            log4j.logError(e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log4j.logError(e);
                }
            }
        }
        // log4j.logDebug("保存文件:"+srcUrl+" 到 "+fileSavePath+",文件名为:"+fileName);
    }

    public static void getUrlFile(String srcUrl, String filePath) throws IOException {
        URL url = new URL(srcUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        long urlFileLength = httpURLConnection.getContentLength();
        if (urlFileLength == -1) {// 如果没有取到文件长度，再取
            try {
                urlFileLength = Long.parseLong(httpURLConnection.getHeaderField("Content-Length"));
            } catch (Exception e1) {
                log4j.logError(e1);
            }
        }
        httpURLConnection.setReadTimeout(5000);// 设置超时时间为20秒
        String urlFileName = url.getFile(); // 取得在服务器上的路径及文件名
        urlFileName = GetFileName(urlFileName); // 取得文件名
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        DataOutputStream dos = null;
        FileOutputStream fos = null;
        File wdFile = new File(filePath + urlFileName);
        fos = new FileOutputStream(wdFile);
        dos = new DataOutputStream(fos);
        byte[] bt = new byte[10240];// 缓冲区
        int len = 0;
        while ((len = bis.read(bt)) > 0) {
            dos.write(bt, 0, len);// 写文件
        }
        dos.close();
        fos.close();
        bis.close();
        httpURLConnection.disconnect();
    }

    /**
     * 取得要下载的文件名
     * 
     * @param file 网络文件名及路径
     * @return 文件名及扩展名
     */
    private static String GetFileName(String file) {
        StringTokenizer st = new StringTokenizer(file, "/");
        while (st.hasMoreTokens()) {
            file = st.nextToken();
        }
        return file;
    }

    /* 根据URL获取文件名 */
    public static String getFileName(String imageSrc) {
        String filename = "";
        String[] array = imageSrc.split("\\/");
        filename = array[array.length - 1];
        if (filename.indexOf("?") > 0) {
            filename = filename.split("\\?")[0];
        }
        return filename;
    }

    public static String getFileExtensation(String filename) {
        String name = "";
        name = filename.split("\\.")[1];
        return name;
    }

    public static String getRandomString() {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf((int) (1000 * Math.random()));
    }

    /* 根据当前图片获取其新的地址 */
    public static String getNewFileName(String imageSrc) {
        String fileName = getFileName(imageSrc);
        String ext = getFileExtensation(fileName);
        fileName = getRandomString() + Constants.DOT + ext;
        return fileName;
    }

    /**
     * 获取文件中的内容,并将内容全部以小写的形式返回
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static String getFileContent2LowerCase(String path) throws Exception {
        String content = getFileContent(path);
        if (content == null) {
            return content;
        } else {
            return content.toLowerCase();
        }
    }

    /**
     * 根据输入的编码读取文件
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static String getFileContent(String path) throws Exception {
        File file = new File(path);
        String content = "";
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while ((line = br.readLine()) != null) {
            content += line + "\n";
        }
        br.close();
        fr.close();
        return content;
    }

    /**
     * 根据指定编码读取文件的内容
     * 
     * @param path
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getFileContent(String path, String charset) throws IOException {
        String content = "";
        InputStreamReader read = new InputStreamReader(new FileInputStream(path), charset);
        BufferedReader br = new BufferedReader(read);
        String line = "";
        while ((line = br.readLine()) != null) {
            content += line + "\n";
        }
        br.close();
        read.close();
        return content;
    }

    /**
     * 将读到的文件，一行一行的放到List中
     * 
     * @param path
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static List<String> getFile2List(String path) throws IOException {
        List<String> cList = new ArrayList<String>();
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while ((line = br.readLine()) != null) {
            cList.add(line.trim());
        }
        br.close();
        fr.close();
        return cList;
    }

    /**
     * 将内容输出到文件中
     * 
     * @param filePath
     * @param content
     * @throws Exception
     */
    public static void writeFile(String filePath, String content) throws Exception {
        File file = new File(filePath);
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    /**
     * 根据指定的编码格式，将内容输出到文件中
     * 
     * @param filePath
     * @param content
     * @param charset
     * @throws Exception
     */
    public static void writeFile(String filePath, String content, String charset) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                                                                                    charset)));
        out.write(content);
        out.close();
    }

    /**
     * 以追加的方式输出文件
     * 
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeFileAppend(String filePath, String content) throws IOException {
        if (appendWriter == null) {
            appendWriter = new PrintWriter(new FileWriter(filePath, true), true);
        }
        appendWriter.println();
        appendWriter.write(content);
        appendWriter.flush();
    }

    public static void main(String[] args) {
        // String url = "www.163.com/a/b.jpg?noscript";
        // String filename = getFileName(url);
        // log4j.logDebug(filename);

        downloadFileByUrl("http://img1.51cto.com/attachment/201010/200231826.jpg", "/home/fenglibin/tmp/img/", null);
    }
}
