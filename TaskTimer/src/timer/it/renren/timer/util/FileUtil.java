package it.renren.timer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

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
     * 根据指定编码读取文件的内容
     * 
     * @param path
     * @param charset
     * @return
     * @throws Exception
     */
    public static String read(String path, String charset) throws IOException {
        String content = "";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path), charset);
        content = read(reader);
        return content;
    }

    /**
     * 根据指定编码读取文件的内容
     * 
     * @param file
     * @param charset
     * @return
     * @throws Exception
     */
    public static String read(File file, String charset) throws IOException {
        String content = "";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
        content = read(reader);
        return content;
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
     * 根据指定的编码格式，将内容输出到文件中
     * 
     * @param filePath
     * @param content
     * @param charset
     * @throws Exception
     */
    public static void writeFile(String filePath, String content) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                                                                                    "GBK")));
        out.write(content);
        out.close();
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
}
