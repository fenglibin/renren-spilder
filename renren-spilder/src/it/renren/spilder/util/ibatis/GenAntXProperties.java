package it.renren.spilder.util.ibatis;

import it.renren.spilder.util.JDomUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public class GenAntXProperties {

    /**
     * @param args
     * @throws IOException
     * @throws JDOMException
     */
    public static void main(String[] args) throws JDOMException, IOException {
        // TODO Auto-generated method stub
        String autoConfigFile = "E:/bruce/tools/bizblog/common/config/src/conf/META-INF/autoconf/auto-config.xml";
        Document doc = JDomUtil.getDocument(new File(autoConfigFile));
        List<Element> propertyList = (List<Element>) XPath.selectNodes(doc, "//property");
        for (Element property : propertyList) {
            String name = property.getAttributeValue("name");
            String defaultValue = property.getAttributeValue("defaultValue");
            if (defaultValue == null) {
                defaultValue = "";
            }
            System.out.println(name + "=" + defaultValue);
        }
    }

}
