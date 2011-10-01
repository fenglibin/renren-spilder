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
 * ͼƬ��������
 * 
 * @author fenglibin 2011-9-20 ����03:41:10
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
     * ͼƬ�ļ���ȡ
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
     * ָ�������߿�����ֵ��ѹ��ͼƬ������ͼƬ��������ֱ�Ӹ���ԭ�ļ���
     * 
     * @param srcImg
     * @param maxLength
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, int maxLength) throws IOException {
        changeImageSize(srcImg, srcImg, maxLength);
    }

    /**
     * ָ�������߿�����ֵ��ѹ��ͼƬ������ͼƬ����������Ϊ�µ�ͼƬ�����srcImg��outImg��ͬ�����ǽ����ĺ���ļ�ֱ�Ӹ���ԭ�ļ������
     * 
     * @param srcImg :ԴͼƬ·��
     * @param outImg :�����ѹ��ͼƬ��·��
     * @param maxLength :�����߿�����ֵ
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, String outImg, int maxLength) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImg);
        if (null != src) {
            int old_w = src.getWidth(); // �õ�Դͼ��
            int old_h = src.getHeight(); // �õ�Դͼ��
            int new_w = 0;// ��ͼ�Ŀ�
            int new_h = 0;// ��ͼ�ĳ�
            if (maxLength < old_w || maxLength < old_h) {// ����ָ������󳤻�����ҪСԭ���ĳ��������Ͳ�ѹ��
                // ����ͼƬ�ߴ�ѹ���ȵõ���ͼ�ĳߴ�
                if (old_w > old_h) {
                    // ͼƬҪ���ŵı���
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
     * ��ͼƬ����ָ����ͼƬ�ߴ�ѹ��������ͼƬ������
     * 
     * @param srcImg :ԴͼƬ·��
     * @param outImg :�����ѹ��ͼƬ��·��
     * @param new_w :ѹ�����ͼƬ��
     * @param new_h :ѹ�����ͼƬ��
     * @throws IOException
     */
    public static void changeImageSize(String srcImg, String outImg, int new_w, int new_h) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImg);
        disposeImage(src, outImg, new_w, new_h);
    }

    /**
     * ����ͼƬ
     * 
     * @param src
     * @param outImg
     * @param new_w
     * @param new_h
     * @throws IOException
     */
    private synchronized static void disposeImage(BufferedImage src, String outImg, int new_w, int new_h)
                                                                                                         throws IOException {
        // �õ�ͼƬ
        int old_w = src.getWidth(); // �õ�Դͼ��
        int old_h = src.getHeight(); // �õ�Դͼ��

        BufferedImage newImg = null;

        // �ж�����ͼƬ������
        switch (src.getType()) {
            case 13:// png,gif
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);
                break;

            default:
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);

                break;
        }

        Graphics2D g = newImg.createGraphics();
        // ��ԭͼ��ȡ��ɫ������ͼ
        g.drawImage(src, 0, 0, old_w, old_h, null);
        g.dispose();
        // ����ͼƬ�ߴ�ѹ���ȵõ���ͼ�ĳߴ�
        newImg.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);

        // ���÷������ͼƬ�ļ�
        OutImage(outImg, newImg);
    }

    /**
     * ��ͼƬ�ļ������ָ����·���������趨ѹ������
     * 
     * @param outImg
     * @param newImg
     * @param per
     * @throws IOException
     */
    private static void OutImage(String outImg, BufferedImage newImg) throws IOException {
        // �ж�������ļ���·���Ƿ���ڣ��������򴴽�
        File file = new File(outImg);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // ������ļ���
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
     * ��Ե�ǰͼƬ���м��в�������к��ͼƬ����������
     * 
     * @param file �����е��ļ�
     * @param x ��ʼ���е�x����
     * @param y ��ʼ���е�y����
     * @param width Ŀ��ͼƬ�Ŀ��
     * @param height Ŀ��ͼƬ�ĸ߶�
     * @param descFile Ŀ������ļ������Ϊ�գ���ֱ�Ӹ���ԭ�ļ�
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
     * ��ͼƬ���ٸ߶ȣ������Ƚϣ�ͼƬ��Ȳ���
     * 
     * @param file
     * @param reduceHeight �����ٵĸ߶�ֵ
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
     * ����ָ��ˮӡλ�ã���ԭͼ������ˮӡ
     * 
     * @param srcImg ԴͼƬ
     * @param waterImg ˮӡͼƬ
     * @param location ˮӡλ�ã�����ֻ֧�����½�
     * @param alpha ͸����(0.0 -- 1.0, 0.0Ϊ��ȫ͸����1.0Ϊ��ȫ��͸��)
     * @throws IOException
     */
    public final static void addWaterMark(String srcImg, String waterImg, WaterImageLocation location, float alpha)
                                                                                                                   throws IOException {
        int x = 0, y = 0;
        // �õ�ͼƬ
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
     * ���ͼƬˮӡ
     * 
     * @param srcImg Ŀ��ͼƬ·�����磺C:\\kutuku.jpg
     * @param waterImg ˮӡͼƬ·�����磺C:\\kutuku.png
     * @param x ˮӡͼƬ����Ŀ��ͼƬ����ƫ���������x<0, �������м�
     * @param y ˮӡͼƬ����Ŀ��ͼƬ�ϲ��ƫ���������y<0, �������м�
     * @param alpha ͸����(0.0 -- 1.0, 0.0Ϊ��ȫ͸����1.0Ϊ��ȫ��͸��)
     * @throws IOException
     */
    public final static void addWaterMark(String srcImg, String waterImg, int x, int y, float alpha) throws IOException {
        // ����Ŀ��ͼƬ
        File file = new File(srcImg);
        String ext = srcImg.substring(srcImg.lastIndexOf(".") + 1);
        Image image = ImageIO.read(file);
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        // ��Ŀ��ͼƬ���ص��ڴ档
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);

        // ����ˮӡͼƬ��
        Image waterImage = ImageIO.read(new File(waterImg));
        int width_1 = waterImage.getWidth(null);
        int height_1 = waterImage.getHeight(null);
        // ����ˮӡͼƬ��͸���ȡ�
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

        // ����ˮӡͼƬ��λ�á�
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

        // ��ˮӡͼƬ��������ԭ�е�ͼƬ���ƶ�λ�á�
        g.drawImage(waterImage, x, y, width_1, height_1, null);
        // �رջ��ʡ�
        g.dispose();

        // ����Ŀ��ͼƬ��
        ImageIO.write(bufferedImage, ext, file);
    }

    /**
     * ��ImageUtil.java��ʵ��������ˮӡͼƬ��λ�ã���ʱֻʵ�������½�
     * 
     * @author fenglibin 2011-10-1 ����04:36:05
     */
    public static enum WaterImageLocation {
        // LEFT_TOP, TOP_CENTER, RIGHT_TOP, MID_CENTER, RIGHT_FOOT,
        LEFT_FOOT
    }
}
