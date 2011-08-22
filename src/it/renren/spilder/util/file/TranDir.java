package it.renren.spilder.util.file;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.FontUtil;

import java.io.File;

/**
 * ��TranDir.java��ʵ����������ָ���ļ���������ļ����ݣ�ȫ��������������ԣ�����巭��Ϊ����
 * 
 * @author fenglibin 2011-8-9 ����01:31:43
 */
public class TranDir {

    private static String         charset   = "utf-8";
    private static final String[] fileTypes = { "php", "html", "txt", "htm" };

    private static void tranDir(String dir) throws Exception {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                tranDir(file.getAbsolutePath());
            } else {
                tranFile(file.getAbsolutePath());
            }
        }
    }

    private static void tranFile(String file) throws Exception {
        if (!isTranFile(file)) {
            return;
        }
        System.out.println("Tran:" + file);
        String content = FileUtil.getFileContent(file, charset);
        content = FontUtil.jian2fan(content);
        FileUtil.writeFile(file, content, charset);
    }

    private static boolean isTranFile(String file) {
        boolean is = Boolean.FALSE;
        for (String type : fileTypes) {
            if (file.endsWith(type)) {
                is = Boolean.TRUE;
                break;
            }
        }
        return is;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String dir = "/home/fenglibin/proc/renren-spilder/www.w3school.com.cn";
        charset = "gbk";
        tranDir(dir);
    }

}
