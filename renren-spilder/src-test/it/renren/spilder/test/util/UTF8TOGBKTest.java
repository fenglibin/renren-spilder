package it.renren.spilder.test.util;

import it.renren.spilder.util.HttpClientUtil;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public class UTF8TOGBKTest {

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     */
    public static void main(String[] args) throws HttpException, IOException {
        // TODO Auto-generated method stub
        String url = "http://jenmhdn.iteye.com/blog/1616906";
        String content = HttpClientUtil.getGetResponseWithHttpClient(url, "utf-8");
        System.out.println(content);
        System.out.println("\nChanged Charset:\n");
        content = new String(content.getBytes("ISO8859_1"), "gbk");
        System.out.println(content);
    }

}
