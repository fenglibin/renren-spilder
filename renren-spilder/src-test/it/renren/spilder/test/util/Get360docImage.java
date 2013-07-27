package it.renren.spilder.test.util;

import it.renren.spilder.main.Environment;
import it.renren.spilder.util.FileUtil;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

public class Get360docImage extends TestCase {

    @Test
    public void testGet360DocImage() throws IOException {
        Environment.referer = "http://www.360doc.com";
        FileUtil.downloadFile("http://image62.360doc.com/DownloadImg/2013/07/0115/33529790_1", "d:/test/");
    }
}
