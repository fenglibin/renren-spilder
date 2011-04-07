package it.renren.spilder.dataobject;

import java.io.Serializable;

public class ArctinyDO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5860383479204850250L;
    int                       id;
    int                       typeid;
    int                       channel;
    int                       senddate;
    int                       sortrank;
    int                       mid;
    // 表前缀，如人人IT网的表前缀为renren，人人IT繁体网的表前缀是renrenfanti_，但是表结构是一样的
    // 只是表前缀不一样，为了一个对象适合多种表，因而表前缀在查询的时候，需要传入
    String                    tablePrefix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getSenddate() {
        return senddate;
    }

    public void setSenddate(int senddate) {
        this.senddate = senddate;
    }

    public int getSortrank() {
        return sortrank;
    }

    public void setSortrank(int sortrank) {
        this.sortrank = sortrank;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
