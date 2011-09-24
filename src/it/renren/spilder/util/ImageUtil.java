package it.renren.spilder.util;

import it.renren.spilder.task.WriteData2FanDB;
import it.renren.spilder.util.log.Log4j;

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
        String src = "/home/fenglibin/tmp/img/2010112712190344343201-lp.jpg";
        String desc = "/home/fenglibin/tmp/img/2010112712190344343201-lp_1.jpg";
        String dirSrc = "/usr/fenglibin/renren-spilder/allimg/lp";
        File dir = new File(dirSrc);
        File[] fileList = dir.listFiles();
        for (File file : fileList) {
            reduceImageHeight(file, 26);
        }
    }

    /**
     * ͼƬ�ļ���ȡ
     * 
     * @param srcImgPath
     * @return
     */
    private static BufferedImage InputImage(String srcImgPath) {
        BufferedImage srcImage = null;
        try {
            FileInputStream in = new FileInputStream(srcImgPath);
            srcImage = javax.imageio.ImageIO.read(in);
        } catch (IOException e) {
            log4j.logError("��ȡͼƬ����", e);
        }
        return srcImage;
    }

    /**
     * ��ͼƬ����ָ����ͼƬ�ߴ�ѹ��������ͼƬ������
     * 
     * @param srcImgPath :ԴͼƬ·��
     * @param outImgPath :�����ѹ��ͼƬ��·��
     * @param new_w :ѹ�����ͼƬ��
     * @param new_h :ѹ�����ͼƬ��
     * @throws IOException
     */
    public static void changeImageSize(String srcImgPath, String outImgPath, int new_w, int new_h) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImgPath);
        disposeImage(src, outImgPath, new_w, new_h);
    }

    /**
     * ָ�������߿�����ֵ��ѹ��ͼƬ������ͼƬ������
     * 
     * @param srcImgPath :ԴͼƬ·��
     * @param outImgPath :�����ѹ��ͼƬ��·��
     * @param maxLength :�����߿�����ֵ
     * @throws IOException
     */
    public static void changeImageSize(String srcImgPath, String outImgPath, int maxLength) throws IOException {
        // �õ�ͼƬ
        BufferedImage src = InputImage(srcImgPath);
        if (null != src) {
            int old_w = src.getWidth(); // �õ�Դͼ��
            int old_h = src.getHeight(); // �õ�Դͼ��
            int new_w = 0;// ��ͼ�Ŀ�
            int new_h = 0;// ��ͼ�ĳ�

            // ����ͼƬ�ߴ�ѹ���ȵõ���ͼ�ĳߴ�
            if (old_w > old_h) {
                // ͼƬҪ���ŵı���
                new_w = maxLength;
                new_h = (int) Math.round(old_h * ((float) maxLength / old_w));
            } else {
                new_w = (int) Math.round(old_w * ((float) maxLength / old_h));
                new_h = maxLength;
            }

            disposeImage(src, outImgPath, new_w, new_h);
        }
    }

    /**
     * ����ͼƬ
     * 
     * @param src
     * @param outImgPath
     * @param new_w
     * @param new_h
     * @throws IOException
     */
    private synchronized static void disposeImage(BufferedImage src, String outImgPath, int new_w, int new_h)
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
        OutImage(outImgPath, newImg);
    }

    /**
     * ��ͼƬ�ļ������ָ����·���������趨ѹ������
     * 
     * @param outImgPath
     * @param newImg
     * @param per
     * @throws IOException
     */
    private static void OutImage(String outImgPath, BufferedImage newImg) throws IOException {
        // �ж�������ļ���·���Ƿ���ڣ��������򴴽�
        File file = new File(outImgPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // ������ļ���
        try {
            ImageIO.write(newImg, outImgPath.substring(outImgPath.lastIndexOf(".") + 1), new File(outImgPath));
        } finally {
            outImgPath = null;
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

}
