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
     * ����XML �ַ��� ����JDom��Document����
     * @param xmlString XML��ʽ���ַ���
     * @return Document ���ؽ�����JDom��Document���󣬽������ɹ����׳��쳣��
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
     * ����XML�ļ�������JDom��Document����
     * @param file 		XML�ļ�
     * @return Document ���ؽ�����JDom��Document���󣬽������ɹ����׳��쳣��
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
     * ����XML�� ����JDom��Document����
     * @param ins InputStream XML�ַ���
     * @return Document ���ؽ�����JDom��Document���󣬽������ɹ����׳��쳣��
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
     * ����URL���� ����JDom��Document����
     * @param urlStr String URL����
     * @return Document ���ؽ�����JDom��Document���󣬽������ɹ����׳��쳣��
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
     * ���������JDom Document�������ָ���ı���ת���ַ������ء�
     * @param xmlDoc ��Ҫ��ת����JDom����
     * @param encoding ����ַ���ʹ�õı���
     * @return String Document���������ɵ��ַ���
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
      * ����xpath��ȡֵ
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
      * ����xpath��ȡֵ
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
