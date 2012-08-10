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
 * ͼƬ��������
 * 
 * @author fenglibin 2011-9-20 ����03:41:10
 */
public class ImageUtil {

    public static BufferedImage InputImage(String srcImg) throws IOException {
        return InputImage(new File(srcImg));
    }

    /**
     * ͼƬ�ļ���ȡ
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
        boolean dispose = Boolean.TRUE;
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
     * ͨ��ָ��ͼƬ������ȣ���ԭͼƬ����ѹ������ʱ�߶Ȱ���Ӧ�ı�������ѹ������ʱ���ָ��������ȴ��ڵ���ԭͼƬ�Ŀ�ȣ��򲻶Ը�ͼƬ����ѹ����
     * 
     * @param srcImg :ԴͼƬ·��
     * @param outImg :�����ѹ��ͼƬ��·
     * @param maxWidth Ŀ��ͼƬ�������
     * @throws IOException
     */
    public static void changeImageSizeWithMaxWidth(String srcImg, String outImg, int maxWidth) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImg);
        changeImageSizeWithMaxWidth(src, outImg, maxWidth);
    }

    /**
     * ͨ��ָ��ͼƬ������ȣ���ԭͼƬ����ѹ������ʱ�߶Ȱ���Ӧ�ı�������ѹ������ʱ���ָ��������ȴ��ڵ���ԭͼƬ�Ŀ�ȣ��򲻶Ը�ͼƬ����ѹ����
     * 
     * @param srcImg :ԴͼƬ
     * @param outImg :�����ѹ��ͼƬ��·
     * @param maxWidth Ŀ��ͼƬ�������
     * @throws IOException
     */
    public static void changeImageSizeWithMaxWidth(BufferedImage src, String outImg, int maxWidth) throws IOException {

        boolean dispose = Boolean.TRUE;
        if (null != src) {
            int old_w = src.getWidth(); // �õ�Դͼ��
            int old_h = src.getHeight(); // �õ�Դͼ��
            int new_w = 0;// ��ͼ�Ŀ�
            int new_h = 0;// ��ͼ�ĳ�
            if (maxWidth < old_w) {// ����ָ��������ȱ���ҪСԭ���Ŀ�ȣ�����Ͳ�ѹ��
                // ͼƬҪ���ŵı���
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
     * ͨ��ָ��ͼƬ�����߶ȣ���ԭͼƬ����ѹ������ʱ��Ȱ���Ӧ�ı�������ѹ������ʱ���ָ�������߶ȴ��ڵ���ԭͼƬ�ĸ߶ȣ��򲻶Ը�ͼƬ����ѹ����
     * 
     * @param srcImg :ԴͼƬ·��
     * @param outImg :�����ѹ��ͼƬ��·
     * @param maxHeight Ŀ��ͼƬ�����߶�
     * @throws IOException
     */
    public static void changeImageSizeWithMaxHeight(String srcImg, String outImg, int maxHeight) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImg);
        changeImageSizeWithMaxHeight(src, outImg, maxHeight);
    }

    /**
     * ͨ��ָ��ͼƬ�����߶ȣ���ԭͼƬ����ѹ������ʱ��Ȱ���Ӧ�ı�������ѹ������ʱ���ָ�������߶ȴ��ڵ���ԭͼƬ�ĸ߶ȣ��򲻶Ը�ͼƬ����ѹ����
     * 
     * @param srcImg :ԴͼƬ
     * @param outImg :�����ѹ��ͼƬ��·
     * @param maxHeight Ŀ��ͼƬ�����߶�
     * @throws IOException
     */
    public static void changeImageSizeWithMaxHeight(BufferedImage src, String outImg, int maxHeight) throws IOException {
        boolean dispose = Boolean.TRUE;
        if (null != src) {
            int old_w = src.getWidth(); // �õ�Դͼ��
            int old_h = src.getHeight(); // �õ�Դͼ��
            int new_w = 0;// ��ͼ�Ŀ�
            int new_h = 0;// ��ͼ�ĳ�
            if (maxHeight < old_h) {// ����ָ��������ȱ���ҪСԭ���Ŀ�ȣ�����Ͳ�ѹ��
                // ͼƬҪ���ŵı���
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

    /**
     * ����ͼƬ�ļ���������ָ���Ŀ�Ⱥ͸߶Ƚ��б���������ԭͼƬ�ļ�
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
     * ����ͼƬ�ļ���������ָ���Ŀ�Ⱥ͸߶Ƚ��б������������µ�Ŀ��ͼƬ�ļ���������ԭͼƬ�ļ�
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
     * ����ͼƬ�ļ���������ָ���Ŀ�Ⱥ͸߶Ƚ��б���������ԭͼƬ�ļ�
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
     * ����ͼƬ�ļ���������ָ���Ŀ�Ⱥ͸߶Ƚ��б������������µ�Ŀ��ͼƬ�ļ���������ԭͼƬ�ļ�
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
        iis.close();
        is.close();
        bi.flush();
    }

    /**
     * ��ͼƬ���ٸ߶ȣ������Ƚϣ�ͼƬ��Ȳ��䣻���ָ���Ĵ����ٸ߶�ֵ���ڵ���ԭͼƬ�ĸ߶�ֵ���򲻽��м��С�<br>
     * �÷������ɵ�Ŀ���ļ���ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceHeight �����ٵĸ߶�ֵ
     * @throws IOException
     */
    public static void reduceImageHeight(File file, int reduceHeight) throws IOException {
        reduceImageHeight(file, reduceHeight, null);
    }

    /**
     * ��ͼƬ���ٸ߶ȣ������Ƚϣ�ͼƬ��Ȳ��䣻���ָ���Ĵ����ٸ߶�ֵ���ڵ���ԭͼƬ�ĸ߶�ֵ���򲻽��м��С�<br>
     * ���Ŀ���ļ���Ϊ�գ��������µ�Ŀ���ļ�������ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceHeight �����ٵĸ߶�ֵ
     * @param descFile ���ٺ����ɵ�Ŀ���ļ�
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
     * ��ͼƬ���ٿ�ȣ������Ƚϣ�ͼƬ�߶Ȳ��䣻���ָ���Ĵ����ٿ��ֵ���ڵ���ԭͼƬ�Ŀ��ֵ���򲻽��м��С�<br>
     * �÷������ɵ�Ŀ���ļ���ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceWeight �����ٵĿ��ֵ
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight) throws IOException {
        reduceImageWidth(file, reduceWeight, null);
    }

    /**
     * ��ͼƬ���ٿ�ȣ������Ƚϣ�ͼƬ�߶Ȳ��䣻���ָ���Ĵ����ٿ��ֵ���ڵ���ԭͼƬ�Ŀ��ֵ���򲻽��м��С�<br>
     * ���Ŀ���ļ���Ϊ�գ��������µ�Ŀ���ļ�������ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceWeight �����ٵĿ��ֵ
     * @param descFile ���ٺ����ɵ�Ŀ���ļ�
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
     * ��ԭͼ���и߶ȺͿ�ȵļ��У�����������������ָ���Ĵ����ٸ߶�ֵ���ڵ���ԭͼƬ�ĸ߶�ֵ���߶Ȳ����м��У����ָ���Ĵ����ٿ��ֵ���ڵ���ԭͼƬ�Ŀ��ֵ����Ȳ����м��С�<br>
     * �÷������ɵ�Ŀ���ļ���ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceWeight �����ٵĸ߶�ֵ
     * @param reduceHeight �����ٵĿ��ֵ
     * @throws IOException
     */
    public static void reduceImageWidth(File file, int reduceWeight, int reduceHeight) throws IOException {
        reduceImageWidth(file, reduceWeight, reduceHeight, null);
    }

    /**
     * ��ԭͼ���и߶ȺͿ�ȵļ��У�����������������ָ���Ĵ����ٸ߶�ֵ���ڵ���ԭͼƬ�ĸ߶�ֵ���߶Ȳ����м��У����ָ���Ĵ����ٿ��ֵ���ڵ���ԭͼƬ�Ŀ��ֵ����Ȳ����м��С�<br>
     * ���Ŀ���ļ���Ϊ�գ��������µ�Ŀ���ļ�������ֱ�Ӹ���Դ�ļ���
     * 
     * @param file
     * @param reduceWeight �����ٵĸ߶�ֵ
     * @param reduceHeight �����ٵĿ��ֵ
     * @param descFile ���ٺ����ɵ�Ŀ���ļ�
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

    /**
     * ����ָ��ԴͼƬĿ¼������Ŀ¼ͼƬ�ļ��Ĵ�СΪָ��������ȣ�ֱ�Ӹ���ԴͼƬ�ļ�
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param maxWidth �������ͼƬ�����
     * @throws IOException
     */
    public static void changeDirImagesWidthSize(String srcImageDir, int maxWidth) throws IOException {
        changeDirImagesWidthSize(srcImageDir, null, maxWidth);
    }

    /**
     * ����ָ��ԴͼƬĿ¼������Ŀ¼ͼƬ�ļ��Ĵ�СΪָ��������ȡ����û��ָ����Ŀ���ļ��У����Ƕ�ԴͼƬ���и��Ǵ������ָ���ˣ��򽫵������ͼƬ�浽Ŀ���ļ�����
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param maxWidth �������ͼƬ�����
     * @throws IOException
     */
    public static void changeDirImagesWidthSize(String srcImageDir, String descImageDir, int maxWidth)
                                                                                                      throws IOException {
        changeDirImagesSize(srcImageDir, descImageDir, maxWidth, 0);
    }

    /**
     * ����ָ��ԴͼƬĿ¼������Ŀ¼ͼƬ�ļ��Ĵ�СΪָ�������߶ȣ�ֱ�Ӹ���ԴͼƬ�ļ�
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param maxHeight �������ͼƬ���߶�
     * @throws IOException
     */
    public static void changeDirImagesHeightSize(String srcImageDir, int maxHeight) throws IOException {
        changeDirImagesHeightSize(srcImageDir, null, maxHeight);
    }

    /**
     * ����ָ��ԴͼƬĿ¼������Ŀ¼ͼƬ�ļ��Ĵ�СΪָ�������߶ȡ����û��ָ����Ŀ���ļ��У����Ƕ�ԴͼƬ���и��Ǵ������ָ���ˣ��򽫵������ͼƬ�浽Ŀ���ļ�����
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param maxHeight �������ͼƬ���߶�
     * @throws IOException
     */
    public static void changeDirImagesHeightSize(String srcImageDir, String descImageDir, int maxHeight)
                                                                                                        throws IOException {
        changeDirImagesSize(srcImageDir, descImageDir, maxHeight, 1);
    }

    /**
     * ����ָ��ԴͼƬĿ¼������Ŀ¼ͼƬ�ļ��Ĵ�СΪָ��������� �� �߶ȣ��������type��ֵ���������Ϊ0��ʾ������ȣ����Ϊ1��ʾ�����߶ȡ�<br>
     * ���û��ָ����Ŀ���ļ��У����Ƕ�ԴͼƬ���и��Ǵ������ָ���ˣ��򽫵������ͼƬ�浽Ŀ���ļ�����
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param maxSize �������ͼƬ����� �� �߶�
     * @param type �������� 0��ʾ������ȣ�1��ʾ�����߶�
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
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// ֻ��������ͼƬ�ļ�
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
     * ����ͼƬ����Ŀ�Ⱥ͸߶���������ͼƬ�����ڿ�ͼ������ȴ��ڸ߶ȵ�ͼ�����ʱ��Ϊ��ͼƬ�����ۣ���ʱ��ͼƬ�����߶Ƚ������ţ���֮�����������ͼƬ��ʾ����<br>
     * �����ʱͼƬ�ĳߴ�Ϊ800*600����ȴ��ڸ߶ȣ���ʱ���ɵ�ͼƬ������Ĳ������߶����߶Ƚ������ţ������ﴫ���maxHeightΪ480�������ɵ�ͼƬӦ��Ϊ��640*480��<br>
     * �����ʱͼƬ�ĳߴ�Ϊ640*1200���߶ȴ��ڿ�ȣ���ʱ���ɵ�ͼƬ������Ĳ�����������߶Ƚ������ţ������ﴫ���maxWidthΪ320�������ɵ�ͼƬӦ��Ϊ��320*600��<br>
     * Ӱ��ָ��ԴĿ¼�����������Ŀ¼
     * 
     * @param srcImageDir ԴͼƬ�ļ���DIR
     * @param maxWidth �����������ɣ�Ŀ��ͼƬ���Ŀ��
     * @param maxHeight ������߶����ɣ�Ŀ��ͼƬ���ĸ߶�
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
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// ֻ��������ͼƬ�ļ�
                changeImageSizeBySmallBetweenWidthHeightForOneFile(file, maxWidth, maxHeight);
            }
        }
    }

    /**
     * ����ͼƬ����Ŀ�Ⱥ͸߶���������ͼƬ�����ڿ�ͼ������ȴ��ڸ߶ȵ�ͼ�����ʱ��Ϊ��ͼƬ�����ۣ���ʱ��ͼƬ�����߶Ƚ������ţ���֮�����������ͼƬ��ʾ����<br>
     * �����ʱͼƬ�ĳߴ�Ϊ800*600����ȴ��ڸ߶ȣ���ʱ���ɵ�ͼƬ������Ĳ������߶����߶Ƚ������ţ������ﴫ���maxHeightΪ480�������ɵ�ͼƬӦ��Ϊ��640*480��<br>
     * �����ʱͼƬ�ĳߴ�Ϊ640*1200���߶ȴ��ڿ�ȣ���ʱ���ɵ�ͼƬ������Ĳ�����������߶Ƚ������ţ������ﴫ���maxWidthΪ320�������ɵ�ͼƬӦ��Ϊ��320*600��
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
     * ����ͼƬ����Ŀ�Ⱥ͸߶���������ͼƬ�����ڿ�ͼ������ȴ��ڸ߶ȵ�ͼ�����ʱ��Ϊ��ͼƬ�����ۣ���ʱ��ͼƬ�����߶Ƚ������ţ���֮�����������ͼƬ��ʾ����<br>
     * �����ʱͼƬ�ĳߴ�Ϊ800*600����ȴ��ڸ߶ȣ���ʱ���ɵ�ͼƬ������Ĳ������߶����߶Ƚ������ţ������ﴫ���maxHeightΪ480�������ɵ�ͼƬӦ��Ϊ��640*480��<br>
     * �����ʱͼƬ�ĳߴ�Ϊ640*1200���߶ȴ��ڿ�ȣ���ʱ���ɵ�ͼƬ������Ĳ�����������߶Ƚ������ţ������ﴫ���maxWidthΪ320�������ɵ�ͼƬӦ��Ϊ��320*600��
     * 
     * @param file ԴͼƬ�ļ�
     * @param maxWidth �����������ɣ�Ŀ��ͼƬ���Ŀ��
     * @param maxHeight ������߶����ɣ�Ŀ��ͼƬ���ĸ߶�
     * @throws IOException
     */
    public static void changeImageSizeBySmallBetweenWidthHeightForOneFile(File file, int maxWidth, int maxHeight)
                                                                                                                 throws IOException {
        if (FileUtil.isImageUsualFileByExt(file.getName())) {// ֻ��������ͼƬ�ļ�
            BufferedImage srcImg = InputImage(file);
            if (null != srcImg) {
                int width = srcImg.getWidth(); // �õ�Դͼ��
                int height = srcImg.getHeight(); // �õ�Դͼ��
                if (width > height && height > maxHeight) {// ��ȴ��ڸ߶ȣ����߶�����
                    changeImageSizeWithMaxHeight(srcImg, file.getAbsolutePath(), maxHeight);
                } else if (height > width && width > maxWidth) {
                    changeImageSizeWithMaxWidth(srcImg, file.getAbsolutePath(), maxWidth);
                }
            }
            srcImg = null;
        }

    }

    /**
     * ��ԴĿ¼�е�ͼƬ�ļ�����ǰ�У���ͼ�ߵ��ұ߿�ʼ������ָ���ĸ߶ȡ�<br>
     * ����Դ�ļ���
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param widthSize �����е�ͼƬ���
     * @throws IOException
     */
    public static void cutDirImagesWidth(String srcImageDir, int widthSize) throws IOException {
        cutDirImagesWidth(srcImageDir, null, widthSize);
    }

    /**
     * ��ԴĿ¼�е�ͼƬ�ļ�����ǰ�У���ͼ�ߵ��ұ߿�ʼ������ָ���ĸ߶ȡ�<br>
     * ���ָ����Ŀ��Ŀ¼�������ɵ��ļ��ŵ�Ŀ��Ŀ¼�У����򸲸�Դ�ļ���
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param widthSize �����е�ͼƬ���
     * @throws IOException
     */
    public static void cutDirImagesWidth(String srcImageDir, String descImageDir, int widthSize) throws IOException {
        cutDirImagesSize(srcImageDir, descImageDir, widthSize, 0);
    }

    /**
     * ��ԴĿ¼�е�ͼƬ�ļ�����ǰ�У���ͼƬ�±߿�ʼ������ָ���ĸ߶ȡ�<br>
     * ����Դ�ļ���
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param heightSize �����е�ͼƬ�߶�
     * @throws IOException
     */
    public static void cutDirImagesHeight(String srcImageDir, int heightSize) throws IOException {
        cutDirImagesHeight(srcImageDir, null, heightSize);
    }

    /**
     * ��ԴĿ¼�е�ͼƬ�ļ�����ǰ�У���ͼƬ�±߿�ʼ������ָ���ĸ߶ȡ�<br>
     * ���ָ����Ŀ��Ŀ¼�������ɵ��ļ��ŵ�Ŀ��Ŀ¼�У����򸲸�Դ�ļ���
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param heightSize �����е�ͼƬ�߶�
     * @throws IOException
     */
    public static void cutDirImagesHeight(String srcImageDir, String descImageDir, int heightSize) throws IOException {
        cutDirImagesSize(srcImageDir, descImageDir, heightSize, 1);
    }

    /**
     * ��ԴĿ¼�е�ͼƬ�ļ�����ǰ�У��������ָ����ȣ�������ұߵ����ݣ����ָ����߶ȣ�����±߿����С�<br>
     * ���ָ����Ŀ��Ŀ¼�������ɵ��ļ��ŵ�Ŀ��Ŀ¼�У����򸲸�Դ�ļ���
     * 
     * @param srcImageDir ԴͼƬ�ļ�Ŀ¼
     * @param descImageDir Ŀ��ͼƬ�ļ�Ŀ¼
     * @param reduceSize �����е�ͼƬ��� �� �߶�
     * @param type �������� 0��ʾ���п�ȣ�1��ʾ���и߶�
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
            } else if (FileUtil.isImageUsualFileByExt(file.getName())) {// ֻ��������ͼƬ�ļ�
                if (type == 0) {
                    ImageUtil.reduceImageWidth(file, reduceSize, descImageDir + File.separator + file.getName());
                } else if (type == 1) {
                    ImageUtil.reduceImageHeight(file, reduceSize, descImageDir + File.separator + file.getName());
                }
            }
        }
    }

    /**
     * ͨ����ȡ�ļ�����ȡ��width��height�ķ�ʽ�����ж��жϵ�ǰ�ļ��Ƿ�ͼƬ������һ�ַǳ��򵥵ķ�ʽ��
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
     * ͨ����ȡ�ļ�����ȡ��width��height�ķ�ʽ�����ж��жϵ�ǰ�ļ��Ƿ�ͼƬ������һ�ַǳ��򵥵ķ�ʽ��
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
