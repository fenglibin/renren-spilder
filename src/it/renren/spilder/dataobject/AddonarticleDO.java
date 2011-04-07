package it.renren.spilder.dataobject;

import java.io.Serializable;

public class AddonarticleDO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8042919137539869539L;
    int                       aid;
    int                       typeid;
    String                    body;
    // 表前缀，如人人IT网的表前缀为renren，人人IT繁体网的表前缀是renrenfanti_，但是表结构是一样的
    // 只是表前缀不一样，为了一个对象适合多种表，因而表前缀在查询的时候，需要传入
    String                    tablePrefix;

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
