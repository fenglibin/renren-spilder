package it.renren.spilder.dataobject;

import java.io.Serializable;

public class ArchivesDO implements Serializable {

    private static final long serialVersionUID = 4034609900066350370L;
    int                       id;
    int                       typeid;
    int                       ismake;
    int                       channel;
    int                       click;
    String                    title;
    String                    writer;
    String                    source;
    int                       pubdate;
    int                       senddate;
    int                       sortrank;
    int                       mid;
    String                    keywords;
    String                    description;
    int                       weight;
    int                       dutyadmin;
    String                    flag;
    String                    litpic;
    String                    filename;
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

    public int getIsmake() {
        return ismake;
    }

    public void setIsmake(int ismake) {
        this.ismake = ismake;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPubdate() {
        return pubdate;
    }

    public void setPubdate(int pubdate) {
        this.pubdate = pubdate;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDutyadmin() {
        return dutyadmin;
    }

    public void setDutyadmin(int dutyadmin) {
        this.dutyadmin = dutyadmin;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
