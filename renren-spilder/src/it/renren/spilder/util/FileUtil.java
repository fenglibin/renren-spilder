package it.renren.spilder.util;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.util.wash.WashBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class FileUtil {

    private static Log4j       log4j          = new Log4j(FileUtil.class.getName());
    private static PrintWriter appendWriter;
    public static String       defaultCharset = "GBK";

    /**
     * ����URL��ȡ�ļ��������ػ�ȡ�Ľ��true ����false
     * 
     * @param srcUrl
     * @param fileSavePath
     * @return
     * @throws IOException
     */
    public static synchronized boolean downloadFile(String srcUrl, String filePath) throws IOException {
        return downloadFile(srcUrl, filePath, null);
    }

    /**
     * ����URL��ȡ�ļ��������ػ�ȡ�Ľ��true ����false
     * 
     * @param srcUrl
     * @param fileSavePath
     * @param newName
     * @return
     * @throws IOException
     */
    public static synchronized boolean downloadFile(String srcUrl, String fileSavePath, String newName)
                                                                                                       throws IOException {
        boolean result = true;
        org.apache.commons.httpclient.HttpClient httpclient = HttpClientUtil.getHttpClient(srcUrl);
        GetMethod get = new GetMethod(UrlUtil.prettyUrl(srcUrl));
        FileOutputStream out = null;
        String fileName = null;
        File wdFile = null;
        try {
            fileName = newName == null ? getFileName(srcUrl) : newName;
            wdFile = new File(fileSavePath + fileName);
            out = new FileOutputStream(wdFile);

            httpclient.executeMethod(get);

            InputStream instream = get.getResponseBodyAsStream();
            int l;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
        } catch (HttpException e) {
            result = false;
            log4j.logError("srcUrl:" + srcUrl + ",fileSavePath:" + fileSavePath, e);
        } catch (IOException e) {
            result = false;
            log4j.logError("srcUrl:" + srcUrl + ",fileSavePath:" + fileSavePath, e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log4j.logError("srcUrl:" + srcUrl + ",fileSavePath:" + fileSavePath, e);
                }
            }
            if (!ImageUtil.isImage(wdFile)) {
                result = false;
                StringUtil.writeGetImageToFile(srcUrl, fileSavePath, fileName);
            }
        }
        return result;
    }

    /**
     * ����URL��ȡ�ļ���
     * 
     * @param file �����ļ�����·��
     * @return �ļ�������չ��
     */
    public static String getFileName(String imageSrc) {
        String filename = "";
        String[] array = imageSrc.split("\\/");
        filename = array[array.length - 1];
        if (filename.indexOf("?") > 0) {
            filename = filename.split("\\?")[0];
        }
        return filename;
    }

    public static String getFileNameWithoutExt(String filename) {
        String ext = getFileExtensation(filename);
        if (!StringUtil.isEmpty(ext)) {
            ext = "." + ext;
            filename = filename.replace(ext, "");
        }
        return filename;
    }

    /**
     * �����ļ�����չ�������û����չ���ģ��򷵻ؿ��ַ���""
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

    public static String getRandomString() {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf((int) (1000 * Math.random()));
    }

    /* ���ݵ�ǰͼƬ��ȡ���µĵ�ַ */
    public static String getNewFileName(String imageSrc) {
        String fileName = getFileName(imageSrc);
        String ext = getFileExtensation(fileName);
        fileName = getRandomString() + Constants.DOT + ext;
        return fileName;
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
     * @throws IOException
     * @throws Exception
     */
    public static String getFileContent(String path) throws IOException {
        return read(path);
    }

    /**
     * ����ָ�������ȡ�ļ�������
     * 
     * @param path
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getFileContent(File path, String charset) throws IOException {
        String content = "";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path), charset);
        content = read(reader);
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
    public static String getFileContent(String path, String charset) throws IOException {
        String content = "";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path), charset);
        content = read(reader);
        return content;
    }

    /**
     * ���������ļ���һ��һ�еķŵ�List��
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
     * ������������ļ���
     * 
     * @param filePath
     * @param content
     * @throws Exception
     */
    public static void writeFile(String filePath, String content) throws IOException {
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
    public static void writeFile(String filePath, String content, String charset) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                                                                                    charset)));
        out.write(content);
        out.close();
    }

    /**
     * ��׷�ӵķ�ʽ����ļ�
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

    /**
     * ��Դ�ļ����е��ļ����ƶ���Ŀ���ļ��У���ʱĿ���ļ����в��Ὺ��Ŀ¼�ṹ��������Դ�ļ����е������ļ�
     * 
     * @param srcDir
     * @param descDir
     * @throws IOException
     */
    public static void moveFile(String srcDir, String descDir) throws IOException {
        moveFile(srcDir, descDir, Boolean.TRUE);
    }

    /**
     * ��Դ�ļ����е��ļ����ƶ���Ŀ���ļ��У���ʱĿ���ļ����в��Ὺ��Ŀ¼�ṹ��������Դ�ļ����е������ļ������Կ����Ƿ������Ŀ¼���ļ�һ���ƶ�����:<br>
     * Դ�ļ���Ŀ¼�ṹ:<br>
     * srcDir<br>
     * &nbsp;&nbsp;--file1.jpg<br>
     * &nbsp;&nbsp;--file2.jpg<br>
     * &nbsp;&nbsp;--subDir1<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;--file3.jpg<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;--file4.jpg<br>
     * &nbsp;&nbsp;--subDir2<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;--file5.jpg<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;--file6.jpg<br>
     * <br>
     * ��ʱ�ƶ���ʱ�����ָ���İ�����Ŀ¼һ���ƶ������ƶ���Ľ�����ǣ�<br>
     * descDir<br>
     * &nbsp;&nbsp;--file1.jpg<br>
     * &nbsp;&nbsp;--file2.jpg<br>
     * &nbsp;&nbsp;--file3.jpg<br>
     * &nbsp;&nbsp;--file4.jpg<br>
     * &nbsp;&nbsp;--file5.jpg<br>
     * &nbsp;&nbsp;--file6.jpg<br>
     * <br>
     * �����������Ŀ¼�����ƶ���Ľṹ�ǣ�<br>
     * descDir<br>
     * &nbsp;&nbsp;--file1.jpg<br>
     * &nbsp;&nbsp;--file2.jpg<br>
     * <br>
     * 
     * @param srcDir Դ�ļ���
     * @param descDir Ŀ���ļ���
     * @param isIncludeChildDir �Ƿ�����ƶ���Ŀ¼������ļ�
     * @throws IOException
     */
    public static void moveFile(String srcDir, String descDir, boolean isIncludeChildDir) throws IOException {
        File fileDir = new File(srcDir);
        File[] fileList = fileDir.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                File[] childFileList = file.listFiles();
                for (File childFile : childFileList) {
                    readAndWrite(childFile, srcDir);
                }
            }
        }
    }

    /**
     * �ļ�����
     * 
     * @param file
     * @param outFile
     * @throws IOException
     */
    public static void copy(File file, String outFile) throws IOException {
        readAndWrite(file, outFile);
    }

    /**
     * ����һ���ļ������Ϊ�µ��ļ�
     * 
     * @param file
     * @param outFile
     * @throws IOException
     */
    public static void readAndWrite(File file, String outFile) throws IOException {
        byte[] bt = new byte[10240];
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(outFile);
        int len = 0;
        while ((len = fis.read(bt)) != -1) {
            fos.write(bt, 0, len);
        }
        fos.close();
        fis.close();
    }

    /**
     * ���ݴ�����ļ�������չ����ȷ���Ƿ��õ�ͼƬ�ļ���jpg��png��gif��bmp�����򷵻�true�����򷵻�false
     * 
     * @param fileName
     * @return
     */
    public static boolean isImageUsualFileByExt(String fileName) {
        boolean is = Boolean.FALSE;
        String extName = getFileExtensation(fileName);
        if (!StringUtil.isEmpty(extName)) {
            extName = extName.toLowerCase();
            if (extName.equals("jpg") || extName.equals("jpeg") || extName.equals("png") || extName.equals("gif")
                || extName.equals("bmp")) {
                is = Boolean.TRUE;
            }
        }
        return is;

    }

    /**
     * get file content by given file path
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String read(String filePath) throws IOException {
        FileReader fr = new FileReader(filePath);
        return read(fr);
    }

    /**
     * get file content by given file
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String read(File file) throws IOException {
        FileReader fr = new FileReader(file);
        return read(fr);
    }

    /**
     * get file content by class path,the default charset is GBK
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static String readFromClassPath(String classpath) throws IOException {
        return readFromClassPath(classpath, defaultCharset);
    }

    /**
     * ��CLASSPATH�и���ָ���ı����ȡ����
     * 
     * @param classpath
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readFromClassPath(String classpath, String charset) throws IOException {
        InputStream is = Thread.currentThread().getClass().getResourceAsStream(classpath);
        InputStreamReader reader = new InputStreamReader(is, charset);
        return read(reader);
    }

    /**
     * get file content by InputStream
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static String read(InputStream is) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(is);
        return read(streamReader);
    }

    /**
     * get file content by InputStreamReader
     * 
     * @param fr
     * @return
     * @throws IOException
     */
    public static String read(InputStreamReader fr) throws IOException {
        String result = "";
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            result += line;
            line = br.readLine();
            if (line != null) {
                result += "\n";
            }
        }
        br.close();
        fr.close();
        return result;
    }

    /**
     * ���ļ������list�з��أ�ÿ��element����һ������
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> readFile2List(String filePath) throws IOException {
        FileReader fr = new FileReader(filePath);
        return readFile2List(fr);
    }

    /**
     * ���ļ������list�з��أ�ÿ��element����һ������
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> readFile2List(File file) throws IOException {
        FileReader fr = new FileReader(file);
        return readFile2List(fr);
    }

    /**
     * ���ļ������list�з��أ�ÿ��element����һ������
     * 
     * @param fr
     * @return
     * @throws IOException
     */
    public static List<String> readFile2List(InputStreamReader fr) throws IOException {
        List<String> fileList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while ((line = br.readLine()) != null) {
            if (!StringUtil.isEmpty(line)) {
                fileList.add(line);
            }
        }
        br.close();
        fr.close();
        return fileList;
    }

    /**
     * �Ը�����Ŀ¼������ļ�����������,������Ŀ¼�ĵݹ鴦��.Ĭ�ϱ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExt ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt) {
        renameFilesInDir(srcPath, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * �Ը�����Ŀ¼������ļ�����������,������Ŀ¼�ĵݹ鴦��.
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExt ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt, boolean keepOldExt) {

        renameFilesInDir(srcPath, srcExt, desExt, Boolean.FALSE, Boolean.TRUE);
    }

    /**
     * �Ը�����Ŀ¼������ļ���������������ͨ������includeChildDirָ���Ƿ�Ҫ�ݹ���Ŀ¼��ͨ������keepOldExtָ���Ƿ���ԭ�ļ�����չ����
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExt ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param includeChildDir �Ƿ�����Ŀ¼�е��ļ�
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt, Boolean includeChildDir,
                                        boolean keepOldExt) {
        File filePath = new File(srcPath);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    rename(file, srcExt, desExt, keepOldExt);
                } else if (includeChildDir) {
                    renameFilesInDir(file.getAbsolutePath(), srcExt, desExt, includeChildDir, keepOldExt);
                }
            }
        }
    }

    /**
     * �Ը�����Ŀ¼������ļ�����������,������Ŀ¼�ĵݹ鴦��Ĭ�ϱ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExts ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void renameFilesInDir(String srcPath, String[] srcExts, String desExt) {
        renameFilesInDir(srcPath, srcExts, desExt, Boolean.TRUE);
    }

    /**
     * �Ը�����Ŀ¼������ļ�����������,������Ŀ¼�ĵݹ鴦��
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExts ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void renameFilesInDir(String srcPath, String[] srcExts, String desExt, boolean keepOldExt) {
        renameFilesInDir(srcPath, srcExts, desExt, Boolean.FALSE, Boolean.TRUE);
    }

    /**
     * �Ը�����Ŀ¼������ļ�����������,���ݴ������includeChildDir��ֵ�ж��Ƿ����Ŀ¼�ĵݹ鴦��
     * 
     * @param srcPath �������Ŀ¼
     * @param srcExts ��Ҫ������ļ����͵���չ��������Ϊ�գ���ʱ��Դ�����Ŀ¼srcPath����������ļ������������������Ϊ�գ���ֻ��������ָ������չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".php";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void renameFilesInDir(String srcPath, String[] srcExts, String desExt, Boolean includeChildDir,
                                        boolean keepOldExt) {
        File filePath = new File(srcPath);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    boolean doRename = Boolean.FALSE;
                    String srcExt = null;
                    if (srcExts != null && srcExts.length > 0) {
                        for (String tSrcExt : srcExts) {
                            if (file.getName().endsWith(tSrcExt)) {
                                srcExt = tSrcExt;
                                doRename = Boolean.TRUE;
                                break;
                            }
                        }
                    } else {
                        doRename = Boolean.TRUE;
                    }
                    if (doRename) {
                        rename(file, srcExt, desExt, keepOldExt);
                    }
                } else if (includeChildDir) {
                    renameFilesInDir(file.getAbsolutePath(), srcExts, desExt, includeChildDir, keepOldExt);
                }
            }
        }
    }

    /**
     * ���ļ��������������Ŀ���ļ�����Ĭ�ϱ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     * 
     * @param file ԭ�ļ�·����
     * @param srcExt ԭ�ļ���չ������չ����Ҫ���ϵ�"."����".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������Ϊ�գ����ʾֻ����չ��Ϊ�˵Ľ��������������򲻶Ը������ļ������жϣ�������������
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".phps";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void rename(String file, String srcExt, String desExt) {
        rename(file, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * ���ļ��������������Ŀ���ļ�����Ĭ�ϱ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     * 
     * @param file ԭ�ļ�·����
     * @param srcExt ԭ�ļ���չ������չ����Ҫ���ϵ�"."����".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������Ϊ�գ����ʾֻ����չ��Ϊ�˵Ľ��������������򲻶Ը������ļ������жϣ�������������
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".phps";<br>
     */
    public static void rename(String file, String srcExt, String desExt, boolean keepOldExt) {
        File sourceFile = new File(file);
        rename(sourceFile, srcExt, desExt, keepOldExt);
    }

    /**
     * ���ļ��������������Ŀ���ļ���
     * 
     * @param file ԭ�ļ�
     * @param srcExt ԭ�ļ���չ������չ����Ҫ���ϵ�"."����".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������Ϊ�գ����ʾֻ����չ��Ϊ�˵Ľ��������������򲻶Ը������ļ������жϣ�������������
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".phps";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void rename(File file, String srcExt, String desExt) {
        rename(file, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * ���ļ��������������Ŀ���ļ���
     * 
     * @param file ԭ�ļ�
     * @param srcExt ԭ�ļ���չ������չ����Ҫ���ϵ�"."����".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������Ϊ�գ����ʾֻ����չ��Ϊ�˵Ľ��������������򲻶Ը������ļ������жϣ�������������
     * @param desExt Ŀ����չ������չ����Ҫ���ϵ�"."����".phps";<br>
     * @param keepOldExt �Ƿ���ԭ�ļ�����չ����ֻ���ں��������µ�Ŀ����չ��
     */
    public static void rename(File file, String srcExt, String desExt, boolean keepOldExt) {
        if (file == null) {
            throw new RuntimeException("Source file is null.");
        }
        if (!file.exists()) {
            throw new RuntimeException("Source file does not exists:" + file.getAbsolutePath());
        }
        if (!StringUtil.isEmpty(srcExt) && !file.getName().endsWith(srcExt)) {
            return;
        }
        if (!desExt.startsWith(".")) {
            return;
        }

        // �����������ڲ�ȥ��ԭ�ļ���չ����ֻ���ں��油���µ���չ��
        if (keepOldExt) {
            file.renameTo(new File(file.getAbsolutePath() + desExt));
        } else {
            // �����������滻ԭ��չ����ʹ���µ���չ��
            file.renameTo(new File(replaceFileExt(file.getAbsolutePath(), desExt)));
        }
    }

    /**
     * �滻�����ļ�����չ������ԭ�ļ�fileΪ��"d:/a/b/c.txt"��desExtΪ".php"��������"d:/a/b/c.php"
     * 
     * @param file ԭ�ļ���·��
     * @param desExt �����չ������չ����Ҫ���ϵ�"."
     * @return
     */
    public static String replaceFileExt(String file, String desExt) {
        if (file.indexOf(".") > 0 && desExt.startsWith(".")) {
            file = file.substring(0, file.lastIndexOf("."));
            file += desExt;
        }
        return file;
    }

    /**
     * ���������ļ��б�
     * 
     * @param files
     * @return
     */
    public static List<File> getFileList(File[] files) {
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * ��������Ŀ¼�б�
     * 
     * @param files
     * @return
     */
    public static List<File> getDirList(File[] files) {
        List<File> dirList = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory()) {
                dirList.add(file);
            }
        }
        return dirList;
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param isReg �Ƿ��������滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isRecursive, boolean isReg,
                                           String charset) throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, isReg, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param isReg �Ƿ��������滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isReg, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, isReg, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir_(String source, String desc, String dir, List<String> fileType,
                                            boolean isRecursive, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, Boolean.FALSE, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param isReg �Ƿ��������滻
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isRecursive, boolean isReg) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, isReg, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param isReg �Ƿ��������滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isReg, String charset)
                                                                                                                 throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, isReg, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType, String charset)
                                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, Boolean.FALSE, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isRecursive) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, Boolean.FALSE, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir_(String source, String desc, String dir, boolean isRecursive, String charset)
                                                                                                                        throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, Boolean.FALSE, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param isReg �Ƿ��������滻
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isRecursive, boolean isReg)
                                                                                                                      throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, isReg, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     * @param isReg �Ƿ��������滻
     */
    public static void replaceContentInDir_(String source, String desc, String dir, List<String> fileType, boolean isReg)
                                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, isReg, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, Boolean.FALSE, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param isReg �Ƿ��������滻
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isReg) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, isReg, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     * @param fileType �ļ�������չ���б�List
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType)
                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, Boolean.FALSE, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ¼
     */
    public static void replaceContentInDir(String source, String desc, String dir) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, Boolean.FALSE, defaultCharset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param source Դ�ַ��������滻���ַ���
     * @param desc Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ
     * @param fileType �ļ�������չ���б�List
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param isReg �Ƿ��������滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isRecursive, boolean isReg, String charset) throws IOException {
        List<String> sourceList = new ArrayList<String>();
        List<String> descList = new ArrayList<String>();
        sourceList.add(source);
        descList.add(desc);
        replaceContentInDir(sourceList, descList, dir, fileType, isRecursive, isReg, charset);
    }

    /**
     * ��ָ����Ŀ¼�£����ļ����������滻������ָ����ָ���ļ����ͽ����滻��Ҳ����ָ���Ƿ���еݹ�
     * 
     * @param sourceList Դ�ַ��������滻���ַ���
     * @param descList Ŀ���ַ������滻Դ�ַ������ַ���
     * @param dir ���滻�ļ�����Ŀ
     * @param fileType �ļ�������չ���б�List
     * @param isRecursive �Ƿ�Դ��滻Ŀ¼�������Ŀ¼�����滻
     * @param isReg �Ƿ��������滻
     * @param charset �ļ�����
     */
    public static void replaceContentInDir(List<String> sourceList, List<String> descList, String dir,
                                           List<String> fileType, boolean isRecursive, boolean isReg, String charset)
                                                                                                                     throws IOException {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            throw new RuntimeException("The directory:" + dir + " not exist.");
        }
        if (sourceList == null || sourceList.size() == 0 || descList == null || descList.size() == 0) {
            return;
        }
        if (sourceList.size() != descList.size()) {
            throw new RuntimeException("The sourceList size must be the same as the descList.");
        }
        if (StringUtil.isEmpty(charset)) {
            throw new RuntimeException("Must give the file charset.");
        }
        File[] files = dirFile.listFiles();
        List<File> fileList = getFileList(files);
        List<File> dirList = getDirList(files);
        for (File file : fileList) {
            String ext = getFileExtensation(file.getName());
            if (fileType != null && fileType.size() > 0 && !fileType.contains(ext)) {
                continue;
            }
            String content = getFileContent(file.getAbsolutePath(), charset);
            boolean doReplace = Boolean.FALSE;
            // �ж��Ƿ���Ҫ�滻
            for (String source : sourceList) {
                if (content.indexOf(source) >= 0) {
                    doReplace = Boolean.TRUE;
                    break;
                }
            }
            if (doReplace) {
                content = StringUtil.replaceContent(content, sourceList, descList);
                System.out.println("Replaced content in:" + file.getAbsolutePath());
                FileUtil.writeFile(file.getAbsolutePath(), content, charset);
            }
        }
        if (isRecursive) {
            for (File _dir : dirList) {
                replaceContentInDir(sourceList, descList, _dir.getAbsolutePath(), fileType, isRecursive, isReg, charset);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // String url = "www.163.com/a/b.jpg?noscript";
        // String filename = getFileName(url);
        // log4j.logDebug(filename);
        String url = "http://dl.iteye.com/upload/attachment/0070/1064/fe8c9ba8-49fe-3281-86ed-82dbe5817466.jpeg";
        String savePath = "d:/test/";
        WashBase.initArgs(args);
        if (!StringUtil.isEmpty(System.getProperty("url"))) {
            url = System.getProperty("url");
        }
        if (!StringUtil.isEmpty(System.getProperty("savePath"))) {
            savePath = System.getProperty("savePath");
        }
        downloadFile(url, savePath, null);
    }
}
