package it.renren.spilder.test.util;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

public class FileUtilTest extends TestCase {

    @Test
    public void testReplaceFileExt() {
        String str = "d:/a/b/c.txt";
        String desExt = ".php";
        str = FileUtil.replaceFileExt(str, desExt);
        assertEquals(str, "d:/a/b/c.php");
    }

    @Test
    public void testRemoveScript() {
        String url = "http://www.renren.it/a/JAVAbiancheng/ANT/20120617/131202.html";
        String encode = "gb2312";
        String str;
        try {
            str = HttpClientUtil.getGetResponseWithHttpClient(url, encode);
            str = StringUtil.removeScript(str);
            assertEquals(-1, str.indexOf("<script"));
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveScriptByLoopFind() {
        String url = "http://www.renren.it/a/JAVAbiancheng/ANT/20120617/131202.html";
        String encode = "gb2312";
        String str;
        try {
            str = HttpClientUtil.getGetResponseWithHttpClient(url, encode);
            str = StringUtil.removeScriptByLoopFind(str);
            assertEquals(-1, str.indexOf("<script"));
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testGetFileName() {
        String str = "990238.html";
        str = FileUtil.getFileNameWithoutExt(str);
        assertEquals(str, "990238");
    }

    @Test
    public void testDownloadImage() throws IOException {
        String image = "http://www.cocoachina.com/cms/uploads/allimg/111215/3292_111215105434_1.png";
        String saveLocation = "d:/test/";
        FileUtil.downloadFile(image, saveLocation);

    }

    @Test
    public void testReplaceContentInDir() throws IOException {
        String dir = "E:\\bruce\\p\\fanli.rritw.com";
        String charset = "utf-8";
        List<String> fileType = new ArrayList<String>();
        fileType.add("php");
        List<String> contentList = FileUtil.readFile2List("fanli.rritw.com.tables.txt");
        List<String> sourceList = new ArrayList<String>();
        List<String> descList = new ArrayList<String>();
        for (String content : contentList) {
            String[] contentArray = content.split("\\|");
            sourceList.add("BIAOTOU.\"" + contentArray[0].trim());
            sourceList.add("BIAOTOU . \"" + contentArray[0].trim());
            descList.add("BIAOTOU.\"" + contentArray[1].trim());
            descList.add("BIAOTOU . \"" + contentArray[1].trim());
        }
        FileUtil.replaceContentInDir(sourceList, descList, dir, fileType, Boolean.TRUE, Boolean.FALSE, charset);
    }

    @Test
    public void testReplaceContentInSql() throws IOException {
        String source = "";
        String desc = "";
        String dir = "E:\\bruce\\p\\fanli.rritw.com";
        String charset = "utf-8";
        List<String> fileType = new ArrayList<String>();
        fileType.add("sql");
        List<String> contentList = FileUtil.readFile2List("E:\\bruce\\p\\replace.txt");
        List<String> sourceList = new ArrayList<String>();
        List<String> descList = new ArrayList<String>();
        for (String content : contentList) {
            String[] contentArray = content.split("\\|");
            source = "`fanli_" + contentArray[0].trim() + "`";
            sourceList.add(source);
            desc = "`fanli_" + contentArray[1].trim() + "`";
            descList.add(desc);
            System.out.println("source:" + source + ", desc:" + desc);

        }
        FileUtil.replaceContentInDir(sourceList, descList, dir, fileType, Boolean.FALSE, Boolean.FALSE, charset);
    }
}
