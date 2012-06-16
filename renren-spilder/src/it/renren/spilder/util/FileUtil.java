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

    private static Log4j       log4j          = new Log4j(FileUtil.class.getName());
    private static PrintWriter appendWriter;
    private static String      defaultCharset = "GBK";

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
     * @throws IOException
     * @throws Exception
     */
    public static String getFileContent(String path) throws IOException {
        return read(path);
    }

    /**
     * 根据指定编码读取文件的内容
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
     * 根据指定编码读取文件的内容
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

    /**
     * 将源文件夹中的文件，移动到目标文件中，此时目标文件夹中不会开成目录结构，而将是源文件夹中的所有文件
     * 
     * @param srcDir
     * @param descDir
     * @throws IOException
     */
    public static void moveFile(String srcDir, String descDir) throws IOException {
        moveFile(srcDir, descDir, Boolean.TRUE);
    }

    /**
     * 将源文件夹中的文件，移动到目标文件中，此时目标文件夹中不会开成目录结构，而将是源文件夹中的所有文件，可以控制是否包括子目录的文件一起移动，如:<br>
     * 源文件的目录结构:<br>
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
     * 此时移动的时候，如果指定的包括子目录一起移动，则移动后的结果将是：<br>
     * descDir<br>
     * &nbsp;&nbsp;--file1.jpg<br>
     * &nbsp;&nbsp;--file2.jpg<br>
     * &nbsp;&nbsp;--file3.jpg<br>
     * &nbsp;&nbsp;--file4.jpg<br>
     * &nbsp;&nbsp;--file5.jpg<br>
     * &nbsp;&nbsp;--file6.jpg<br>
     * <br>
     * 如果不包括子目录，则移动后的结构是：<br>
     * descDir<br>
     * &nbsp;&nbsp;--file1.jpg<br>
     * &nbsp;&nbsp;--file2.jpg<br>
     * <br>
     * 
     * @param srcDir 源文件夹
     * @param descDir 目标文件夹
     * @param isIncludeChildDir 是否包括移动子目录下面的文件
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
     * 文件拷贝
     * 
     * @param file
     * @param outFile
     * @throws IOException
     */
    public static void copy(File file, String outFile) throws IOException {
        readAndWrite(file, outFile);
    }

    /**
     * 读入一个文件并输出为新的文件
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
     * 根据传入的文件名，确定是否常用的图片文件：jpg、png、gif、bmp，是则返回true，否则返回false
     * 
     * @param fileName
     * @return
     */
    public static boolean isImageUsualFile(String fileName) {
        boolean is = Boolean.FALSE;
        String extName = getFileExtensation(fileName);
        if (!StringUtil.isEmpty(extName)) {
            extName = extName.toLowerCase();
            if (extName.equals("jpg") || extName.equals("png") || extName.equals("gif") || extName.equals("bmp")) {
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
     * 从CLASSPATH中根据指定的编码读取内容
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
     * 对给定的目录里面的文件进行重命名,不进行目录的递归处理
     * 
     * @param srcPath
     * @param srcExt
     * @param desExt
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt) {

        renameFilesInDir(srcPath, srcExt, desExt, Boolean.FALSE);
    }

    /**
     * 对给定的目录里面的文件进行重命名，并通过参数includeChildDir指定是否要递归子目录。如果指定了递归，则输出目录会保持和原目录一样的目录结构。
     * 
     * @param srcPath
     * @param srcExt
     * @param desPath
     * @param desExt
     * @param includeChildDir
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt, Boolean includeChildDir) {
        File filePath = new File(srcPath);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    rename(file, srcExt, desExt);
                } else if (includeChildDir) {
                    renameFilesInDir(file.getAbsolutePath(), srcExt, desExt, includeChildDir);
                }
            }
        }
    }

    /**
     * 将文件重命名并输出到目标文件名
     * 
     * @param file 原文件路径名
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     */
    public static void rename(String file, String srcExt, String desExt) {
        File sourceFile = new File(file);
        rename(sourceFile, srcExt, desExt);
    }

    /**
     * 将文件重命名并输出到目标文件名
     * 
     * @param file 原文件
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     */
    public static void rename(File sourceFile, String srcExt, String desExt) {
        if (sourceFile == null) {
            throw new RuntimeException("Source file is null.");
        }
        if (!sourceFile.exists()) {
            throw new RuntimeException("Source file does not exists:" + sourceFile.getAbsolutePath());
        }
        if (!StringUtil.isEmpty(srcExt) && !sourceFile.getName().endsWith(srcExt)) {
            return;
        }
        if (!desExt.startsWith(".")) {
            return;
        }
        sourceFile.renameTo(new File(replaceFileExt(sourceFile.getAbsolutePath(), desExt)));
    }

    /**
     * 替换给定文件的扩展名，如原文件file为："d:/a/b/c.txt"，desExt为".php"，则结果是"d:/a/b/c.php"
     * 
     * @param file 原文件的路径
     * @param desExt 结果扩展名，扩展名需要带上点"."
     * @return
     */
    public static String replaceFileExt(String file, String desExt) {
        if (file.indexOf(".") > 0 && desExt.startsWith(".")) {
            file = file.substring(0, file.lastIndexOf("."));
            file += desExt;
        }
        return file;
    }

    public static void main(String[] args) {
        // String url = "www.163.com/a/b.jpg?noscript";
        // String filename = getFileName(url);
        // log4j.logDebug(filename);

        downloadFileByUrl("http://img1.51cto.com/attachment/201010/200231826.jpg", "/home/fenglibin/tmp/img/", null);
    }
}
