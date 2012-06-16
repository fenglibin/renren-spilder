package it.renren.spilder.test;

import bsh.EvalError;
import it.renren.spilder.util.BshUtil;
import it.renren.spilder.util.StringUtil;

public class SubTest {
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
		return url;
	}
	/**
	 * @param args
	 * @throws EvalError 
	 */
	public static void main(String[] args) throws EvalError {
		// TODO Auto-generated method stub
		String str = "http://www.google.org.cn/page/(*)";
		str = getAddUrl(str,"2");
		System.out.println(str);
	}

}
