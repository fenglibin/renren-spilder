package it.renren.tools.wsclient.wsdl.wss;

import it.renren.tools.wsclient.util.StringUtil;
import it.renren.tools.wsclient.wsdl.wss.crypto.KeyMaterialWssCrypto;
import it.renren.tools.wsclient.wsdl.wss.crypto.WssCrypto;

import java.io.IOException;

import javax.security.auth.login.CredentialException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.w3c.dom.Document;

/**
 * @author libinfeng
 */
public class WSSUtil {

    /**
     * Add Web Service Security Header to Request Document
     * 
     * @param doc
     * @param wssEntry
     * @return
     * @throws IOException
     * @throws CredentialException
     * @throws WSSecurityException
     * @throws Exception
     */
    public static void addWSSecurity(Document doc, WSSEntry wssEntry) throws WSSecurityException, CredentialException,
                                                                     IOException {

        WssCrypto wssCrypto = new KeyMaterialWssCrypto(wssEntry);
        WSSecSignature wssSign = new WSSecSignature();
        wssSign.setUserInfo(wssEntry.getAlias(), wssEntry.getAliasPassword());

        if (wssEntry.getKeyIdentifierType() != 0) wssSign.setKeyIdentifierType(wssEntry.getKeyIdentifierType());

        if (!StringUtil.isEmpty(wssEntry.getSignatureAlgorithm())) wssSign.setSignatureAlgorithm(wssEntry.getSignatureAlgorithm());

        if (!StringUtil.isEmpty(wssEntry.getSignatureCanonicalization())) wssSign.setSigCanonicalization(wssEntry.getSignatureCanonicalization());

        wssSign.setUseSingleCertificate(Boolean.FALSE);

        WSSecHeader secHeader = new WSSecHeader();
        secHeader.insertSecurityHeader(doc);

        wssSign.build(doc, wssCrypto.getCrypto(), secHeader);
    }
}
