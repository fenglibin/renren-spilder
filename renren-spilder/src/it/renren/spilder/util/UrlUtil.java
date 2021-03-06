package it.renren.spilder.util;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Environment;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.parser.ImageElement;
import it.renren.spilder.parser.ImageParser;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.htmlparser.util.ParserException;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0 创建时间：2009-11-11 下午02:26:26 类说明:根据Url地址获取指定网页文件的内容，可以是本地的网页，也可以是互联网上的网页。
 */
public class UrlUtil {

    private static Log4j       log4j           = new Log4j(UrlUtil.class.getName());
    public static final String GO_URL_NO_HTTP  = "go.rritw.com";
    public static final String GO_URL          = "http://" + GO_URL_NO_HTTP + "/";
    public static String       DEFAULT_CHARSET = "GBK";

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
    public static void saveImages(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
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
        if (Environment.isSaveImage2CurrentHtmlFileTileNameDir) {
            // imageDescUrl += detail.getTitle() + File.separator;
            imageSaveLocation += detail.getTitle() + File.separator;
        }
        File file = new File(imageSaveLocation);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<ImageElement> imageElements = ImageParser.imageParser(content, charset);
        boolean firstImage = true;
        for (ImageElement image : imageElements) {
            String imageSrc = image.getSrc();
            if (StringUtil.isEmpty(imageSrc)) {
                continue;
            }
            /* 将图片URL中的中文编码进行还原 */
            imageSrc = URLDecoder.decode(imageSrc, childPageConfig.getCharset());
            /* 获取文件名，没有路径 */
            String imageName = FileUtil.getFileName(imageSrc);
            if (parentPageConfig.isImageRename()) {
                String ext = FileUtil.getFileExtensation(imageName);
                imageName = String.valueOf(System.currentTimeMillis());
                if (!StringUtil.isEmpty(ext)) {
                    imageName += Constants.DOT + ext;
                }
            }

            /* 组装当前下载图片存放的路径 */
            String imageDes = imageDescUrl + imageName;
            /* 将获取到的内容以文件的形式写到本地 */
            /* 根据当前文件所在服务器，以及图片URL，获取其远程服务器的绝对地址 */
            String imageUrl = makeImageUrl(url, imageSrc);
            /* 获取远程文件到本地指定目录并保存，如果因为某张图片处理错误，那忽略该错误 */
            try {
                boolean imageSaveResult = true;
                File savedImage = new File(imageSaveLocation + imageName);
                if (!savedImage.exists()) {
                    imageSaveResult = FileUtil.downloadFile(imageUrl, imageSaveLocation, imageName);
                    if (imageSaveResult && !StringUtil.isEmpty(Environment.waterImageLocation)) {// 需要增加水印
                        ImageUtil.addWaterMark(imageSaveLocation + imageName, Environment.waterImageLocation, ImageUtil.WaterImageLocation.LEFT_FOOT, 1);
                    }
                }

                /* 替换原始图片的路径 */
                content = content.replace(imageSrc, imageDes);

                if (imageSaveResult) {// 确保图片是本地存在的
                    savedImage = new File(imageSaveLocation + imageName);
                }
                if (savedImage.length() > (Constants.K * 5)) {// 只有大于5K的图片存在的时候，才将这张图片作为封面，并认为这是一个带图的文章
                    if (firstImage) {// 第一张图片，此处生成缩略图
                        String litPicName = getLitPicName(imageSaveLocation, imageName);
                        if (!StringUtil.isEmpty(litPicName)) {
                            detail.setPicArticle(true);
                            detail.setLitpicAddress(imageDescUrl + litPicName);
                            firstImage = false;
                        }
                    }
                }

            } catch (Exception e) {/* 如果对拼装的图片地址处理发生异常，那再尝试对其原地址进行获取 */
                FileUtil.downloadFile(imageSrc, imageSaveLocation, imageName);
                /* 替换原始图片的路径 */
                content = content.replace(imageSrc, imageDes);
            }
        }
        detail.setContent(content);
    }

    /**
     * 保存缩略图，并返回缩略图的文件名。如果传入的图片文件名没有扩展名，则不保存缩略图，以空字符串""返回
     * 
     * @param imageSaveLocation
     * @param imageName
     * @return
     * @throws IOException
     */
    public static String getLitPicName(String imageSaveLocation, String imageName) throws IOException {
        String litPicName = "";
        String ext = FileUtil.getFileExtensation(imageName);
        if (StringUtil.isEmpty(ext)) {
            return litPicName;
        }
        String filePrefix = imageName.replace(Constants.DOT + ext, "");
        litPicName = filePrefix + "-lp" + Constants.DOT + ext;
        File savedLitImage = new File(imageSaveLocation + litPicName);
        if (!savedLitImage.exists()) {
            ImageUtil.changeImageSize(imageSaveLocation + imageName, imageSaveLocation + litPicName, Constants.LIT_PIC_MAX_WIDTH_OR_HEIGHT);
        }
        return litPicName;
    }

