package it.renren.spilder.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片处理工具类
 * 
 * @author fenglibin 2011-9-20 下午03:41:10
 */
public class ImageUtil {

    public static BufferedImage InputImage(String srcImg) throws IOException {
        return InputImage(new File(srcImg));
    }

    /**
     * 图片文件读取
     * 
     * @param srcImg
     * @return
     * @throws IOException
     */
    public static BufferedImage InputImage(File srcImg) throws IOException {
        BufferedImage srcImage = null;
        FileInputStream in = new FileInputStream(srcImg);
        srcImage = javax.imageio.ImageIO.read(in);
        return srcImage;
    }

    /**
     * 指定长或者宽的最大值来压缩图片，无损图片的质量，直接覆盖原文件。
     * 
     * @param srcImg
     * @param maxLength
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, int maxLength) throws IOException {
        changeImageSize(srcImg, srcImg, maxLength);
    }

    /**
     * 指定长或者宽的最大值来压缩图片，无损图片的质量。存为新的图片，如果srcImg与outImg相同，则是将更改后的文件直接覆盖原文件输出。
     * 
     * @param srcImg :源图片路径
     * @param outImg :输出的压缩图片的路径
     * @param maxLength :长或者宽的最大值
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, String outImg, int maxLength) throws IOException {
        // 得到图片
        BufferedImage src = InputImage(srcImg);
        boolean dispose = Boolean.TRUE;
        if (null != src) {
            int old_w = src.getWidth(); // 得到源图宽
            int old_h = src.getHeight(); // 得到源图长
            int new_w = 0;// 新图的宽
            int new_h = 0;// 新图的长
            if (maxLength < old_w || maxLength < old_h) {// 现在指定的最大长或宽必须要小原来的长或宽，否则就不压缩
                // 根据图片尺寸压缩比得到新图的尺寸
                if (old_w > old_h) {
                    // 图片要缩放的比例
                    new_w = maxLength;
                    new_h = (int) Math.round(old_h * ((float) maxLength / old_w));
                } else {
                    new_w = (int) Math.round(old_w * ((float) maxLength / old_h));
                    new_h = maxLength;
                }
            } else {
                new_w = old_w;
                new_h = old_h;
                if (src.equals(outImg)) {
                    dispose = Boolean.FALSE;
                }
            }

            if (dispose) {
                disposeImage(src, outImg, new_w, new_h);
            }

        }
    }

    /**
     * 通过指定图片的最大宽度，对原图片进行压缩，此时高度按相应的比例进行压缩；此时如果指定的最大宽度大于等于原图片的宽度，则不对该图片进行压缩。
     * 
     * @param srcImg :源图片路径
     * @param outImg :输出的压缩图片的路
     * @param maxWidth 目标图片的最大宽度
     * @throws IOException
     */
    public static void changeImageSizeWithMaxWidth(String srcImg, String outImg, int maxWidth) throws IOException {
        // 得到图片
        BufferedImage src = InputImage(srcImg);
        changeImageSizeWithMaxWidth(src, outImg, maxWidth);
    }

    /**
     * 通过指定图片的最大宽度，对原图片进行压缩，此时高度按相应的比例进行压缩；此时如果指定的最大宽度大于等于原图片的宽度，则不对该图片进行压缩。
     * 
     * @param srcImg :源图片
     * @param outImg :输出的压缩图片的路
     * @param maxWidth 目标图片的最大宽度
     * @throws IOException
     */
    public static void changeImageSizeWithMaxWidth(BufferedImage src, String outImg, int maxWidth) throws IOException {

        boolean dispose = Boolean.TRUE;
        if (null != src) {
            int old_w = src.getWidth(); // 得到源图宽
            int old_h = src.getHeight(); // 得到源图长
            int new_w = 0;// 新图的宽
            int new_h = 0;// 新图的长
            if (maxWidth < old_w) {// 现在指定的最大宽度必须要小原来的宽度，否则就不压缩
                // 图片要缩放的比例
                new_w = maxWidth;
                new_h = (int) Math.round(old_h * ((float) maxWidth / old_w));
            } else {
                new_w = old_w;
                new_h = old_h;
                if (src.equals(outImg)) {
                    dispose = Boolean.FALSE;
                }
            }
            if (dispose) {
                disposeImage(src, outImg, new_w, new_h);
            }
        }
    }

