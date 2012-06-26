package it.renren.spilder.util.wash;

import it.renren.spilder.util.UrlUtil;

import java.io.IOException;

/**
 * 类GenLitImage.java的实现描述：TODO 生成文件的缩略图
 * 
 * @author Administrator 2012-6-26 上午08:20:21
 */
public class GenLitImage extends WashBase {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        initArgs(args);
        // TODO Auto-generated method stub
        String savePath = System.getProperty("savePath");
        String filename = System.getProperty("filename");
        UrlUtil.getLitPicName(savePath, filename);
    }
}
