package it.renren.spilder.util.other.lifegirl;

import it.renren.spilder.util.ImageUtil;
import java.io.IOException;

public class ImageUtilTest {

    private static String imageDir = "/usr/fenglibin/images/lifegirl/index_icon";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //cutImageSize();
        changeImageSize();
    }

    public static void changeImageSize() throws IOException {
        int maxWidth = 150;
        ImageUtil.changeDirImagesWidthSize(imageDir, null, maxWidth);
    }

    public static void cutImageSize() throws IOException {
        int cutDirImagesHeight = 22;
        ImageUtil.cutDirImagesHeight(imageDir, cutDirImagesHeight);
    }

}