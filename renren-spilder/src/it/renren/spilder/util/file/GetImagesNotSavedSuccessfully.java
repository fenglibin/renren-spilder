package it.renren.spilder.util.file;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.UrlUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 根据给定的文件，获取未成功获取的图片文件
 * 
 * @author Administrator 2012-8-10 下午11:25:04
 */
public class GetImagesNotSavedSuccessfully {

    private static String filePath = "notGetImagesUrlSaveFile.txt";

    public static void main(String args[]) throws IOException {
        String file = getFilePath();
        FileReader fr = new FileReader(new File(file));
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        String path = "";
        while (line != null) {
            if (line.startsWith("#")) {
                path = line.split("===")[1];
                new File(path).mkdirs();
            }
            if (line.startsWith("wget")) {
                line = line.split(" ")[1];
                try {
                    String imageName = FileUtil.getFileName(line);
                    if (!new File(path + imageName).exists()) {
                        FileUtil.downloadFile(line, path);
                        UrlUtil.getLitPicName(path, imageName);
                    }
                } catch (Exception e) {
                    System.out.println(line);
                }
            }
            line = br.readLine();
        }
        br.close();
        fr.close();
    }

    private static String getFilePath() {
        if (System.getProperty("filePath") != null) {
            filePath = System.getProperty("filePath");
        }
        return filePath;
    }
}