    private static String makeImageUrl(String url, String fileUrl) {
        url = makeUrl(url, fileUrl);
        if (url.indexOf(GO_URL_NO_HTTP) > 0) {
            url = url.replace(GO_URL_NO_HTTP + "/", "");
        }
        return url;
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
        }
        if (fileUrl.startsWith("/")) {
            if (hostUrl.endsWith("/")) {
                hostUrl = hostUrl.substring(0, hostUrl.length() - 2);
            }
            fileUrl = hostUrl + fileUrl;
        } else {
            if (!hostUrl.endsWith("/")) {
                hostUrl = hostUrl + "/";
            }
            fileUrl = hostUrl + fileUrl;
        }
        return fileUrl;
    }

    /**
     * 组成标准的URL
     * 
     * @param pageUrl
     * @param childLinks
     */
    public static void makeStadardUrl(String pageUrl, List<AHrefElement> childLinks) {
        for (AHrefElement link : childLinks) {
            String childUrl = link.getHref();
            childUrl = makeUrl(pageUrl, childUrl);
            link.setHref(childUrl);
        }
    }

    /**
     * 根据传入的url地址，获取主域地址，如传入"http://www.163.com/a/b.html"，得到的值为"http://www.163.com"
     * 
     * @param url
     * @return
     */
    public static String getHost(String url) {
        String host = null;
        String urlTemp = url.replace("://", "");
        if (urlTemp.indexOf("/") > 0) {
            host = url.substring(0, urlTemp.indexOf("/") + 3);
        } else {
            host = url;
        }
        return host;
    }

    /**
     * 将内容中链接的相对路径替换为绝对路径
     * 
     * @param childUrl 当前页面的URL
     * @param childContent 当前页面的内容
     * @param charset 当前页面的编码
     * @return
     * @throws ParserException
     */
    public static String replaceRelativeUrl2AbsoluteUrl(String childUrl, String childContent, String charset) throws ParserException {
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(childContent, null, null, charset, Boolean.FALSE);
        for (AHrefElement href : childLinks) {
            String url = href.getHref();
            if (!url.startsWith("http")) {
                String urlAbsolute = UrlUtil.makeUrl(childUrl, url);
                url = Constants.COLON + url + Constants.COLON;
                urlAbsolute = Constants.COLON + urlAbsolute + Constants.COLON;
                childContent = childContent.replace(url, urlAbsolute);
            }
        }
        return childContent;
    }

    /**
     * 将内容中所有的超连接，修改为go.renren.it的后接参数，然后通过APACHE跳转，减少外链；<br>
     * 如原来的超连接是：http://www.abc.com/1.html，修改后为：http://go.renren.it/www.abc.com/1.html.
     * 
     * @param childContent
     * @param charset
     * @return
     * @throws ParserException
     */
    public static String replaceHref2GoUrl(String childContent, String charset) throws ParserException {
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(childContent, null, null, charset, Boolean.FALSE);
        for (AHrefElement href : childLinks) {
            String goUrl = GO_URL;
            String url = href.getHref();
            goUrl += removeProtocol(url);
            childContent = childContent.replace(url, goUrl);
        }
        return childContent;
    }

    /**
     * 移除协议头
     * 
     * @param url
     * @return
     */
    public static String removeProtocol(String url) {
        if (StringUtil.isEmpty(url)) {
            return url;
        }
        if (url.indexOf("://") < 0) {
            return url;
        }
        int index = url.indexOf("://") + 3;
        url = url.substring(index);
        return url;
    }

    /**
     * 处理URL中的特殊字符，如将空格替换为%20等、将中文进行转码等操作:<br>
     * 1. + URL 中+号表示空格 %2B <br>
     * 2. 空格 URL中的空格可以用+号或者编码 %20<br>
     * 3. / 分隔目录和子目录 %2F<br>
     * 4. ? 分隔实际的 URL 和参数 %3F<br>
     * 5. % 指定特殊字符 %25<br>
     * 6. # 表示书签 %23<br>
     * 7. & URL 中指定的参数间的分隔符 %26<br>
     * 8. = URL 中指定参数的值 %3D <br>
     * 
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String prettyUrl(String url) throws UnsupportedEncodingException {
        if (StringUtil.isEmpty(url)) {
            return url;
        }
        // 处理空格
        url = url.replace(" ", "%20");
        // 处理中文
        StringBuffer utf8Str = new StringBuffer();
        char[] charArray = url.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                utf8Str.append(URLEncoder.encode(String.valueOf(charArray[i]), "utf-8"));
            } else {
                utf8Str.append(charArray[i]);
            }
        }
        return utf8Str.toString();
    }

    public static void main(String[] args) {
        try {
            log4j.logDebug(getContentByURL("http://www.ibm.com/developerworks/cn/views/java/libraryview.jsp?view_by=search&search_by=Ajax", "gbk"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }
    }
}
