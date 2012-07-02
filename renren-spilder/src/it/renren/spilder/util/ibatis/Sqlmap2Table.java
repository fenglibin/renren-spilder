package it.renren.spilder.util.ibatis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * ����Ibatis��SqlMap�����ļ����������ɱ�ṹ��<br>
 * Ҫ�����е�sqlmap�ж�Ӧ���ֶζ���jdbcType������ԡ�
 * 
 * @author Administrator 2012-7-2 ����09:33:07
 */
public class Sqlmap2Table {

    // Ĭ�����е�varchar����512�����Ա�֤�������������ֶ�
    private static final String DEFAULT_VARCHAR_LENGTH = "VARCHAR(512)";

    public static void main(String[] args) throws JDOMException, IOException {
        // ����ָ�����sqlmap�����ļ�����·��
        String sqlMap = "I:/Site/proc/bizblog_trunk/dal/src/conf";
        analysis(sqlMap);
    }

    /**
     * ����ָ����Ŀ¼���б�������
     * 
     * @param path
     * @throws IOException
     * @throws JDOMException
     */
    private static void analysis(String path) throws IOException, JDOMException {
        File filePath = new File(path);
        if (filePath.isDirectory() && !filePath.getName().equals(".svn")) {
            File[] fileList = filePath.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    analysis(file.getAbsolutePath());
                } else {
                    analysisSqlMap(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * ����������sqlmap�����ļ�
     * 
     * @param sqlMapFile
     * @throws IOException
     * @throws JDOMException
     */
    private static void analysisSqlMap(String sqlMapFile) throws IOException, JDOMException {
        // System.out.println(sqlMapFile);
        /**
         * ����Ҫ��sqlmap�ļ��е���һ��ȥ����<br>
         * <!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd"><br>
         * ����JDom�����ļ�����Document����ʱ���ᱨ�Ҳ���www.ibatis.com����쳣��������Ⱦ���ɹ���
         */
        String xmlString = filterRead(sqlMapFile, "<!DOCTYPE");
        Document doc = getDocument(xmlString);
        List<Element> resultMap = (List<Element>) XPath.selectNodes(doc, "//resultMap");
        for (Element e : resultMap) {
            String alias = e.getAttributeValue("class");
            String tableName = getTableName(doc, alias);
            List<Element> children = e.getChildren();
            StringBuilder createTableString = new StringBuilder("create table " + tableName + "(\n\t");
            int size = 0;
            for (Element child : children) {
                String jdbcType = child.getAttributeValue("jdbcType");
                if (jdbcType.toUpperCase().equals("VARCHAR")) {
                    jdbcType = DEFAULT_VARCHAR_LENGTH;
                }
                createTableString.append(child.getAttributeValue("column")).append(" ").append(jdbcType);
                if (size < children.size() - 1) {
                    createTableString.append(",\n\t");

                } else {
                    createTableString.append("\n");
                }
                size++;
            }
            createTableString.append(")");
            System.out.println(createTableString.toString().toUpperCase());
        }
    }

    private static String getTableName(Document doc, String alias) throws JDOMException {
        String tableName = "";
        String classPath = null;
        // �����alias������һ��������Ҳ������һ��java��·����������ͨ����alias�Ƿ��е�"."�������������
        if (alias.indexOf(".") > 0) {// ��JAVA��
            classPath = alias;
        } else {// �Ǳ������͵����õı�����ȥ��
            Element aliasElement = (Element) XPath.selectSingleNode(doc, "//typeAlias[@alias=\"" + alias + "\"]");
            classPath = aliasElement.getAttributeValue("type");
        }
        String[] classPathArray = classPath.split("\\.");
        // ȡ��DO������
        classPath = classPathArray[classPathArray.length - 1];
        int i = classPath.lastIndexOf("DO");
        // ȡ�����ݱ������ɵ�DO���ƣ��ޡ�DO�������ַ�
        classPath = classPath.substring(0, i);
        char[] chars = classPath.toCharArray();
        boolean isFirst = Boolean.TRUE;
        // ������ʵ�ı���
        for (char c : chars) {
            if (!isFirst && c >= 65 && c <= 90) {
                tableName += "_";
            }
            if (isFirst) {
                isFirst = Boolean.FALSE;
            }
            tableName += c;
        }
        // ����ת��Ϊ��д����
        return tableName.toUpperCase();
    }

    /**
     * �������Ķ�
     * 
     * @param filePath �ļ�·��
     * @param notIncludeLineStartWith ���������ַ�����ĳ�еĿ�ͷ���������ַ��������ڶ�ȡ��ʱ����к���
     * @return
     * @throws IOException
     */
    private static String filterRead(String filePath, String notIncludeLineStartWith) throws IOException {
        String result = "";
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            if (!line.startsWith(notIncludeLineStartWith)) {
                result += line;
            }
            line = br.readLine();
            if (line != null && !line.startsWith(notIncludeLineStartWith)) {
                result += "\n";
            }
        }
        br.close();
        fr.close();
        return result;
    }

    /**
     * ����XML �ַ��� ����JDom��Document����
     * 
     * @param xmlString XML��ʽ���ַ���
     * @return Document ���ؽ�����JDom��Document���󣬽������ɹ����׳��쳣��
     * @throws IOException
     * @throws JDOMException
     */
    private static Document getDocument(String xmlString) throws JDOMException, IOException {

        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(new StringReader(xmlString));
        return anotherDocument;

    }
}
