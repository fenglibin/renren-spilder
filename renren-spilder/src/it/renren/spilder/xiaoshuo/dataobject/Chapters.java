package it.renren.spilder.xiaoshuo.dataobject;

import java.util.Date;

public class Chapters {

    private Integer id;

    private Integer bookId;

    private String  title;

    private Date    intime;

    private Boolean isgenhtml;

    private String  context;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Boolean getIsgenhtml() {
        return isgenhtml;
    }

    public void setIsgenhtml(Boolean isgenhtml) {
        this.isgenhtml = isgenhtml;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }
}