    /**
     * 通过指定图片的最大高度，对原图片进行压缩，此时宽度按相应的比例进行压缩；此时如果指定的最大高度大于等于原图片的高度，则不对该图片进行压缩。
     * 
     * @param srcImg :源图片路径
     * @param outImg :输出的压缩图片的路
     * @param maxHeight 目标图片的最大高度
     * @throws IOException
     */
    public static void changeImageSizeWithMaxHeight(String srcImg, String outImg, int maxHeight) throws IOException {
        // 得到图片
        BufferedImage src = InputImage(srcImg);
        changeImageSizeWithMaxHeight(src, outImg, maxHeight);
    }

    /**
     * 通过指定图片的最大高度，对原图片进行压缩，此时宽度按相应的比例进行压缩；此时如果指定的最大高度大于等于原图片的高度，则不对该图片进行压缩。
     * 
     * @param srcImg :源图片
     * @param outImg :输出的压缩图片的路
     * @param maxHeight 目标图片的最大高度
     * @throws IOException
     */
    public static void changeImageSizeWithMaxHeight(BufferedImage src, String outImg, int maxHeight) throws IOException {
        boolean dispose = Boolean.TRUE;
        if (null != src) {
            int old_w = src.getWidth(); // 得到源图宽
            int old_h = src.getHeight(); // 得到源图长
            int new_w = 0;// 新图的宽
            int new_h = 0;// 新图的长
            if (maxHeight < old_h) {// 现在指定的最大宽度必须要小原来的宽度，否则就不压缩
                // 图片要缩放的比例
                new_w = (int) Math.round(old_w * ((float) maxHeight / old_h));
                new_h = maxHeight;
            } else {
                new_w = old_w;
                new_h = old_h;
                if (src.equals(outImg)) {
                    dispose = Boolean.FALSE;
                }
            }
            if (dispose) {
                disposeImage(src, outImg, new_w, new_h);
            }

        }
    }

    /**
     * 将图片按照指定的图片尺寸压缩，无损图片的质量
     * 
     * @param srcImg :源图片路径
     * @param outImg :输出的压缩图片的路径
     * @param new_w :压缩后的图片宽
     * @param new_h :压缩后的图片高
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, String outImg, int new_w, int new_h) throws IOException {
        // 得到图片
        BufferedImage src = InputImage(srcImg);
        disposeImage(src, outImg, new_w, new_h);
    }

    /**
     * 处理图片
     * 
     * @param src
     * @param outImg
     * @param new_w
     * @param new_h
     * @throws IOException
     */
    private synchronized static void disposeImage(BufferedImage src, String outImg, int new_w, int new_h)
                                                                                                         throws IOException {
        // 得到图片
        int old_w = src.getWidth(); // 得到源图宽
        int old_h = src.getHeight(); // 得到源图长

        BufferedImage newImg = null;

        // 判断输入图片的类型
        switch (src.getType()) {
            case 13:// png,gif
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);
                break;

            default:
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);

