package it.renren.spilder.util.web.antivirus;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.wash.WashBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �������ļ��������ļ�ԭ���������Ϣ�������ļ����бȽϣ����ݴ����жϸ��ļ��Ƿ��Ǳ��޸Ļ��������ӵ�<br>
 * ��AntiVirus.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2012-6-27 ����09:27:13
 */
public class AntiVirus extends WashBase {

    // ָ������ļ���·��
    private static String              checkLocation   = "d:/test/checkLocation";
    // �����ʹ���ļ���Ϣ���ļ�
    private static String              saveFile        = "d:/test/saveFile.txt";
    // �Ƿ��ʹ��
    private static boolean             init            = false;
    // �����ļ����·��
    private static String              badFileLocation = "d:/test/badFileLocation";
    // �ļ��б�
    private static List<String>        filesList       = new ArrayList<String>();
    private static Map<String, String> filesMap        = new HashMap<String, String>();
    private static final String        seperateChar    = ":";
    // ������ļ����ͣ������Ƕ����������ð��":"�ָ�
    private static String              fileType        = ".php";
    private static List<String>        fileTypeList    = new ArrayList<String>();
    // ��������ļ������ƣ������Ƕ����������ð��":"�ָ�
    private static String              notCheckDir     = "a:my:w3school";
    private static List<String>        notCheckDirList = new ArrayList<String>();
    // ɾ�����¼��ʱ�µ��ļ�
    private static boolean             isDealNewFile   = false;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        initArgs(args);
        init();
        if (!init) {
            filesList = FileUtil.readFile2List(saveFile);
            for (String value : filesList) {
                String key = value.substring(0, value.indexOf(seperateChar));
                filesMap.put(key, value);
            }
        }
        analysis(checkLocation);
        if (init) {
            writeInit();
        }

    }

    private static void analysis(String path) throws Exception {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                String _path = file.getAbsolutePath();
                _path = _path.replace("\\", "/");
                _path = _path.replace(checkLocation, "");
                String fileInfo = _path + seperateChar + file.length() + seperateChar + file.lastModified();
                if (init) {
                    filesList.add(fileInfo);
                } else {
                    String storedFileInfo = filesMap.get(_path);
                    if (storedFileInfo != null) {// ��������ļ�
                        if (!fileInfo.equals(storedFileInfo)) {
                            System.out.println("changed file:" + file.getAbsolutePath());
                            FileUtil.copy(file, badFileLocation + File.separator + file.getName() + ".changed");
                        }
                    } else {// ����������ļ�
                        System.out.println("new file:" + file.getAbsolutePath() + ". fileInfo:" + fileInfo
                                           + ".storedFileInfo:" + storedFileInfo);
                        FileUtil.copy(file, badFileLocation + File.separator + file.getName() + ".new");
                        if (isDealNewFile) {
                            file.delete();
                        }
                    }
                }
            }
            for (File file : dirList) {
                analysis(file.getPath());
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
            if (file.isFile()) {
                if (!StringUtil.isEmpty(fileType)) {
                    for (String ext : fileTypeList) {
                        if (file.getName().endsWith(ext)) {
                            fileList.add(file);
                            break;
                        }
                    }
                } else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * ��ȡ�ļ��е�list�б��������ļ�
     * 
     * @param files
     * @return
     */
    private static List<File> getDirList(File[] files) {
        List<File> dirList = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().endsWith(".svn")) {
                boolean add = true;
                if (!StringUtil.isEmpty(notCheckDir)) {
                    for (String dir : notCheckDirList) {
                        if (file.getName().equals(dir)) {
                            add = false;
                            break;
                        }
                    }

                }
                if (add) {
                    dirList.add(file);
                }
            }
        }
        return dirList;
    }

    /**
     * �����������ļ�
     * 
     * @throws IOException
     */
    private static void writeInit() throws IOException {
        String content = "";
        for (String value : filesList) {
            content += value + "\n";
        }
        FileUtil.writeFile(saveFile, content);
    }

    private static void init() {
        String value = System.getProperty("saveFile");
        if (!StringUtil.isEmpty(value)) {
            saveFile = value;
        }

        value = System.getProperty("init");
        if (!StringUtil.isEmpty(value)) {
            init = Boolean.parseBoolean(value);
        }

        value = System.getProperty("badFileLocation");
        if (!StringUtil.isEmpty(value)) {
            badFileLocation = value;
        }

        value = System.getProperty("checkLocation");
        if (!StringUtil.isEmpty(value)) {
            checkLocation = value;
        }

        value = System.getProperty("fileType");
        if (!StringUtil.isEmpty(value)) {
            fileType = value;
        }
        String[] strs = fileType.split(":");
        for (String str : strs) {
            fileTypeList.add(str);
        }

        value = System.getProperty("notCheckDir");
        if (!StringUtil.isEmpty(value)) {
            notCheckDir = value;
        }
        strs = notCheckDir.split(":");
        for (String str : strs) {
            notCheckDirList.add(str);
        }

        value = System.getProperty("isDealNewFile");
        if (!StringUtil.isEmpty(value)) {
            isDealNewFile = Boolean.parseBoolean(value);
        }

    }

}
