package it.renren.spilder.xiaoshuo.dataobject;

import java.util.Date;

public class Downurl extends it.renren.spilder.dataobject.DownurlDO {

    private String url;

    private Date   intime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}
