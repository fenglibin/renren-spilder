package it.renren.spilder.test.image;

import it.renren.spilder.util.FileUtil;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MoveImages {

    @Test
    public void moveTest() throws IOException {
        String imagePath = "C:/gouwuke";
        move(imagePath);
    }

    public void move(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        File[] files = imageFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().equals("output")) {
                    String parentPath = file.getParentFile().getAbsolutePath();
                    File[] subFiles = file.listFiles();
                    for (File subFile : subFiles) {
                        String fileNameLowerCase = subFile.getName().toLowerCase();
                        if (FileUtil.isImageUsualFileByExt(fileNameLowerCase)) {
                            FileUtil.copy(subFile, parentPath + "/" + subFile.getName());
                        }
                    }
                } else {
                    move(file.getAbsolutePath());
                }
            }
        }
    }
}
