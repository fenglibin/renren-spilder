package it.renren.tools.wsclient.core;

import it.renren.tools.wsclient.util.FileUtil;
import it.renren.tools.wsclient.util.StringUtil;
import it.renren.tools.wsclient.util.XmlUtils;
import it.renren.tools.wsclient.wsdl.WSAction;
import it.renren.tools.wsclient.wsdl.WSRequest;
import it.renren.tools.wsclient.wsdl.WSResponse;
import it.renren.tools.wsclient.wsdl.wss.WSSEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.CredentialException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author libinfeng
 */
public class Run {

    public static void test() throws IOException, SAXException, ParserConfigurationException, CredentialException {
        WSRequest request = new WSRequest();
        String soapRequestData = FileUtil.read("OnPersonPlacingOrder.xml");
        request.setRequestData(soapRequestData);
        request.setSoapAddress("http://localhost:8080/executionserver/services");

        WSSEntry wssEntry = new WSSEntry();
        wssEntry.setJksFile("d:/tools/jks/client2.jks");
        wssEntry.setJksPass("sre123456");
        wssEntry.setAlias("client2");
        wssEntry.setAliasPassword("sre123456");

        WSResponse response = WSAction.sendWSSRequest(request, wssEntry);

        System.out.println(XmlUtils.formatPretty(response.getResponseData()));
    }

    public static void main(String[] args) {
        try {
            Properties p = null;
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    String[] keyValue = arg.split("=");
                    String value = "";
                    if (keyValue.length == 1) {
                        value = "";
                    } else {
                        value = keyValue[1];
                    }
                    System.setProperty(keyValue[0], value);
                }
            }
            String outFile = System.getProperty("outFile");
            String soapAddress = System.getProperty("soapAddress");
            if (StringUtil.isEmpty(soapAddress)) {
                System.out.println("The soapAddress is missing. For example: soapAddress=http://localhost:8080/executionserver/services");
                System.exit(-1);
            }

            String requestXmlFile = System.getProperty("requestXmlFile");
            if (StringUtil.isEmpty(requestXmlFile)) {
                System.out.println("Please set the request parameter. For example: requestXmlFile=requestXmlFile.xml");
                System.exit(-1);
            }
            if (!new File(requestXmlFile).exists()) {
                System.out.println("The request file is not exists:" + requestXmlFile);
                System.exit(-1);
            }
            String soapRequestData = FileUtil.read(requestXmlFile);
            if (StringUtil.isEmpty(outFile)) {
                System.out.println("Request Xml String From File:" + "\n" + soapRequestData + "\n");
            }

            WSRequest request = new WSRequest();
            request.setRequestData(soapRequestData);
            request.setSoapAddress(soapAddress);

            String wss = System.getProperty("wss");
            if (!StringUtil.isEmpty(wss)) {// check if webservice security request
                if (!new File(wss).exists()) {
                    System.out.println("The WebService Security config file is not exists:" + wss);
                    System.exit(-1);
                }
                p = new Properties();
                p.load(new FileInputStream(wss));
            }
            WSResponse response = null;
            if (p != null) {
                WSSEntry wssEntry = new WSSEntry();
                wssEntry.setJksFile(p.getProperty("jksFile"));
                wssEntry.setJksPass(p.getProperty("jksPass"));
                wssEntry.setAlias(p.getProperty("alias"));
                wssEntry.setAliasPassword(p.getProperty("aliasPassword"));

                response = WSAction.sendWSSRequest(request, wssEntry);
            } else {
                response = WSAction.sendWSRequest(request);
            }
            if (response != null) {
                StringBuilder sb = new StringBuilder("");
                if (!StringUtil.isEmpty(outFile)) {
                    sb.append("Request Xml String From File:");
                    sb.append("\n");
                    sb.append(soapRequestData);
                    sb.append("\n\n\n");
                }
                sb.append("The Xml String to Server:");
                sb.append("\n");
                sb.append(response.getRequestData());
                sb.append("\n");
                sb.append("\n");
                sb.append("The result of response:");
                sb.append("\n");
                sb.append("Status Code:");
                sb.append(response.getStatusCode());
                sb.append(",");
                sb.append("Cost Time:");
                sb.append(response.getCostTime());
                sb.append("\n");
                sb.append("Response:");
                sb.append("\n");
                sb.append(response.getResponseData());

                if (!StringUtil.isEmpty(outFile)) {
                    FileUtil.write2File(sb.toString(), System.getProperty("outFile"));
                    System.out.println("The response has been writed to file:" + System.getProperty("outFile"));
                } else {
                    System.out.println(sb.toString());
                }
            } else {
                System.out.println("Could not get the response.");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
