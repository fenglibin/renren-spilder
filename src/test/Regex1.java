package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex1{
	public static void main(String args[]) {
	String str="http://www.cnblogs.com/mjc467621163/archive/2011/07/1222.html";
	String regEx="([0-9]){4}[/]([0-9]){2}[/]([0-9]){2}[/]([0-9])*.html"; //±Ì æaªÚf 
	Pattern p=Pattern.compile(regEx);
	Matcher m=p.matcher(str);
	boolean result=m.find();
	System.out.println(result);
	}
	}
