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
     * 根据URL获取文件，并返回获取的结果true 或者false
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
     * 根据URL获取文件，并返回获取的结果true 或者false
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
     * 根据URL获取文件名
     * 
     * @param file 网络文件名及路径
     * @return 文件名及扩展名
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
    public static void writeFile(String filePath, String content) throws IOException {
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
     * 根据传入的文件名的扩展名，确定是否常用的图片文件：jpg、png、gif、bmp，是则返回true，否则返回false
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
     * 将文件读入的list中返回，每个element包含一行内容
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
     * 将文件读入的list中返回，每个element包含一行内容
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
     * 将文件读入的list中返回，每个element包含一行内容
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
     * 对给定的目录里面的文件进行重命名,不进行目录的递归处理.默认保留原文件的扩展名，只是在后面增加新的目标扩展名
     * 
     * @param srcPath 待处理的目录
     * @param srcExt 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt) {
        renameFilesInDir(srcPath, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * 对给定的目录里面的文件进行重命名,不进行目录的递归处理.
     * 
     * @param srcPath 待处理的目录
     * @param srcExt 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
     */
    public static void renameFilesInDir(String srcPath, String srcExt, String desExt, boolean keepOldExt) {

        renameFilesInDir(srcPath, srcExt, desExt, Boolean.FALSE, Boolean.TRUE);
    }

    /**
     * 对给定的目录里面的文件进行重命名，并通过参数includeChildDir指定是否要递归子目录，通过参数keepOldExt指定是否保留原文件的扩展名。
     * 
     * @param srcPath 待处理的目录
     * @param srcExt 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     * @param includeChildDir 是否处理子目录中的文件
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
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
     * 对给定的目录里面的文件进行重命名,不进行目录的递归处理。默认保留原文件的扩展名，只是在后面增加新的目标扩展名
     * 
     * @param srcPath 待处理的目录
     * @param srcExts 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
     */
    public static void renameFilesInDir(String srcPath, String[] srcExts, String desExt) {
        renameFilesInDir(srcPath, srcExts, desExt, Boolean.TRUE);
    }

    /**
     * 对给定的目录里面的文件进行重命名,不进行目录的递归处理。
     * 
     * @param srcPath 待处理的目录
     * @param srcExts 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
     */
    public static void renameFilesInDir(String srcPath, String[] srcExts, String desExt, boolean keepOldExt) {
        renameFilesInDir(srcPath, srcExts, desExt, Boolean.FALSE, Boolean.TRUE);
    }

    /**
     * 对给定的目录里面的文件进行重命名,根据传入参数includeChildDir的值判定是否进行目录的递归处理。
     * 
     * @param srcPath 待处理的目录
     * @param srcExts 需要处理的文件类型的扩展名，可以为空，此时会对待处理目录srcPath下面的所有文件进行重命名；如果不为空，则只处理这里指定的扩展名；扩展名需要带上点"."，如".php";<br>
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".php";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
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
     * 将文件重命名并输出到目标文件名，默认保留原文件的扩展名，只是在后面增加新的目标扩展名
     * 
     * @param file 原文件路径名
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
     */
    public static void rename(String file, String srcExt, String desExt) {
        rename(file, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * 将文件重命名并输出到目标文件名，默认保留原文件的扩展名，只是在后面增加新的目标扩展名
     * 
     * @param file 原文件路径名
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     */
    public static void rename(String file, String srcExt, String desExt, boolean keepOldExt) {
        File sourceFile = new File(file);
        rename(sourceFile, srcExt, desExt, keepOldExt);
    }

    /**
     * 将文件重命名并输出到目标文件名
     * 
     * @param file 原文件
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
     */
    public static void rename(File file, String srcExt, String desExt) {
        rename(file, srcExt, desExt, Boolean.TRUE);
    }

    /**
     * 将文件重命名并输出到目标文件名
     * 
     * @param file 原文件
     * @param srcExt 原文件扩展名，扩展名需要带上点"."，如".php";<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这个如果不为空，则表示只针扩展名为此的进行重命名，否则不对给定的文件进行判断，都进行重命名
     * @param desExt 目标扩展名，扩展名需要带上点"."，如".phps";<br>
     * @param keepOldExt 是否保留原文件的扩展名，只是在后面增加新的目标扩展名
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

        // 这种做法是在不去掉原文件扩展名，只是在后面补充新的扩展名
        if (keepOldExt) {
            file.renameTo(new File(file.getAbsolutePath() + desExt));
        } else {
            // 这种做法是替换原扩展名，使用新的扩展名
            file.renameTo(new File(replaceFileExt(file.getAbsolutePath(), desExt)));
        }
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

    /**
     * 返回所有文件列表
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
     * 返回所有目录列表
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
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param isReg 是否是正则替换
     * @param charset 文件编码
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isRecursive, boolean isReg,
                                           String charset) throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, isReg, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param isReg 是否是正则替换
     * @param charset 文件编码
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isReg, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, isReg, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param charset 文件编码
     */
    public static void replaceContentInDir_(String source, String desc, String dir, List<String> fileType,
                                            boolean isRecursive, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, Boolean.FALSE, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param isReg 是否是正则替换
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isRecursive, boolean isReg) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, isReg, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param isReg 是否是正则替换
     * @param charset 文件编码
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isReg, String charset)
                                                                                                                 throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, isReg, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param charset 文件编码
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType, String charset)
                                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, Boolean.FALSE, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType,
                                           boolean isRecursive) throws IOException {
        replaceContentInDir(source, desc, dir, fileType, isRecursive, Boolean.FALSE, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param charset 文件编码
     */
    public static void replaceContentInDir_(String source, String desc, String dir, boolean isRecursive, String charset)
                                                                                                                        throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, Boolean.FALSE, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param isReg 是否是正则替换
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isRecursive, boolean isReg)
                                                                                                                      throws IOException {
        replaceContentInDir(source, desc, dir, null, isRecursive, isReg, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     * @param isReg 是否是正则替换
     */
    public static void replaceContentInDir_(String source, String desc, String dir, List<String> fileType, boolean isReg)
                                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, isReg, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param charset 文件编码
     */
    public static void replaceContentInDir(String source, String desc, String dir, String charset) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, Boolean.FALSE, charset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param isReg 是否是正则替换
     */
    public static void replaceContentInDir(String source, String desc, String dir, boolean isReg) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, isReg, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     * @param fileType 文件类型扩展名列表List
     */
    public static void replaceContentInDir(String source, String desc, String dir, List<String> fileType)
                                                                                                         throws IOException {
        replaceContentInDir(source, desc, dir, fileType, Boolean.FALSE, Boolean.FALSE, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目录
     */
    public static void replaceContentInDir(String source, String desc, String dir) throws IOException {
        replaceContentInDir(source, desc, dir, null, Boolean.FALSE, Boolean.FALSE, defaultCharset);
    }

    /**
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param source 源字符，即被替换的字符串
     * @param desc 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目
     * @param fileType 文件类型扩展名列表List
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param isReg 是否是正则替换
     * @param charset 文件编码
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
     * 在指定的目录下，对文件进行内容替换，可以指定对指定文件类型进行替换，也可以指定是否进行递归
     * 
     * @param sourceList 源字符，即被替换的字符串
     * @param descList 目标字符串，替换源字符串的字符串
     * @param dir 待替换文件所在目
     * @param fileType 文件类型扩展名列表List
     * @param isRecursive 是否对待替换目录下面的子目录进行替换
     * @param isReg 是否是正则替换
     * @param charset 文件编码
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
            // 判断是否需要替换
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
