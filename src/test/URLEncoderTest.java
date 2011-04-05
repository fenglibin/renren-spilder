package test;

import it.renren.spilder.util.BshUtil;
import it.renren.spilder.util.StringUtil;

import java.net.URLEncoder;

import bsh.EvalError;

public class URLEncoderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://www.iq888.com/qiushi/qiushi_t.aspx?keyword=urlencode(У԰)&searchtype=title&start=((*)*10-10)";
		try {
			url = getAddUrl(url,"2");
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(url);
	}
	private static String getAddUrl(String batBaseUrl,String index) throws EvalError{
		String url = "";
		if(batBaseUrl.indexOf("((*)")>0){
			String s1 = "((*)" + StringUtil.subString(batBaseUrl, "((*)", ")") + ")";
			String s2 = s1.replace("(*)", String.valueOf(index));
			s2 = BshUtil.eval(s2).toString();
			url =  batBaseUrl.replace(s1, s2);
		}else{
			url = batBaseUrl.replace("(*)", index);
		}
		if(url.indexOf("urlencode(")>0){
			String cnwords = StringUtil.subString(url, "urlencode(", ")");
			String s1 = "urlencode(" + cnwords + ")";
			String s2 = URLEncoder.encode(cnwords);
			url = url.replace(s1,s2);
		}
		return url;
	}
}
