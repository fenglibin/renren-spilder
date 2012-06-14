package it.renren.tools.wsclient.wsdl.wss.crypto;

import it.renren.tools.wsclient.util.StringUtil;
import it.renren.tools.wsclient.wsdl.wss.WSSEntry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

import org.apache.commons.ssl.KeyStoreBuilder;
import org.apache.commons.ssl.Util;
import org.apache.ws.security.components.crypto.CredentialException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.Merlin;

/**
 * @author libinfeng
 */
public class KeyMaterialWssCrypto implements WssCrypto {

    private WSSEntry      config;
    private KeyStore      keyStore;
    private static Crypto crypto;

    public KeyMaterialWssCrypto(WSSEntry config){
        this.config = config;
    }

    public void setConfig(WSSEntry config) {
        this.config = config;
    }

    public KeyStore load() throws Exception {
        if (keyStore != null) return keyStore;

        if (!StringUtil.isEmpty(getDefaultAlias()) && !StringUtil.isEmpty(getAliasPassword())) {
            keyStore = KeyStoreBuilder.build(Util.streamToBytes(new FileInputStream(config.getJksFile())),
                                             getDefaultAlias().getBytes(), getPassword().toCharArray(),
                                             getAliasPassword().toCharArray());
        } else {
            keyStore = KeyStoreBuilder.build(Util.streamToBytes(new FileInputStream(config.getJksFile())),
                                             getPassword().toCharArray());
        }
        return keyStore;

    }

    public String getStatus() {
        try {
            if (!StringUtil.isEmpty(getSource()) && !StringUtil.isEmpty(getPassword())) {
                load();
                return "OK";
            } else {
                return "<unavailable>";
            }
        } catch (Exception e) {
            return "<error: " + e.getMessage() + ">";
        }
    }

    public Crypto getCrypto() throws CredentialException, IOException {
        if (crypto == null) {
            Properties properties = new Properties();
            properties.put("org.apache.ws.security.crypto.merlin.file", config.getJksFile());
            properties.put("org.apache.ws.security.crypto.merlin.keystore.provider", "this");
            if (!StringUtil.isEmpty(getDefaultAlias())) properties.put("org.apache.ws.security.crypto.merlin.keystore.alias",
                                                                       getDefaultAlias());
            if (!StringUtil.isEmpty(getAliasPassword())) properties.put("org.apache.ws.security.crypto.merlin.alias.password",
                                                                        getAliasPassword());
            crypto = new KeyMaterialCrypto(properties);
        }
        return crypto;
    }

    public String getPassword() {
        return config.getJksPass();
    }

    public String getAliasPassword() {
        return config.getAliasPassword();
    }

    public String getDefaultAlias() {
        return config.getAlias();
    }

    private class KeyMaterialCrypto extends Merlin {

        private KeyMaterialCrypto(Properties properties) throws CredentialException, IOException{
            super(properties);
        }

        @Override
        public KeyStore load(InputStream input, String storepass, String provider, String type)
                                                                                               throws CredentialException {
            if ("this".equals(provider)) {
                try {
                    return KeyMaterialWssCrypto.this.load();
                } catch (Exception e) {
                    throw new CredentialException(0, null, e);
                }
            } else return super.load(input, storepass, provider, type);
        }

        @Override
        protected String getCryptoProvider() {
            return config.getCryptoProvider();
        }
    }

    public String getCryptoProvider() {
        return config.getCryptoProvider();
    }

    public String getSource() {
        return config.getJksFile();
    }
}
