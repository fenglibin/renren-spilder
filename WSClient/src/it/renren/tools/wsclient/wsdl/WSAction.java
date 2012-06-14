package it.renren.tools.wsclient.wsdl;

import it.renren.tools.wsclient.util.StringUtil;
import it.renren.tools.wsclient.util.XmlUtils;
import it.renren.tools.wsclient.wsdl.wss.WSSEntry;
import it.renren.tools.wsclient.wsdl.wss.WSSUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.security.auth.login.CredentialException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author libinfeng
 */
public class WSAction {

    /**
     * Send Web Service Security Request
     * 
     * @param request
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws CredentialException
     * @throws Exception
     */
    public static WSResponse sendWSSRequest(WSRequest request, WSSEntry wssEntry) throws IOException, SAXException,
                                                                                 ParserConfigurationException,
                                                                                 CredentialException {
        Document doc = XmlUtils.parseXml(request.getRequestData());
        WSSUtil.addWSSecurity(doc, wssEntry);
        request.setRequestData(XmlUtils.serializePretty(doc));
        return sendWSRequest(request);
    }

    /**
     * Send Web Service Request
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static WSResponse sendWSRequest(WSRequest request) throws IOException, SAXException,
                                                             ParserConfigurationException {
        WSResponse response = new WSResponse();
        response.setRequestData(request.getRequestData());
        PostMethod postmethod = new PostMethod(request.getSoapAddress());
        byte[] b = request.getRequestData().getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(b, 0, b.length);
        RequestEntity re = new InputStreamRequestEntity(is, b.length,
                                                        "application/xop+xml; charset=UTF-8; type=\"text/xml\"");
        postmethod.setRequestEntity(re);
        HttpClient httpClient = new HttpClient();
        long start = System.currentTimeMillis();
        int statusCode = httpClient.executeMethod(postmethod);
        long end = System.currentTimeMillis();
        response.setStatusCode(statusCode);
        response.setCostTime(end - start);
        String soapResponseData = postmethod.getResponseBodyAsString();
        if (!StringUtil.isEmpty(soapResponseData)) {
            soapResponseData = XmlUtils.formatPretty(soapResponseData);
        }
        response.setResponseData(soapResponseData);
        return response;
    }
}
