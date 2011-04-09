package it.renren.spilder.util;

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
import java.util.StringTokenizer;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class FileUtil {

    private static Log4j log4j = new Log4j(FileUtil.class.getName());

    public static synchronized void downloadFileByUrl(String srcUrl, String filePath) {
        downloadFileByUrl(srcUrl, filePath, null);
    }

    /**
     * ����ָ��URL��Դ�ļ���ָ��·����
     * 
     * @param srcUrl Ҫ�����ļ��ľ���·��url
     * @param fileSavePath �ļ�Ҫ�����·��
     */
    public static synchronized void downloadFileByUrl(String srcUrl, String fileSavePath, String newName) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(srcUrl);
        HttpResponse response;
        FileOutputStream out = null;
        String fileName = null;
        try {
            fileName = newName == null ? getFileName(srcUrl) : newName;
            File wdFile = new File(fileSavePath + fileName);
            out = new FileOutputStream(wdFile);
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int l;
                byte[] tmp = new byte[2048];
                while ((l = instream.read(tmp)) != -1) {
                    out.write(tmp, 0, l);
                }
            }
        } catch (ClientProtocolException e) {
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
        // log4j.logDebug("�����ļ�:"+srcUrl+" �� "+fileSavePath+",�ļ���Ϊ:"+fileName);
    }

    public static synchronized void downloadFileByUrl2(String srcUrl, String fileSavePath, String newName) {
        org.apache.commons.httpclient.HttpClient httpclient = HttpClientUtil.getHttpClient();
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
        } catch (ClientProtocolException e) {
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
        // log4j.logDebug("�����ļ�:"+srcUrl+" �� "+fileSavePath+",�ļ���Ϊ:"+fileName);
    }

    public static void getFile(String srcUrl, String filePath) throws IOException {
        URL url = new URL(srcUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        long urlFileLength = httpURLConnection.getContentLength();
        if (urlFileLength == -1) {// ���û��ȡ���ļ����ȣ���ȡ
            try {
                urlFileLength = Long.parseLong(httpURLConnection.getHeaderField("Content-Length"));
            } catch (Exception e1) {
                log4j.logError(e1);
            }
        }
        httpURLConnection.setReadTimeout(5000);// ���ó�ʱʱ��Ϊ20��
        String urlFileName = url.getFile(); // ȡ���ڷ������ϵ�·�����ļ���
        urlFileName = GetFileName(urlFileName); // ȡ���ļ���
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        DataOutputStream dos = null;
        FileOutputStream fos = null;
        File wdFile = new File(filePath + urlFileName);
        fos = new FileOutputStream(wdFile);
        dos = new DataOutputStream(fos);
        byte[] bt = new byte[10240];// ������
        int len = 0;
        while ((len = bis.read(bt)) > 0) {
            dos.write(bt, 0, len);// д�ļ�
        }
        dos.close();
        fos.close();
        bis.close();
        httpURLConnection.disconnect();
    }

    /**
     * ȡ��Ҫ���ص��ļ���
     * 
     * @param file �����ļ�����·��
     * @return �ļ�������չ��
     */
    private static String GetFileName(String file) {
        StringTokenizer st = new StringTokenizer(file, "/");
        while (st.hasMoreTokens()) {
            file = st.nextToken();
        }
        return file;
    }

    /* ����URL��ȡ�ļ��� */
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

    /* ���ݵ�ǰͼƬ��ȡ���µĵ�ַ */
    public static String getNewFileName(String imageSrc) {
        String filename = "";
        String fileName = getFileName(imageSrc);
        String ext = getFileExtensation(fileName);
        filename = getRandomString() + "." + ext;
        return filename;
    }

    /**
     * ��ȡ�ļ��е�����,��������ȫ����Сд����ʽ����
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
     * ��������ı����ȡ�ļ�
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
     * ����ָ�������ȡ�ļ�������
     * 
     * @param path
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getFileContent(String path, String charset) throws Exception {
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
     * ������������ļ���
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
     * ����ָ���ı����ʽ��������������ļ���
     * 
     * @param filePath
     * @param content
     * @param charset
     * @throws Exception
     */
    public static void writeFile(String filePath, String content, String charset) throws Exception {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                                                                                    charset)));
        out.write(content);
        out.close();
    }

    public static void main(String[] args) {
        String url = "www.163.com/a/b.jpg?noscript";
        String filename = getFileName(url);
        log4j.logDebug(filename);

        downloadFileByUrl2("http://img1.51cto.com/attachment/201010/200231826.jpg", "c:/", null);
    }
}
