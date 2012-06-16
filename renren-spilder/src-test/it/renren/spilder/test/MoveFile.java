package it.renren.spilder.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MoveFile {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String dir = "/usr/fenglibin/renren-spilder/allimg/2011-09-23";
        String descDir = "/usr/fenglibin/renren-spilder/allimg/2011-09-23";
        moveFile(dir, descDir, Boolean.TRUE);
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

    public static void readAndWrite(File file, String outPath) throws IOException {
        byte[] bt = new byte[10240];
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(outPath + File.separator + file.getName());
        int len = 0;
        while ((len = fis.read(bt)) != -1) {
            fos.write(bt, 0, len);
        }
        fos.close();
        fis.close();
        System.out.println("write 2 file finished.");
    }

}
