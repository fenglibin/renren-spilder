package test;

import it.renren.spilder.util.ImageUtil;
import java.io.IOException;

public class ImageUtilTest {

    private static String imageDir = "/usr/fenglibin/images/test";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        cutImageSize();
        changeImageSize();
    }

    public static void changeImageSize() throws IOException {
        int maxWidth = 400;
        ImageUtil.changeDirImagesWidthSize(imageDir, null, maxWidth);
    }

    public static void cutImageSize() throws IOException {
        int cutDirImagesHeight = 22;
        ImageUtil.cutDirImagesHeight(imageDir, cutDirImagesHeight);
    }

}
