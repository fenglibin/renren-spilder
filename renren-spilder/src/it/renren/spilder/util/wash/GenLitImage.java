package it.renren.spilder.util.wash;

import it.renren.spilder.util.UrlUtil;

import java.io.IOException;

/**
 * ��GenLitImage.java��ʵ��������TODO �����ļ�������ͼ
 * 
 * @author Administrator 2012-6-26 ����08:20:21
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
