package it.renren.spilder.test.image;

import it.renren.spilder.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GetImages {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        // String siteName = "gouwuke";
        String siteName = "yiqifa";
        List<String> list = FileUtil.readFile2List("c:/" + siteName + ".txt");
        for (String imgSrc : list) {
            String imgName = FileUtil.getFileName(imgSrc);
            String path = imgSrc.replace("http://image." + siteName + ".com/", "");
            String storeDir = "c:/gouwuke/" + path.replace(imgName, "");
            File dir = new File(storeDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            System.out.println("获取图片:" + imgSrc);
            boolean result = FileUtil.downloadFile(imgSrc, storeDir);
            if (!result) {
                System.out.println("获取图片:" + imgSrc + " 失败。");
            }
        }

    }
}
