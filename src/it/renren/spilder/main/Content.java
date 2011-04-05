package it.renren.spilder.main;

import java.util.List;

public class Content extends ContentRangeWithReplace{
	private List startList;
	private List endList;
	private String WashContent;
	private String handler;
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
}
