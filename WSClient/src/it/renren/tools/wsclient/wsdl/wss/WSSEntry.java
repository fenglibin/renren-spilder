package it.renren.tools.wsclient.wsdl.wss;

/**
 * @author libinfeng
 */
public class WSSEntry {

    private String jksFile;
    private String jksPass;
    private String alias;
    private String aliasPassword;
    private int    keyIdentifierType = 3;
    private String signatureAlgorithm;
    private String signatureCanonicalization;
    private String cryptoProvider;

    public String getJksFile() {
        return jksFile;
    }

    public void setJksFile(String jksFile) {
        this.jksFile = jksFile;
    }

    public String getJksPass() {
        return jksPass;
    }

    public void setJksPass(String jksPass) {
        this.jksPass = jksPass;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAliasPassword() {
        return aliasPassword;
    }

    public void setAliasPassword(String aliasPassword) {
        this.aliasPassword = aliasPassword;
    }

    public int getKeyIdentifierType() {
        return keyIdentifierType;
    }

    public void setKeyIdentifierType(int keyIdentifierType) {
        this.keyIdentifierType = keyIdentifierType;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getSignatureCanonicalization() {
        return signatureCanonicalization;
    }

    public void setSignatureCanonicalization(String signatureCanonicalization) {
        this.signatureCanonicalization = signatureCanonicalization;
    }

    public String getCryptoProvider() {
        return cryptoProvider;
    }

    public void setCryptoProvider(String cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

}
