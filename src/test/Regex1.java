package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex1{
	public static void main(String args[]) {
	String str="http://www.google.org.cn/posts/cl-doodle-independence-de-doodle-oktoberfest.html";
	String regEx="http://www.google.org.cn/posts/([a-z]|-)*.html$"; //±Ì æaªÚf 
	Pattern p=Pattern.compile(regEx);
	Matcher m=p.matcher(str);
	boolean result=m.find();
	System.out.println(result);
	}
	}
