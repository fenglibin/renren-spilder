package it.renren.spilder.dataobject;

import java.io.Serializable;

public class AddonarticleDO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8042919137539869539L;
    private int               aid;
    private int               typeid;
    private String            body;
    private String            tablePrefix;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}
