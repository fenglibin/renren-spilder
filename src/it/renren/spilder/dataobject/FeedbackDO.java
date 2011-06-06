package it.renren.spilder.dataobject;

import java.io.Serializable;

public class FeedbackDO implements Serializable {

    private static final long serialVersionUID = -6851849260595850115L;
    /* ���µ�ID */
    private int               aid;
    /* ��ǰ���µ�����ID */
    private int               typeid;
    /* �����˵����� */
    private String            username;
    /* ��ǰ���µı��� */
    private String            arctitle;
    /* �����˵�IP */
    private String            ip;
    /* �����Ƿ��Ѿ������ */
    private int               ischeck;
    /* �������� */
    private int               dtime;
    /* ���� */
    private int               mid;
    /* ���� */
    private int               bad;
    /* ���� */
    private int               good;
    /* �û����������ͣ�Ĭ��Ϊfeedback */
    private int               ftype;
    /* ���飬Ĭ��Ϊ1 */
    private int               face;
    /* ���۵����� */
    private String            msg;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArctitle() {
        return arctitle;
    }

    public void setArctitle(String arctitle) {
        this.arctitle = arctitle;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getIscheck() {
        return ischeck;
    }

    public void setIscheck(int ischeck) {
        this.ischeck = ischeck;
    }

    public int getDtime() {
        return dtime;
    }

    public void setDtime(int dtime) {
        this.dtime = dtime;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
