package weiboautoman.timer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static Logger log4j = LoggerFactory.getLogger(FileUtil.class);

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

    /**
     * 从网络获取文件，并保存到本地，不重新命名
     * 
     * @param srcUrl 文件的url
     * @param filePath 保存文件的路径，此路径包括文件名
     */
    public static synchronized void downloadFileByUrl(String srcUrl, String filePath) {
        downloadFileByUrl(srcUrl, filePath, null);
    }

    /**
     * 从网络获取文件，并保存到本地，如果指定了newName，那以文件以newName保存；如果传入的newName为空，则保存为原文件名
     * 
     * @param srcUrl 文件的url
     * @param fileSavePath 保存文件的路径
     * @param newName 新文件名
     */
    public static synchronized void downloadFileByUrl(String srcUrl, String fileSavePath, String newName) {
        org.apache.commons.httpclient.HttpClient httpclient = HttpClientUtil.getHttpClient();
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", "Mozilla/3.0 (compatible; MSIE 6.0; Windows NT 6.1)"));
        httpclient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        GetMethod get = new GetMethod(srcUrl);
        FileOutputStream out = null;
        String fileName = null;
        try {
            if (!StringUtil.isNull(newName)) {
                fileName = getFileName(srcUrl);
                fileSavePath = fileSavePath.replace(fileName, "");
                fileSavePath += newName;
            }
            File wdFile = new File(fileSavePath);
            out = new FileOutputStream(wdFile);

            httpclient.executeMethod(get);

            InputStream instream = get.getResponseBodyAsStream();
            int l;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
        } catch (HttpException e) {
            if (log4j.isErrorEnabled()) {
                log4j.error("get image from internet cause HttpException:", e);
            }
        } catch (IOException e) {
            if (log4j.isErrorEnabled()) {
                log4j.error("save image file cause IOException:", e);
            }
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    if (log4j.isErrorEnabled()) {
                        log4j.error("close save image file cause IOException:", e);
                    }
                }

            }
        }
    }

    /**
     * 根据输入的编码读取文件
     * 
     * @param path
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String getFileContent(String path) throws IOException {
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
     * 返回文件的扩展名，如果没有扩展名的，则返回空字符串""
     * 
     * @param filename
     * @return
     */
    public static String getFileExtensation(String filename) {
        String extName = "";
        String[] nameArray = filename.split("\\.");
        if (nameArray.length <= 1) {
            return extName;
        }
        extName = nameArray[nameArray.length - 1];
        return extName;
    }

    /**
     * 根据传入的文件名，确定是否常用的图片文件：jpg、png、gif、bmp，是则返回true，否则返回false
     * 
     * @param fileName
     * @return
     */
    public static boolean isImageUsualFile(String fileName) {
        boolean is = Boolean.FALSE;
        String extName = getFileExtensation(fileName);
        if (!StringUtil.isNull(extName)) {
            extName = extName.toLowerCase();
            if (extName.equals("jpg") || extName.equals("png") || extName.equals("gif") || extName.equals("bmp")) {
                is = Boolean.TRUE;
            }
        }
        return is;

    }
}
