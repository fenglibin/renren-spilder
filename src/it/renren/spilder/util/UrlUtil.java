package it.renren.spilder.util;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.parser.ImageElement;
import it.renren.spilder.parser.ImageParser;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0 创建时间：2009-11-11 下午02:26:26 类说明:根据Url地址获取指定网页文件的内容，可以是本地的网页，也可以是互联网上的网页。
 */
public class UrlUtil {

    private static Log4j log4j = new Log4j(UrlUtil.class.getName());

    public static String getContentByURL(String urlStr) throws IOException {
        return getContentByURL(urlStr, null);
    }

    /**
     * 根据URL读取内容，可以网络上的网页也可以是本地的网页. 如果是本地网页，需要在前面补充为Url标准访问地址，即在本地文件绝对路径前面补"file:///"，
     * 如本地文件为"c:/a.htm"，则通过补充为"file:///c:/a.htm"
     * 
     * @param urlStr 待读取的url
     * @param charset 编码格式
     * @return 获取到的内容
     * @throws IOException
     */
    public static String getContentByURL(String urlStr, String charset) throws IOException {
        String content = "";
        URL url = new URL(urlStr);
        InputStream ins = url.openStream();
        byte[] bt = new byte[2048];
        int len = 0;
        while ((len = ins.read(bt)) != -1) {
            byte[] tbt = new byte[len];
            System.arraycopy(bt, 0, tbt, 0, len);
            content += new String(tbt);
            bt = new byte[2048];
        }
        if (charset == null) {
            charset = getCharset(content);
        }
        content = new String(content.getBytes(), charset);
        ins.close();
        // content = content.toLowerCase();
        return content;
    }

    /**
     * 获取当前网页内容的编码，未指定则为GBK
     * 
     * @param content
     * @return
     */
    private static String getCharset(String content) {
        String charset = "";
        int start = content.indexOf("charset=");
        if (start > 0) {
            content = content.substring(start + 8);
            int end = content.indexOf("\"");
            charset = content.substring(0, end);
            if (!(charset.startsWith("utf") || charset.equalsIgnoreCase("gbk") || charset.equalsIgnoreCase("gb2312"))) {
                charset = "gbk";
            }
        } else {
            charset = "gbk";
        }
        return charset;
    }

    /**
     * 将图片保存到本地，并返回替换原图片地址为当前服务器图片路径后的内容
     * 
     * @param parentPageConfig 列表页面的配置
     * @param childPageConfig 具体每个页面的配置
     * @param detail 当臆每个页面的具体内容，如标题、内容体等
     * @return
     * @throws Exception
     */
    public static void saveImages(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail)
                                                                                                                 throws Exception {
        // 当前图片页面所在的URL地址，或者是当前页面的主域地址；可以是"http://www.renren.it" 或 "http://www.renren.it/a/b.html"
        String url = detail.getUrl();
        // 根据URL地址获取到的内容，或者是指定部份的内容
        String content = detail.getContent();
        // 更改网络图片地址的目标地址，将图片放在服务器的什么地方，如"/uploads/allimg/"，注：路径最后一定要带"/"
        String imageDescUrl = parentPageConfig.getImageDescUrl();
        // 本地保存图片的路径，如"d:/t/"，注：路径最后一定要带"/"
        String imageSaveLocation = parentPageConfig.getImageSaveLocation();
        String charset = childPageConfig.getCharset();
        String date = DateUtil.getNow("yyyy-MM-dd");
        imageDescUrl = imageDescUrl + date + "/";
        imageSaveLocation = imageSaveLocation + date + File.separator;
        File file = new File(imageSaveLocation);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<ImageElement> imageElements = ImageParser.imageParser(content, charset);
        boolean firstImage = true;
        for (ImageElement image : imageElements) {
            String imageSrc = image.getSrc();
            /* 获取文件名，没有路径 */
            String fileName = FileUtil.getFileName(imageSrc);
            /* 组装当前下载图片存放的路径 */
            String imageDes = imageDescUrl + fileName;
            /* 将获取到的内容以文件的形式写到本地 */
            /* 根据当前文件所在服务器，以及图片URL，获取其远程服务器的绝对地址 */
            String imageUrl = makeUrl(url, imageSrc);
            /* 获取远程文件到本地指定目录并保存，如果因为某张图片处理错误，那忽略该错误 */
            try {
                File savedImage = new File(imageSaveLocation + fileName);
                if (!savedImage.exists()) {
                    FileUtil.downloadFileByUrl(imageUrl, imageSaveLocation, fileName);
                }
                /* 替换原始图片的路径 */
                content = content.replace(imageSrc, imageDes);
                if (savedImage.length() > Constants.K) {// 只有大于1K的图片存在的时候，才将这张图片作为封面，并认为这是一个带图的文章
                    detail.setPicArticle(true);
                    if (firstImage) {
                        detail.setLitpicAddress(imageDes);
                        firstImage = false;
                    }
                }
            } catch (Exception e) {/* 如果对拼装的图片地址处理发生异常，那再尝试对其原地址进行获取 */
                FileUtil.downloadFileByUrl(imageSrc, imageSaveLocation, fileName);
                /* 替换原始图片的路径 */
                content = content.replace(imageSrc, imageDes);
            }
        }
        detail.setContent(content);
    }

    /**
     * 根据当前页的url地址，以及传入的图片地址(主要三种类型，一种是以http开始的绝对地址；一种是以"/"开头的地址；另外一种直接是文件名，如"aa.gif")，拼装该图片的url地址
     * 
     * @param url 当前页面的url地址
     * @param fileUrl 当前图片的地址
     * @return
     */
    public static String makeUrl(String url, String fileUrl) {
        String hostUrl = getHost(url);
        if (fileUrl.indexOf("://") > 0) {// 绝对地址
            return fileUrl;
        } else if (!fileUrl.startsWith("/")) {
            String filename = FileUtil.getFileName(url);
            fileUrl = url.replace(filename, fileUrl);
        } else if (fileUrl.startsWith("/")) {
            if (hostUrl.endsWith("/")) {
                hostUrl = hostUrl.substring(0, hostUrl.length() - 2);
            }
            fileUrl = hostUrl + fileUrl;
        }
        return fileUrl;
    }

    /**
     * 根据传入的url地址，获取主域地址，如传入"http://www.163.com/a/b.html"，得到的值为"http://www.163.com"
     * 
     * @param url
     * @return
     */
    private static String getHost(String url) {
        String host = null;
        String urlTemp = url.replace("://", "");
        if (urlTemp.indexOf("/") > 0) {
            host = url.substring(0, urlTemp.indexOf("/") + 3);
        } else {
            host = url;
        }
        return host;
    }

    public static void main(String[] args) {
        try {
            log4j.logDebug(getContentByURL(
                                           "http://www.ibm.com/developerworks/cn/views/java/libraryview.jsp?view_by=search&search_by=Ajax",
                                           "gbk"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }
    }
}