                break;
        }

        Graphics2D g = newImg.createGraphics();
        // 从原图上取颜色绘制新图
        g.drawImage(src, 0, 0, old_w, old_h, null);
        g.dispose();
        // 根据图片尺寸压缩比得到新图的尺寸
        newImg.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);

        // 调用方法输出图片文件
        OutImage(outImg, newImg);
    }

    /**
     * 将图片文件输出到指定的路径，并可设定压缩质量
     * 
     * @param outImg
     * @param newImg
     * @param per
     * @throws IOException
     */
    private static void OutImage(String outImg, BufferedImage newImg) throws IOException {
        // 判断输出的文件夹路径是否存在，不存在则创建
        File file = new File(outImg);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 输出到文件流
        try {
            ImageIO.write(newImg, outImg.substring(outImg.lastIndexOf(".") + 1), new File(outImg));
        } finally {
            outImg = null;
            newImg = null;
        }
    }

    /**
     * 剪切图片文件，并根据指定的宽度和高度进行保留，覆盖原图片文件
     * 
     * @param srcFile
     * @param width
     * @param height
     * @throws IOException
     */
    public static void cutImgFile(String srcFile, int width, int height) throws IOException {
        cutImgFile(new File(srcFile), 0, 0, width, height, null);
    }

    /**
     * 剪切图片文件，并根据指定的宽度和高度进行保留，并生成新的目标图片文件，不覆盖原图片文件
     * 
     * @param srcFile
     * @param width
     * @param height
     * @param descFile
     * @throws IOException
     */
    public static void cutImgFile(String srcFile, int width, int height, String descFile) throws IOException {
        cutImgFile(new File(srcFile), 0, 0, width, height, descFile);
    }

    /**
     * 剪切图片文件，并根据指定的宽度和高度进行保留，覆盖原图片文件
     * 
     * @param file
     * @param width
     * @param height
     * @throws IOException
     */
    public static void cutImgFile(File file, int width, int height) throws IOException {
        cutImgFile(file, 0, 0, width, height, null);
    }

    /**
     * 剪切图片文件，并根据指定的宽度和高度进行保留，并生成新的目标图片文件，不覆盖原图片文件
     * 
     * @param file
     * @param width
     * @param height
     * @param descFile
     * @throws IOException
     */
    public static void cutImgFile(File file, int width, int height, String descFile) throws IOException {
        cutImgFile(file, 0, 0, width, height, descFile);
    }

    /**
     * 针对当前图片进行剪切并输出剪切后的图片，不按比例
     * 
     * @param file 被剪切的文件
     * @param x 开始剪切的x坐标
     * @param y 开始剪切的y坐标
     * @param width 目标图片的宽度
     * @param height 目标图片的高度
     * @param descFile 目标输出文件，如果为空，则直接覆盖原文件
     * @throws IOException
     */
    public static void cutImgFile(File file, int x, int y, int width, int height, String descFile) throws IOException {
        String endName = file.getName();
        endName = endName.substring(endName.lastIndexOf(".") + 1);
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(endName);
        ImageReader reader = (ImageReader) readers.next();
        InputStream is = new FileInputStream(file);
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        if (descFile == null) {
            descFile = file.getAbsolutePath();
        }
        ImageOutputStream out = ImageIO.createImageOutputStream(new FileOutputStream(descFile));
        ImageIO.write(bi, endName, out);
        out.close();
        iis.close();
        is.close();
        bi.flush();
    }

    /**
     * 给图片减少高度，不按比较，图片宽度不变；如果指定的待减少高度值大于等于原图片的高度值，则不进行剪切。<br>
     * 该方法生成的目标文件会直接覆盖源文件。
     * 
     * @param file
     * @param reduceHeight 待减少的高度值
     * @throws IOException
     */
    public static void reduceImageHeight(File file, int reduceHeight) throws IOException {
        reduceImageHeight(file, reduceHeight, null);
    }

    /**
     * 给图片减少高度，不按比较，图片宽度不变；如果指定的待减少高度值大于等于原图片的高度值，则不进行剪切。<br>
     * 如果目标文件不为空，则生成新的目标文件，否则直接覆盖源文件。
     * 
     * @param file
     * @param reduceHeight 待减少的高度值
     * @param descFile 减少后生成的目标文件
     * @throws IOException
     */
    public static void reduceImageHeight(File file, int reduceHeight, String descFile) throws IOException {
        BufferedImage src = InputImage(file.getAbsolutePath());
        int width = src.getWidth();
        int height = src.getHeight();
        src = null;
        if (reduceHeight >= height) {
            return;
        }
        cutImgFile(file, width, height - reduceHeight, descFile);
    }

    /**
     * 给图片减少宽度，不按比较，图片高度不变；如果指定的待减少宽度值大于等于原图片的宽度值，则不进行剪切。<br>
     * 该方法生成的目标文件会直接覆盖源文件。
     * 
     * @param file
     * @param reduceWeight 待减少的宽度值
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight) throws IOException {
        reduceImageWidth(file, reduceWeight, null);
    }

    /**
     * 给图片减少宽度，不按比较，图片高度不变；如果指定的待减少宽度值大于等于原图片的宽度值，则不进行剪切。<br>
     * 如果目标文件不为空，则生成新的目标文件，否则直接覆盖源文件。
     * 
     * @param file
     * @param reduceWeight 待减少的宽度值
     * @param descFile 减少后生成的目标文件
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight, String descFile) throws IOException {
        BufferedImage src = InputImage(file.getAbsolutePath());
        int width = src.getWidth();
        int height = src.getHeight();
        src = null;
        if (reduceWeight >= width) {
            return;
        }
        cutImgFile(file, width - reduceWeight, height, descFile);
    }

    /**
     * 对原图进行高度和宽度的剪切，不按比例输出；如果指定的待减少高度值大于等于原图片的高度值，高度不进行剪切，如果指定的待减少宽度值大于等于原图片的宽度值，宽度不进行剪切。<br>
     * 该方法生成的目标文件会直接覆盖源文件。
     * 
     * @param file
     * @param reduceWeight 待减少的高度值
     * @param reduceHeight 待减少的宽度值
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight, int reduceHeight) throws IOException {
        reduceImageWidth(file, reduceWeight, reduceHeight, null);
    }

    /**
     * 对原图进行高度和宽度的剪切，不按比例输出；如果指定的待减少高度值大于等于原图片的高度值，高度不进行剪切，如果指定的待减少宽度值大于等于原图片的宽度值，宽度不进行剪切。<br>
     * 如果目标文件不为空，则生成新的目标文件，否则直接覆盖源文件。
     * 
     * @param file
     * @param reduceWeight 待减少的高度值
     * @param reduceHeight 待减少的宽度值
     * @param descFile 减少后生成的目标文件
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight, int reduceHeight, String descFile)
                                                                                                       throws IOException {
        BufferedImage src = InputImage(file.getAbsolutePath());
        int width = src.getWidth();
        int height = src.getHeight();
        src = null;
        if (reduceWeight >= width) {
            reduceWeight = 0;
        }
        if (reduceHeight >= height) {
            reduceHeight = 0;
        }
        if (reduceWeight == 0 && reduceHeight == 0) {
            return;
        }
        cutImgFile(file, width - reduceWeight, height - reduceHeight);
    }

    /**
     * 根据指定水印位置，在原图上增加水印
     * 
     * @param srcImg 源图片
     * @param waterImg 水印图片
     * @param location 水印位置，现在只支持左下角
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @throws IOException
     */
    public final static void addWaterMark(String srcImg, String waterImg, WaterImageLocation location, float alpha)
                                                                                                                   throws IOException {
        int x = 0, y = 0;
        // 得到图片
        BufferedImage src = InputImage(srcImg);
        BufferedImage water = InputImage(waterImg);
        if (location.equals(WaterImageLocation.LEFT_FOOT)) {
            y = src.getHeight() - water.getHeight();
            y = y < 0 ? 0 : y;
        }
        water = null;
        src = null;
        addWaterMark(srcImg, waterImg, x, y, alpha);
    }

    /**
     * 添加图片水印
     * 
     * @param srcImg 目标图片路径，如：C:\\kutuku.jpg
     * @param waterImg 水印图片路径，如：C:\\kutuku.png
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @throws IOException
     */
    public final static void addWaterMark(String srcImg, String waterImg, int x, int y, float alpha) throws IOException {
        // 加载目标图片
        File file = new File(srcImg);
        String ext = srcImg.substring(srcImg.lastIndexOf(".") + 1);
        Image image = ImageIO.read(file);
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        // 将目标图片加载到内存。
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);

        // 加载水印图片。
        Image waterImage = ImageIO.read(new File(waterImg));
        int width_1 = waterImage.getWidth(null);
        int height_1 = waterImage.getHeight(null);
        // 设置水印图片的透明度。
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

        // 设置水印图片的位置。
        int widthDiff = width - width_1;
        int heightDiff = height - height_1;
        if (x < 0) {
            x = widthDiff / 2;
        } else if (x > widthDiff) {
            x = widthDiff;
        }
        if (y < 0) {
            y = heightDiff / 2;
        } else if (y > heightDiff) {
            y = heightDiff;
        }

        // 将水印图片“画”在原有的图片的制定位置。
        g.drawImage(waterImage, x, y, width_1, height_1, null);
        // 关闭画笔。
        g.dispose();

        // 保存目标图片。
        ImageIO.write(bufferedImage, ext, file);
    }

    /**
     * 类ImageUtil.java的实现描述：水印图片的位置，暂时只实现了左下角
     * 
     * @author fenglibin 2011-10-1 下午04:36:05
     */
    public static enum WaterImageLocation {
        // LEFT_TOP, TOP_CENTER, RIGHT_TOP, MID_CENTER, RIGHT_FOOT,
        LEFT_FOOT
    }

    /**
     * 调整指定源图片目录及其子目录图片文件的大小为指定的最大宽度，直接覆盖源图片文件
     * 
     * @param srcImageDir 源图片文件目录
     * @param maxWidth 调整后的图片最大宽度
     * @throws IOException
     */
    public static void changeDirImagesWidthSize(String srcImageDir, int maxWidth) throws IOException {
        changeDirImagesWidthSize(srcImageDir, null, maxWidth);
    }

    /**
     * 调整指定源图片目录及其子目录图片文件的大小为指定的最大宽度。如果没有指定了目标文件夹，则是对源图片进行覆盖处理，如果指定了，则将调整后的图片存到目标文件夹中
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param maxWidth 调整后的图片最大宽度
     * @throws IOException
     */
    public static void changeDirImagesWidthSize(String srcImageDir, String descImageDir, int maxWidth)
                                                                                                      throws IOException {
        changeDirImagesSize(srcImageDir, descImageDir, maxWidth, 0);
    }

    /**
     * 调整指定源图片目录及其子目录图片文件的大小为指定的最大高度，直接覆盖源图片文件
     * 
     * @param srcImageDir 源图片文件目录
     * @param maxHeight 调整后的图片最大高度
     * @throws IOException
     */
    public static void changeDirImagesHeightSize(String srcImageDir, int maxHeight) throws IOException {
        changeDirImagesHeightSize(srcImageDir, null, maxHeight);
    }

    /**
     * 调整指定源图片目录及其子目录图片文件的大小为指定的最大高度。如果没有指定了目标文件夹，则是对源图片进行覆盖处理，如果指定了，则将调整后的图片存到目标文件夹中
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param maxHeight 调整后的图片最大高度
     * @throws IOException
     */
    public static void changeDirImagesHeightSize(String srcImageDir, String descImageDir, int maxHeight)
                                                                                                        throws IOException {
        changeDirImagesSize(srcImageDir, descImageDir, maxHeight, 1);
    }

    /**
     * 调整指定源图片目录及其子目录图片文件的大小为指定的最大宽度 或 高度，这里根据type的值而定，如果为0表示调整宽度，如果为1表示调整高度。<br>
     * 如果没有指定了目标文件夹，则是对源图片进行覆盖处理，如果指定了，则将调整后的图片存到目标文件夹中
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param maxSize 调整后的图片最大宽度 或 高度
     * @param type 调整类型 0表示调整宽度，1表示调整高度
     * @throws IOException
     */
    public static void changeDirImagesSize(String srcImageDir, String descImageDir, int maxSize, int type)
                                                                                                          throws IOException {
        File srcImageDirFile = new File(srcImageDir);
        File[] files = srcImageDirFile.listFiles();
        for (File file : files) {
            if (StringUtil.isEmpty(descImageDir)) {
                descImageDir = srcImageDir;
            }
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    changeDirImagesSize(file.getAbsolutePath(),
                                        file.getAbsolutePath().replace(srcImageDir, descImageDir), maxSize, type);
                }
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// 只处理常见的图片文件
                if (type == 0) {
                    changeImageSizeWithMaxWidth(srcImageDir + File.separator + file.getName(),
                                                descImageDir + File.separator + file.getName(), maxSize);
                } else if (type == 1) {

                    changeImageSizeWithMaxHeight(srcImageDir + File.separator + file.getName(),
                                                 descImageDir + File.separator + file.getName(), maxSize);
                }
            }
        }
    }

    /**
     * 根据图片自身的宽度和高度情况，如果图片是属于宽图，即宽度大于高度的图，这个时候为了图片的美观，此时按图片的最大高度进行缩放；反之则按最大宽度缩放图片。示例：<br>
     * 如果此时图片的尺寸为800*600，宽度大于高度，此时生成的图片按传入的参数最大高度最大高度进行缩放，如这里传入的maxHeight为480，则生成的图片应该为：640*480；<br>
     * 如果此时图片的尺寸为640*1200，高度大于宽度，此时生成的图片按传入的参数最大宽度最大高度进行缩放，如这里传入的maxWidth为320，则生成的图片应该为：320*600；<br>
     * 影响指定源目录下面的所有子目录
     * 
     * @param srcImageDir 源图片文件的DIR
     * @param maxWidth 如果按宽度生成，目标图片最大的宽度
     * @param maxHeight 如果按高度生成，目标图片最大的高度
     * @throws IOException
     */
    public static void changeImageSizeBySmallBetweenWidthHeight(String srcImageDir, int maxWidth, int maxHeight)
                                                                                                                throws IOException {
        File srcImageDirFile = new File(srcImageDir);
        File[] files = srcImageDirFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    changeImageSizeBySmallBetweenWidthHeight(file.getAbsolutePath(), maxWidth, maxHeight);
                }
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// 只处理常见的图片文件
                changeImageSizeBySmallBetweenWidthHeightForOneFile(file, maxWidth, maxHeight);
            }
        }
    }

    /**
     * 根据图片自身的宽度和高度情况，如果图片是属于宽图，即宽度大于高度的图，这个时候为了图片的美观，此时按图片的最大高度进行缩放；反之则按最大宽度缩放图片。示例：<br>
     * 如果此时图片的尺寸为800*600，宽度大于高度，此时生成的图片按传入的参数最大高度最大高度进行缩放，如这里传入的maxHeight为480，则生成的图片应该为：640*480；<br>
     * 如果此时图片的尺寸为640*1200，高度大于宽度，此时生成的图片按传入的参数最大宽度最大高度进行缩放，如这里传入的maxWidth为320，则生成的图片应该为：320*600；
     * 
     * @param srcImageDir
     * @param maxWidth
     * @param maxHeight
     * @throws IOException
     */
    public static void changeImageSizeBySmallBetweenWidthHeightForOneFile(String file, int maxWidth, int maxHeight)
                                                                                                                   throws IOException {
        changeImageSizeBySmallBetweenWidthHeightForOneFile(new File(file), maxWidth, maxHeight);
    }

    /**
     * 根据图片自身的宽度和高度情况，如果图片是属于宽图，即宽度大于高度的图，这个时候为了图片的美观，此时按图片的最大高度进行缩放；反之则按最大宽度缩放图片。示例：<br>
     * 如果此时图片的尺寸为800*600，宽度大于高度，此时生成的图片按传入的参数最大高度最大高度进行缩放，如这里传入的maxHeight为480，则生成的图片应该为：640*480；<br>
     * 如果此时图片的尺寸为640*1200，高度大于宽度，此时生成的图片按传入的参数最大宽度最大高度进行缩放，如这里传入的maxWidth为320，则生成的图片应该为：320*600；
     * 
     * @param file 源图片文件
     * @param maxWidth 如果按宽度生成，目标图片最大的宽度
     * @param maxHeight 如果按高度生成，目标图片最大的高度
     * @throws IOException
     */
    public static void changeImageSizeBySmallBetweenWidthHeightForOneFile(File file, int maxWidth, int maxHeight)
                                                                                                                 throws IOException {
        if (FileUtil.isImageUsualFileByExt(file.getName())) {// 只处理常见的图片文件
            BufferedImage srcImg = InputImage(file);
            if (null != srcImg) {
                int width = srcImg.getWidth(); // 得到源图宽
                int height = srcImg.getHeight(); // 得到源图长
                if (width > height && height > maxHeight) {// 宽度大于高度，按高度生成
                    changeImageSizeWithMaxHeight(srcImg, file.getAbsolutePath(), maxHeight);
                } else if (height > width && width > maxWidth) {
                    changeImageSizeWithMaxWidth(srcImg, file.getAbsolutePath(), maxWidth);
                }
            }
            srcImg = null;
        }

    }

    /**
     * 对源目录中的图片文件进行前切，从图边的右边开始，剪切指定的高度。<br>
     * 覆盖源文件。
     * 
     * @param srcImageDir 源图片文件目录
     * @param widthSize 待剪切的图片宽度
     * @throws IOException
     */
    public static void cutDirImagesWidth(String srcImageDir, int widthSize) throws IOException {
        cutDirImagesWidth(srcImageDir, null, widthSize);
    }

    /**
     * 对源目录中的图片文件进行前切，从图边的右边开始，剪切指定的高度。<br>
     * 如果指定了目标目录，则生成的文件放到目标目录中，否则覆盖源文件。
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param widthSize 待剪切的图片宽度
     * @throws IOException
     */
    public static void cutDirImagesWidth(String srcImageDir, String descImageDir, int widthSize) throws IOException {
        cutDirImagesSize(srcImageDir, descImageDir, widthSize, 0);
    }

    /**
     * 对源目录中的图片文件进行前切，从图片下边开始，剪切指定的高度。<br>
     * 覆盖源文件。
     * 
     * @param srcImageDir 源图片文件目录
     * @param heightSize 待剪切的图片高度
     * @throws IOException
     */
    public static void cutDirImagesHeight(String srcImageDir, int heightSize) throws IOException {
        cutDirImagesHeight(srcImageDir, null, heightSize);
    }

    /**
     * 对源目录中的图片文件进行前切，从图片下边开始，剪切指定的高度。<br>
     * 如果指定了目标目录，则生成的文件放到目标目录中，否则覆盖源文件。
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param heightSize 待剪切的图片高度
     * @throws IOException
     */
    public static void cutDirImagesHeight(String srcImageDir, String descImageDir, int heightSize) throws IOException {
        cutDirImagesSize(srcImageDir, descImageDir, heightSize, 1);
    }

    /**
     * 对源目录中的图片文件进行前切，如果类型指定宽度，则剪切右边的内容，如果指定提高度，则从下边开剪切。<br>
     * 如果指定了目标目录，则生成的文件放到目标目录中，否则覆盖源文件。
     * 
     * @param srcImageDir 源图片文件目录
     * @param descImageDir 目标图片文件目录
     * @param reduceSize 待剪切的图片宽度 或 高度
     * @param type 剪切类型 0表示剪切宽度，1表示剪切高度
     * @throws IOException
     */
    public static void cutDirImagesSize(String srcImageDir, String descImageDir, int reduceSize, int type)
                                                                                                          throws IOException {
        File srcImageDirFile = new File(srcImageDir);
        File[] files = srcImageDirFile.listFiles();
        for (File file : files) {
            if (StringUtil.isEmpty(descImageDir)) {
                descImageDir = srcImageDir;
            }
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    cutDirImagesSize(file.getAbsolutePath(), file.getAbsolutePath().replace(srcImageDir, descImageDir),
                                     reduceSize, type);
                }
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// 只处理常见的图片文件
                if (type == 0) {
                    ImageUtil.reduceImageWidth(file, reduceSize, descImageDir + File.separator + file.getName());
                } else if (type == 1) {
                    ImageUtil.reduceImageHeight(file, reduceSize, descImageDir + File.separator + file.getName());
                }
            }
        }
    }

    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     * 
     * @param imagePath
     * @return
     */
    public static boolean isImage(String imagePath) {
        if (StringUtil.isEmpty(imagePath)) {
            return false;
        }
        File image = new File(imagePath);
        return isImage(image);
    }

    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     * 
     * @param imageFile
     * @return
     */
    public static boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        String imagePath = "D:/test/b385243e-3cc8-39a3-86b7-62dc62e780e8.jpg";
        imagePath = "D:/test/test.bmp";
        imagePath = "c:/favicon.png";
        // System.out.println(isImage(imagePath));
        File image = new File(imagePath);
        InputStream is = new FileInputStream(image);
        byte[] bt = new byte[3];
        is.read(bt);
        System.out.println(bytesToHexString(bt));
    }
}
