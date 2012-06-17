package it.renren.spilder.dataobject;

/**
 * 用于传递分页的参数<br>
 * 类Page.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2012-6-17 上午11:10:18
 */
public class Page {

    private int    start;
    private int    pageSize;
    private String tablePrefix;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}
