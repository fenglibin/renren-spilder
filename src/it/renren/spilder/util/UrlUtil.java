package it.renren.spilder.util;
import it.renren.spilder.util.log.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0
 * 创建时间：2009-11-11 下午02:26:26
 * 类说明:根据Url地址获取指定网页文件的内容，可以是本地的网页，也可以是互联网上的网页。
 */
public class UrlUtil {
	private static Log4j log4j = new Log4j(UrlUtil.class.getName());
	public static String getContentByURL(String urlStr) throws IOException{
		return getContentByURL(urlStr, null);
	}
	/**
     * 根据URL读取内容，可以网络上的网页也可以是本地的网页.
     * 如果是本地网页，需要在前面补充为Url标准访问地址，即在本地文件绝对路径前面补"file:///"，
     * 如本地文件为"c:/a.htm"，则通过补充为"file:///c:/a.htm"
     * @param urlStr	待读取的url
     * @param charset	编码格式
     * @return			获取到的内容
     * @throws IOException
     */
    public static String getContentByURL(String urlStr,String charset) throws IOException{
    	String content="";
    	URL url = new URL(urlStr);    	
        InputStream ins = url.openStream();
        byte[] bt = new byte[2048];
        int len=0;
        while((len = ins.read(bt))!=-1){
        	byte[] tbt = new byte[len];
        	System.arraycopy(bt, 0, tbt, 0, len);
        	content += new String(tbt);
        	bt = new byte[2048];
        }
        if(charset == null){
        	charset = getCharset(content);
        }
        content = new String(content.getBytes(),charset);
        ins.close();
        //content = content.toLowerCase();
        return content;
    }
    /**
     * 获取当前网页内容的编码，未指定则为GBK
     * @param content
     * @return
     */
    private static String getCharset(String content){
    	String charset="";
    	int start = content.indexOf("charset=");
    	if(start>0){
    		content = content.substring(start+8);
        	int end = content.indexOf("\"");
        	charset = content.substring(0,end);
        	if(!(charset.startsWith("utf") || charset.equalsIgnoreCase("gbk") || charset.equalsIgnoreCase("gb2312"))){
        		charset = "gbk";
        	}
    	}else{
    		charset = "gbk";
    	}    	
    	return charset;
    }
    public static void main(String[] args){
    	try {
			log4j.logDebug(getContentByURL("http://www.ibm.com/developerworks/cn/views/java/libraryview.jsp?view_by=search&search_by=Ajax","gbk"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}
    }
}