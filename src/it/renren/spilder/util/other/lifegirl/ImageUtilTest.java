package it.renren.spilder.util.other.lifegirl;

import it.renren.spilder.util.ImageUtil;
import java.io.IOException;

public class ImageUtilTest {

    private static String imageDir = "/usr/fenglibin/renren-spilder/allimg/2011-11-05";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        cutImageSize();
        changeImageSizeBySmallBetweenWidthHeight();
        // changeImageSize();
    }

    public static void changeImageSize() throws IOException {
        int maxWidth = 150;
        ImageUtil.changeDirImagesWidthSize(imageDir, null, maxWidth);
    }

    public static void changeImageSizeBySmallBetweenWidthHeight() throws IOException {
        int maxWidth = 320;
        int maxHeight = 480;
        ImageUtil.changeImageSizeBySmallBetweenWidthHeight(imageDir, maxWidth, maxHeight);
    }

    public static void cutImageSize() throws IOException {
        int cutDirImagesHeight = 22;
        ImageUtil.cutDirImagesHeight(imageDir, cutDirImagesHeight);
    }

}
