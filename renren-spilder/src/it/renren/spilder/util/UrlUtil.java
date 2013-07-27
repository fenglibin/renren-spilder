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
import java.util.Set;

import org.htmlparser.util.ParserException;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0 ����ʱ�䣺2009-11-11 ����02:26:26 ��˵��:����Url��ַ��ȡָ����ҳ�ļ������ݣ������Ǳ��ص���ҳ��Ҳ�����ǻ������ϵ���ҳ��
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
     * ����URL��ȡ���ݣ����������ϵ���ҳҲ�����Ǳ��ص���ҳ. ����Ǳ�����ҳ����Ҫ��ǰ�油��ΪUrl��׼���ʵ�ַ�����ڱ����ļ�����·��ǰ�油"file:///"��
     * �籾���ļ�Ϊ"c:/a.htm"����ͨ������Ϊ"file:///c:/a.htm"
     * 
     * @param urlStr ����ȡ��url
     * @param charset �����ʽ
     * @return ��ȡ��������
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
     * ��ȡ��ǰ��ҳ���ݵı��룬δָ����ΪGBK
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
     * ��ͼƬ���浽���أ��������滻ԭͼƬ��ַΪ��ǰ������ͼƬ·���������
     * 
     * @param parentPageConfig �б�ҳ�������
     * @param childPageConfig ����ÿ��ҳ�������
     * @param detail ����ÿ��ҳ��ľ������ݣ�����⡢�������
     * @return
     * @throws Exception
     */
    public static void saveImages(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        // ��ǰͼƬҳ�����ڵ�URL��ַ�������ǵ�ǰҳ��������ַ��������"http://www.renren.it" �� "http://www.renren.it/a/b.html"
        String url = detail.getUrl();
        // ����URL��ַ��ȡ�������ݣ�������ָ�����ݵ�����
        String content = detail.getContent();
        // ��������ͼƬ��ַ��Ŀ���ַ����ͼƬ���ڷ�������ʲô�ط�����"/uploads/allimg/"��ע��·�����һ��Ҫ��"/"
        String imageDescUrl = parentPageConfig.getImageDescUrl();
        // ���ر���ͼƬ��·������"d:/t/"��ע��·�����һ��Ҫ��"/"
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
            /* ��ͼƬURL�е����ı�����л�ԭ */
            imageSrc = URLDecoder.decode(imageSrc, childPageConfig.getCharset());
            /* ��ȡ�ļ�����û��·�� */
            String imageName = FileUtil.getFileName(imageSrc);
            if (parentPageConfig.isImageRename()) {
                String ext = FileUtil.getFileExtensation(imageName);
                imageName = String.valueOf(System.currentTimeMillis());
                if (!StringUtil.isEmpty(ext)) {
                    imageName += Constants.DOT + ext;
                }
            }

            /* ��װ��ǰ����ͼƬ��ŵ�·�� */
            String imageDes = imageDescUrl + imageName;
            /* ����ȡ�����������ļ�����ʽд������ */
            /* ���ݵ�ǰ�ļ����ڷ��������Լ�ͼƬURL����ȡ��Զ�̷������ľ��Ե�ַ */
            String imageUrl = makeImageUrl(url, imageSrc);
            /* ��ȡԶ���ļ�������ָ��Ŀ¼�����棬�����Ϊĳ��ͼƬ��������Ǻ��Ըô��� */
            try {
                boolean imageSaveResult = true;
                File savedImage = new File(imageSaveLocation + imageName);
                if (!savedImage.exists()) {
                    imageSaveResult = FileUtil.downloadFile(imageUrl, imageSaveLocation, imageName);
                    if (imageSaveResult && !StringUtil.isEmpty(Environment.waterImageLocation)) {// ��Ҫ����ˮӡ
                        ImageUtil.addWaterMark(imageSaveLocation + imageName, Environment.waterImageLocation, ImageUtil.WaterImageLocation.LEFT_FOOT, 1);
                    }
                }

                /* �滻ԭʼͼƬ��·�� */
                content = content.replace(imageSrc, imageDes);

                if (imageSaveResult) {// ȷ��ͼƬ�Ǳ��ش��ڵ�
                    savedImage = new File(imageSaveLocation + imageName);
                }
                if (savedImage.length() > (Constants.K * 5)) {// ֻ�д���5K��ͼƬ���ڵ�ʱ�򣬲Ž�����ͼƬ��Ϊ���棬����Ϊ����һ����ͼ������
                    if (firstImage) {// ��һ��ͼƬ���˴���������ͼ
                        String litPicName = getLitPicName(imageSaveLocation, imageName);
                        if (!StringUtil.isEmpty(litPicName)) {
                            detail.setPicArticle(true);
                            detail.setLitpicAddress(imageDescUrl + litPicName);
                            firstImage = false;
                        }
                    }
                }

            } catch (Exception e) {/* �����ƴװ��ͼƬ��ַ�������쳣�����ٳ��Զ���ԭ��ַ���л�ȡ */
                FileUtil.downloadFile(imageSrc, imageSaveLocation, imageName);
                /* �滻ԭʼͼƬ��·�� */
                content = content.replace(imageSrc, imageDes);
            }
        }
        detail.setContent(content);
    }

    /**
     * ��������ͼ������������ͼ���ļ�������������ͼƬ�ļ���û����չ�����򲻱�������ͼ���Կ��ַ���""����
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
     * ���ݵ�ǰҳ��url��ַ���Լ������ͼƬ��ַ(��Ҫ�������ͣ�һ������http��ʼ�ľ��Ե�ַ��һ������"/"��ͷ�ĵ�ַ������һ��ֱ�����ļ�������"aa.gif")��ƴװ��ͼƬ��url��ַ
     * 
     * @param url ��ǰҳ���url��ַ
     * @param fileUrl ��ǰͼƬ�ĵ�ַ
     * @return
     */
    public static String makeUrl(String url, String fileUrl) {
        String hostUrl = getHost(url);
        if (fileUrl.indexOf("://") > 0) {// ���Ե�ַ
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
     * ��ɱ�׼��URL
     * 
     * @param pageUrl
     * @param childLinks
     */
    public static void makeStadardUrl(String pageUrl, Set<AHrefElement> childLinks) {
        for (AHrefElement link : childLinks) {
            String childUrl = link.getHref();
            childUrl = makeUrl(pageUrl, childUrl);
            link.setHref(childUrl);
        }
    }

    /**
     * ���ݴ����url��ַ����ȡ�����ַ���紫��"http://www.163.com/a/b.html"���õ���ֵΪ"http://www.163.com"
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
     * �����������ӵ����·���滻Ϊ����·��
     * 
     * @param childUrl ��ǰҳ���URL
     * @param childContent ��ǰҳ�������
     * @param charset ��ǰҳ��ı���
     * @return
     * @throws ParserException
     */
    public static String replaceRelativeUrl2AbsoluteUrl(String childUrl, String childContent, String charset) throws ParserException {
        Set<AHrefElement> childLinks = AHrefParser.ahrefParser(childContent, null, null, charset, Boolean.FALSE);
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
     * �����������еĳ����ӣ��޸�Ϊgo.renren.it�ĺ�Ӳ�����Ȼ��ͨ��APACHE��ת������������<br>
     * ��ԭ���ĳ������ǣ�http://www.abc.com/1.html���޸ĺ�Ϊ��http://go.renren.it/www.abc.com/1.html.
     * 
     * @param childContent
     * @param charset
     * @return
     * @throws ParserException
     */
    public static String replaceHref2GoUrl(String childContent, String charset) throws ParserException {
        Set<AHrefElement> childLinks = AHrefParser.ahrefParser(childContent, null, null, charset, Boolean.FALSE);
        for (AHrefElement href : childLinks) {
            String goUrl = GO_URL;
            String url = href.getHref();
            goUrl += removeProtocol(url);
            childContent = childContent.replace(url, goUrl);
        }
        return childContent;
    }

    /**
     * �Ƴ�Э��ͷ
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
     * ����URL�е������ַ����罫�ո��滻Ϊ%20�ȡ������Ľ���ת��Ȳ���:<br>
     * 1. + URL ��+�ű�ʾ�ո� %2B <br>
     * 2. �ո� URL�еĿո������+�Ż��߱��� %20<br>
     * 3. / �ָ�Ŀ¼����Ŀ¼ %2F<br>
     * 4. ? �ָ�ʵ�ʵ� URL �Ͳ��� %3F<br>
     * 5. % ָ�������ַ� %25<br>
     * 6. # ��ʾ��ǩ %23<br>
     * 7. & URL ��ָ���Ĳ�����ķָ��� %26<br>
     * 8. = URL ��ָ��������ֵ %3D <br>
     * 
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String prettyUrl(String url) throws UnsupportedEncodingException {
        if (StringUtil.isEmpty(url)) {
            return url;
        }
        // ����ո�
        url = url.replace(" ", "%20");
        // ��������
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
