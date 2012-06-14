package it.renren.tools.wsclient.wsdl.wss.crypto;

import java.io.IOException;

import org.apache.ws.security.components.crypto.CredentialException;
import org.apache.ws.security.components.crypto.Crypto;

/**
 * @author libinfeng
 */
public interface WssCrypto {

    public Crypto getCrypto() throws IOException, CredentialException;

    public String getSource();

    public String getPassword();
}
