package test;

import it.renren.spilder.util.StringUtil;

public class TestStringUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String source="0aaaaaaa11cccccccccddddddddddeeeeeeeeee11ewerwersdfsdfsd11dffadfaaaaaaaa11cccccccccddddddddddeeeeeeeeee11ewerwersdfsdfsd11dffadfa";
		//String locateString="11";
		String locateString="aa";
		String addString="ZZ";
		System.out.println(testAddStringBeforeAll(source,locateString,addString));
	}
	private static String testAddStringBeforeAll(String source,String locateString,String addString){
		source = StringUtil.addStringBeforeAll(source, locateString, addString);
		return source;
	}
}
