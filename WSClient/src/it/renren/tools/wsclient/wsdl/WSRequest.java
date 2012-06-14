package it.renren.tools.wsclient.wsdl;

/**
 * @author libinfeng
 */
public class WSRequest {

    String requestData;
    String soapAddress;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getSoapAddress() {
        return soapAddress;
    }

    public void setSoapAddress(String soapAddress) {
        this.soapAddress = soapAddress;
    }

}
