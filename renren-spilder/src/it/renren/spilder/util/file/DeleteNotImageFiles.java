package it.renren.spilder.util.file;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ɾ��ָ��·���£���չ��Ϊָ����ʽ(jpg��png��gif��bmp)�ļ���ͼƬ�ļ�����ʵ����ȴ����ͼƬ���ļ�
 * 
 * @author fenglibin 2012-6-15 ����08:45:21
 */
public class DeleteNotImageFiles {

    private static String basePath = "/home/fenglibin/www/img.renren.it/";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
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
        check(getBasePath());
    }

    /**
     * ɾ��ָ��·���£���չ��Ϊָ����ʽ(jpg��png��gif��bmp)�ļ���ͼƬ�ļ�����ʵ����ȴ����ͼƬ���ļ�
     * 
     * @param path
     * @throws Exception
     */
    private static void check(String path) throws Exception {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                if (!ImageUtil.isImage(file)) {
                    System.out.println("delete" + file.getAbsolutePath());
                    file.delete();
                }
            }
            fileList.clear();
            fileList = null;
            for (File file : dirList) {
                check(file.getPath());
            }
        }
    }

    /**
     * ��ȡ�ļ���list�б��������ļ���
     * 
     * @param files
     * @return
     */
    private static List<File> getFileList(File[] files) {
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (FileUtil.isImageUsualFileByExt(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * ��ȡ�ļ��е�list�б��������ļ���
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
}
