package it.renren.spilder.util;

import it.renren.spilder.task.WriteData2FanDB;
import it.renren.spilder.util.log.Log4j;

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

    private static Log4j log4j = new Log4j(WriteData2FanDB.class.getName());

    public static void main(String[] args) throws IOException {
        String src = "/usr/fenglibin/renren-spilder/allimg/temp/FD113033211.jpg";
        String water = "/usr/fenglibin/renren-spilder/allimg/temp/login_logo.gif";
        String desc = "/home/fenglibin/tmp/img/2010112712190344343201-lp_1.jpg";
        String dirSrc = "/usr/fenglibin/renren-spilder/allimg/temp/FD113033211.jpg";
        File dir = new File(dirSrc);
        File[] fileList = dir.listFiles();
        // for (File file : fileList) {
        // reduceImageHeight(file, 26);
        // }
        addWaterMark(src, water, WaterImageLocation.LEFT_FOOT, 1);

    }

    /**
     * 图片文件读取
     * 
     * @param srcImg
     * @return
     * @throws IOException
     */
    private static BufferedImage InputImage(String srcImg) throws IOException {
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
            }

            disposeImage(src, outImg, new_w, new_h);
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

    public static void cutImgFile(String srcFile, int width, int height) throws IOException {
        cutImgFile(new File(srcFile), 0, 0, width, height, null);
    }

    public static void cutImgFile(String srcFile, int width, int height, String descFile) throws IOException {
        cutImgFile(new File(srcFile), 0, 0, width, height, descFile);
    }

    public static void cutImgFile(File file, int width, int height) throws IOException {
        cutImgFile(file, 0, 0, width, height, null);
    }

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
        bi.flush();
    }

    /**
     * 给图片减少高度，不按比较，图片宽度不变
     * 
     * @param file
     * @param reduceHeight 待减少的高度值
     * @throws IOException
     */
    public static void reduceImageHeight(File file, int reduceHeight) throws IOException {
        BufferedImage src = InputImage(file.getAbsolutePath());
        int width = src.getWidth();
        int height = src.getHeight();
        src = null;
        cutImgFile(file, width, height - reduceHeight);
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
}
