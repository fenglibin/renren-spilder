package it.renren.spilder.test;

import java.io.File;

public class RenameFile {

    private static void doDir(String dir) throws Exception {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doDir(file.getAbsolutePath());
            } else {
                doFile(file);
            }
        }
    }

    private static void doFile(File file) throws Exception {
        String fileName = file.getName();
        String path = file.getParentFile().getAbsolutePath();
        fileName = fileName.substring(0, fileName.length() / 2);
        String desc = path + File.separator + fileName;
        file.renameTo(new File(desc));
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        String dir = args[0].trim();
        doDir(dir);
    }

}
