package weiboautoman.timer.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.WeiboException;
import weibo4j.http.ImageItem;

public class ImageUtil {

    private static Logger log = LoggerFactory.getLogger(ImageUtil.class);

    public static ImageItem readImageItem(String imageSrc) throws WeiboException, IOException {
        String name = FileUtil.getFileName(imageSrc);
        byte[] content = readImageByteArray(imageSrc);
        return new ImageItem(name, content);
    }

    /**
     * 将网络图片读入到字节数组中
     * 
     * @param imageSrc
     * @return
     * @throws IOException
     */
    public static byte[] readImageByteArray(String imageSrc) throws IOException {
        byte[] bt = null;
        org.apache.commons.httpclient.HttpClient httpclient = HttpClientUtil.getHttpClient();
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", "Mozilla/3.0 (compatible; MSIE 6.0; Windows NT 6.1)"));
        httpclient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        GetMethod get = new GetMethod(imageSrc);
        InputStream instream = null;
        try {
            httpclient.executeMethod(get);
            int imageSize = Integer.parseInt(get.getResponseHeader("Content-Length").getValue());
            instream = get.getResponseBodyAsStream();
            bt = new byte[imageSize];
            int length = -1;
            byte[] readByte = new byte[2048];
            int destPos = 0;
            while ((length = instream.read(readByte)) != -1) {
                System.arraycopy(readByte, 0, bt, destPos, length);
                destPos += length;
            }
        } catch (HttpException e) {
            if (log.isErrorEnabled()) {
                log.error("get image from network error:" + e.getMessage(), e);
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("get image from network error:" + e.getMessage(), e);
            }
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
        return bt;
    }

    /**
     * 将图片保存到本地，并返回图片保存后的绝对路径
     * 
     * @param imagePath 图片保存的前缀路径
     * @param imageUrl 图片的网络地址
     * @return
     * @throws IOException
     */
    public static String saveImage(String imagePath, String imageUrl) throws IOException {
        int maxLength = 600;
        return saveImage(imagePath, imageUrl, maxLength);
    }

    /**
     * 将图片保存到本地，并返回图片保存后的绝对路径
     * 
     * @param imagePath 图片保存的前缀路径
     * @param imageUrl 图片的网络地址
     * @param maxLength 图片保留最高的高度或者宽度，这个参数用于对图片的压缩
     * @return
     * @throws IOException
     */
    public static String saveImage(String imagePath, String imageUrl, int maxLength) throws IOException {
        String tempUrlImageDir = "tempUrlImageDir";
        String date = DateUtil.getNow("yyyy-MM-dd");
        /* 将图片URL中的中文编码进行还原 */
        try {
            imageUrl = URLDecoder.decode(imageUrl, "GBK");
        } catch (UnsupportedEncodingException e) {
        }
        /* 获取文件名，没有路径 */
        String imageName = FileUtil.getFileName(imageUrl);
        /* 是否常用图片文件格式检查 */
        if (!FileUtil.isImageUsualFile(imageName)) {
            if (log.isWarnEnabled()) {
                log.warn("图片：" + imageUrl + " 不是常见的图片格式：jpg,png,gif,bmp");
            }
            return null;
        }
        // 将文件重新命名
        imageName = String.valueOf(System.currentTimeMillis()).concat(String.valueOf(NumberUtil.getRandomInt())) + "."
                    + FileUtil.getFileExtensation(imageName);
        String imageSaveLocation = imagePath + tempUrlImageDir + File.separator + date + File.separator;
        File descDir = new File(imageSaveLocation);
        if (!descDir.exists()) {
            descDir.mkdirs();
        }
        /* 将获取到的内容以文件的形式写到本地 */
        /* 获取远程文件到本地指定目录并保存，如果因为某张图片处理错误，那忽略该错误 */
        FileUtil.downloadFileByUrl(imageUrl, imageSaveLocation, imageName);
        /* 对图片进行压缩，最大宽度或者高度不能够超过指定的值 */
        changeImageSize(imageSaveLocation + imageName, maxLength);
        return imageSaveLocation + imageName;
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
            src = null;

        }
    }

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
}
