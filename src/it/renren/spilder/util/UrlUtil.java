package it.renren.spilder.util;
import it.renren.spilder.util.log.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0
 * ����ʱ�䣺2009-11-11 ����02:26:26
 * ��˵��:����Url��ַ��ȡָ����ҳ�ļ������ݣ������Ǳ��ص���ҳ��Ҳ�����ǻ������ϵ���ҳ��
 */
public class UrlUtil {
	private static Log4j log4j = new Log4j(UrlUtil.class.getName());
	public static String getContentByURL(String urlStr) throws IOException{
		return getContentByURL(urlStr, null);
	}
	/**
     * ����URL��ȡ���ݣ����������ϵ���ҳҲ�����Ǳ��ص���ҳ.
     * ����Ǳ�����ҳ����Ҫ��ǰ�油��ΪUrl��׼���ʵ�ַ�����ڱ����ļ�����·��ǰ�油"file:///"��
     * �籾���ļ�Ϊ"c:/a.htm"����ͨ������Ϊ"file:///c:/a.htm"
     * @param urlStr	����ȡ��url
     * @param charset	�����ʽ
     * @return			��ȡ��������
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
     * ��ȡ��ǰ��ҳ���ݵı��룬δָ����ΪGBK
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