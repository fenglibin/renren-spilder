package it.renren.spilder.dataobject;

import java.io.Serializable;

public class ArctinyDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5860383479204850250L;
	int id;
	int typeid;
	int channel;
	int senddate;
	int sortrank;
	int mid;

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
}
