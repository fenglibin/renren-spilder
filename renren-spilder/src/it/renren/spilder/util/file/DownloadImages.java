package it.renren.spilder.util.file;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.wash.WashBase;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/*
 * 根据配置文件获取图片, 配置文件格式如下: <br> <?xml version="1.0" encoding="GB2312"?> <Download> <Images> <Date>2012-08-10</Date>
 * <!--Image和LitImage都是可以多个的-->
 * <Image>http://www.searchdatabase.com.cn/upload/article/2012/2012-08-10-10-08-12.jpg</Image>
 * <Image>http://www.searchdatabase.com.cn/upload/article/2012/2012-08-10-10-08-53.jpg</Image>
 * <Image>http://www.searchdatabase.com.cn/upload/article/2012/2012-08-10-10-09-54.jpg</Image>
 * <LitImage>http://www.searchdatabase.com.cn/upload/article/2012/2012-08-10-10-08-12.jpg</LitImage> </Images>
 * </Download>
 */
public class DownloadImages extends WashBase {

    private static String configFile      = "images.xml";
    private static String imagePathPrefix = "/home/fenglibin/www/img.renren.it/";

    /**
     * @param args
     * @throws IOException
     * @throws JDOMException
     */
    public static void main(String[] args) throws JDOMException, IOException {
        initArgs(args);
        try {
            Document doc = JDomUtil.getDocument(new File(getConfigFile()));
            String storePath = doc.getRootElement().getChild("Images").getChildText("Date");
            if (!storePath.startsWith("/")) {
                storePath = imagePathPrefix + storePath;
            }
            @SuppressWarnings("unchecked")
            List<Element> images = doc.getRootElement().getChild("Images").getChildren("Image");
            @SuppressWarnings("unchecked")
            List<Element> litImages = doc.getRootElement().getChild("Images").getChildren("LitImage");
            for (Element e : images) {
                FileUtil.downloadFile(e.getText(), storePath);
            }
            for (Element e : litImages) {
                String imageName = FileUtil.getFileName(e.getText());
                UrlUtil.getLitPicName(storePath, imageName);

            }
        } finally {
            release();
        }
    }

    /**
     * 通过WGET命令获取图片
     * 
     * @param storePath
     * @param imageUrl
     * @throws IOException
     */
    private static void getImageByWget(String storePath, String imageUrl) throws IOException {
        String imageName = FileUtil.getFileName(imageUrl);
        Runtime.getRuntime().exec("del " + storePath + imageName);
        Runtime.getRuntime().exec("/bash/wget -O=" + storePath + " " + imageUrl);
    }

    private static String getConfigFile() {
        if (System.getProperty("configFile") != null) {
            configFile = System.getProperty("configFile");
        }
        return configFile;
    }

}
