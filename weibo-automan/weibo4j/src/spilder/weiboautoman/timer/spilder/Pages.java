package weiboautoman.timer.spilder;

public class Pages {

    int   curpage;
    int   realpages;
    int   firstpage;
    int   prevpage;
    int   nextpage;
    int   lastpage;
    int[] multi;

    public int getCurpage() {
        return curpage;
    }

    public void setCurpage(int curpage) {
        this.curpage = curpage;
    }

    public int getRealpages() {
        return realpages;
    }

    public void setRealpages(int realpages) {
        this.realpages = realpages;
    }

    public int getFirstpage() {
        return firstpage;
    }

    public void setFirstpage(int firstpage) {
        this.firstpage = firstpage;
    }

    public int getPrevpage() {
        return prevpage;
    }

    public void setPrevpage(int prevpage) {
        this.prevpage = prevpage;
    }

    public int getNextpage() {
        return nextpage;
    }

    public void setNextpage(int nextpage) {
        this.nextpage = nextpage;
    }

    public int getLastpage() {
        return lastpage;
    }

    public void setLastpage(int lastpage) {
        this.lastpage = lastpage;
    }

    public int[] getMulti() {
        return multi;
    }

    public void setMulti(int[] multi) {
        this.multi = multi;
    }

}
