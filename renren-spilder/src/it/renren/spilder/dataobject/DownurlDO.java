package it.renren.spilder.dataobject;

import java.io.Serializable;

public class DownurlDO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4995963439933433394L;
    private String            url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
