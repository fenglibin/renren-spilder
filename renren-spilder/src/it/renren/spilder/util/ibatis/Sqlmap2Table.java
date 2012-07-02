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
 * 根据Ibatis的SqlMap配置文件，重新生成表结构。<br>
 * 要求所有的sqlmap中对应的字段都有jdbcType这个属性。
 * 
 * @author Administrator 2012-7-2 下午09:33:07
 */
public class Sqlmap2Table {

    // 默认所有的varchar都是512，可以保证满足绝大多数的字段
    private static final String DEFAULT_VARCHAR_LENGTH = "VARCHAR(512)";

    public static void main(String[] args) throws JDOMException, IOException {
        // 这里指定你的sqlmap配置文件所在路径
        String sqlMap = "I:/Site/proc/bizblog_trunk/dal/src/conf";
        analysis(sqlMap);
    }

    /**
     * 根据指定的目录进行遍历分析
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
     * 分析单个的sqlmap配置文件
     * 
     * @param sqlMapFile
     * @throws IOException
     * @throws JDOMException
     */
    private static void analysisSqlMap(String sqlMapFile) throws IOException, JDOMException {
        // System.out.println(sqlMapFile);
        /**
         * 这里要把sqlmap文件中的这一行去掉：<br>
         * <!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd"><br>
         * 否则JDom根据文件创建Document对象时，会报找不到www.ibatis.com这个异常，导致渲染不成功。
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
        // 这里的alias可能是一个别名，也可能是一个java类路径，这里我通过该alias是否有点"."这个符号来区别
        if (alias.indexOf(".") > 0) {// 是JAVA类
            classPath = alias;
        } else {// 是别名，就到配置的别名中去找
            Element aliasElement = (Element) XPath.selectSingleNode(doc, "//typeAlias[@alias=\"" + alias + "\"]");
            classPath = aliasElement.getAttributeValue("type");
        }
        String[] classPathArray = classPath.split("\\.");
        // 取到DO的名称
        classPath = classPathArray[classPathArray.length - 1];
        int i = classPath.lastIndexOf("DO");
        // 取到根据表名生成的DO名称，无“DO”两个字符
        classPath = classPath.substring(0, i);
        char[] chars = classPath.toCharArray();
        boolean isFirst = Boolean.TRUE;
        // 生成真实的表名
        for (char c : chars) {
            if (!isFirst && c >= 65 && c <= 90) {
                tableName += "_";
            }
            if (isFirst) {
                isFirst = Boolean.FALSE;
            }
            tableName += c;
        }
        // 表名转换为大写返回
        return tableName.toUpperCase();
    }

    /**
     * 过滤性阅读
     * 
     * @param filePath 文件路径
     * @param notIncludeLineStartWith 不包括的字符，即某行的开头是这样的字符串，则在读取的时候该行忽略
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
     * 根据XML 字符串 建立JDom的Document对象
     * 
     * @param xmlString XML格式的字符串
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException
     * @throws JDOMException
     */
    private static Document getDocument(String xmlString) throws JDOMException, IOException {

        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(new StringReader(xmlString));
        return anotherDocument;

    }
}
