package it.renren.spilder.util.other.automan;

/**
 * ��PPTimeContentObject.java��ʵ��������ƤƤʱ����Ļ�ȡ���ݿ��JSON������
 * 
 * @author fenglibin 2011-10-3 ����04:02:23
 */
public class PPTimeContentObject {

    boolean  error;
    Rowset[] rowset;
    Pages    pages;
    Setting  setting;
    int      lastTimer;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Rowset[] getRowset() {
        return rowset;
    }

    public void setRowset(Rowset[] rowset) {
        this.rowset = rowset;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public int getLastTimer() {
        return lastTimer;
    }

    public void setLastTimer(int lastTimer) {
        this.lastTimer = lastTimer;
    }

}
