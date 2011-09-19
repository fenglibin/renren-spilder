package it.renren.spilder.main.config;

import java.util.List;

public class Content extends ContentRangeWithReplace {

    private List   startList;
    private List   endList;
    private String WashContent;
    private String handler;
    private int    minLength;
    private int    separatePageMaxPages;
    private String separatePageUrlSuffix;

    public List getEndList() {
        return endList;
    }

    public void setEndList(List endList) {
        this.endList = endList;
    }

    public List getStartList() {
        return startList;
    }

    public void setStartList(List startList) {
        this.startList = startList;
    }

    public String getWashContent() {
        return WashContent;
    }

    public void setWashContent(String washContent) {
        WashContent = washContent;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getSeparatePageMaxPages() {
        return separatePageMaxPages;
    }

    public void setSeparatePageMaxPages(int separatePageMaxPages) {
        this.separatePageMaxPages = separatePageMaxPages;
    }

    public String getSeparatePageUrlSuffix() {
        return separatePageUrlSuffix;
    }

    public void setSeparatePageUrlSuffix(String separatePageUrlSuffix) {
        this.separatePageUrlSuffix = separatePageUrlSuffix;
    }

}
