package it.renren.spilder.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class JDomUtil {
	/**
     * 根据XML 字符串 建立JDom的Document对象
     * @param xmlString XML格式的字符串
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException 
     * @throws JDOMException 
     */
    public static Document getDocument(String xmlString) throws JDOMException, IOException
    {

        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(new StringReader(xmlString));
        return anotherDocument;
       
    }
    /**
     * 根据XML文件，建立JDom的Document对象
     * @param file 		XML文件
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException 
     * @throws JDOMException 
     */
    public static Document getDocument(File file) throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(file);
        
        return anotherDocument;
    }
    /**
     * 根据XML流 建立JDom的Document对象
     * @param ins InputStream XML字符流
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException 
     * @throws JDOMException 
     */
    public static Document getDocument(InputStream ins) throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(ins);
        return anotherDocument;
    }

    /**
     * 根据URL链接 建立JDom的Document对象
     * @param urlStr String URL链接
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException 
     * @throws JDOMException 
     */
    public static Document getDocumentByURL(String urlStr) throws IOException, JDOMException
    {
        URL url = new URL(urlStr);
        InputStream ins = url.openStream();
        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(ins);
        return anotherDocument;
    }
     /**
     * 这个方法将JDom Document对象根据指定的编码转换字符串返回。
     * @param xmlDoc 将要被转换的JDom对象
     * @param encoding 输出字符串使用的编码
     * @return String Document经处理生成的字符串
     * @throws IOException 
      */
     public static String toXML(Document xmlDoc, String encoding) throws IOException
     {
     	ByteArrayOutputStream byteRep = new ByteArrayOutputStream();
     	PrintWriter out=new PrintWriter(byteRep);
 		Format format = Format.getPrettyFormat();
 		format.setEncoding(encoding);
 		XMLOutputter docWriter = new XMLOutputter(format);
 		try {
 			docWriter.output(xmlDoc, out); 			
 		} catch (Exception e) {
 		}
 		return byteRep.toString();
     }
     /**
      * 根据xpath获取值
      * @param xmlDoc
      * @param xpath
      * @return
      */
     public static String getValueByXpath(Document xmlDoc,String xpath){
    	 String value="";
    	 try {
    		 if(XPath.selectSingleNode(xmlDoc,xpath) == null){
    			 value = null;
    		 }else{
    			 value = ((Element) (XPath.selectSingleNode(xmlDoc,xpath))).getTextTrim();
    		 }			
		} catch (JDOMException e) {}
		return value;
     }
     /**
      * 根据xpath获取值
      * @param xmlDoc
      * @param xpath
      * @return
      */
     public static String getValueByXpathNotTrim(Document xmlDoc,String xpath){
    	 String value="";
    	 try {
    		 if(XPath.selectSingleNode(xmlDoc,xpath) == null){
    			 value = null;
    		 }else{
    			 value = ((Element) (XPath.selectSingleNode(xmlDoc,xpath))).getText();
    		 }			
		} catch (JDOMException e) {}
		return value;
     }
}
