package it.renren.tools.wsclient.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * General XML-related utilities This file is from soapUI
 * 
 * @author libinfeng
 */

@SuppressWarnings("deprecation")
public final class XmlUtils {

    private static DocumentBuilder documentBuilder;

    public static Document parseXml(String xmlString) throws IOException, SAXException, ParserConfigurationException {
        return parse(new InputSource(new StringReader(xmlString)));
    }

    static synchronized public Document parse(InputSource inputSource) throws IOException, SAXException,
                                                                      ParserConfigurationException {
        return ensureDocumentBuilder().parse(inputSource);
    }

    private static DocumentBuilder ensureDocumentBuilder() throws ParserConfigurationException {
        if (documentBuilder == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            documentBuilder = dbf.newDocumentBuilder();
        }

        return documentBuilder;
    }

    public static String serializePretty(Document document) throws IOException {
        Writer out = new StringWriter();
        serializePretty(document, out);
        return out.toString();
    }

    public static void serializePretty(Document dom, Writer writer) throws IOException {
        try {
            XmlObject xmlObject = XmlObject.Factory.parse(dom.getDocumentElement());
            serializePretty(xmlObject, writer);
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    public static void serializePretty(XmlObject xmlObject, Writer writer) throws IOException {
        XmlOptions options = new XmlOptions();
        options.setSavePrettyPrint();
        options.setSavePrettyPrintIndent(3);
        options.setSaveNoXmlDecl();
        options.setSaveAggressiveNamespaces();
        // StringToStringMap map = new StringToStringMap();
        // map.put( SoapVersion.Soap11.getEnvelopeNamespace(), "SOAPENV" );
        // map.put( SoapVersion.Soap12.getEnvelopeNamespace(), "SOAPENV" );
        //
        // options.setSaveSuggestedPrefixes( map );

        xmlObject.save(writer, options);
    }

    /**
     * format the given XML string pretty.
     * 
     * @param xmlString
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static String formatPretty(String xmlString) throws IOException, SAXException, ParserConfigurationException {
        return xmlString = serializePretty(parseXml(xmlString));
    }

}
