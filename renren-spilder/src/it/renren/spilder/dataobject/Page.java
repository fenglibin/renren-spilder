package it.renren.spilder.dataobject;

/**
 * ���ڴ��ݷ�ҳ�Ĳ���<br>
 * ��Page.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2012-6-17 ����11:10:18
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
