package it.renren.spilder.yayamai;

import it.renren.spilder.util.log.Log4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0 ����ʱ�䣺2010-3-1 ����10:11:22 ��˵��
 */
class RemoteFaBu {

    private static Log4j log4j = new Log4j(RemoteFaBu.class.getName());

    /* ֱ�ӽ���ȡ�����ݷ��������ַ��� */
    public static void doFaBu(int recordNum) {
        try {
            // ����URL
            String REQUEST_URL = "http://www.yayamai.com/remotefabu.asp";
            // ���󷽷�
            final String REQUEST_MOTHOD = "POST";
            // ����URL��HttpURLConnection����
            HttpURLConnection httpConn = null;
            log4j.logDebug("��ʼԶ�̷�����¼:" + recordNum);
            Connection conn = ODBCConnection.getConnectionByODBC();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select top "
                                           + recordNum
                                           + " siteinfo.*,linktypes.typename,users.qq,users.email,users.password,users.regdate FROM (linktypes INNER JOIN siteinfo ON linktypes.id = siteinfo.sitetype) INNER JOIN users ON siteinfo.userid = users.id order by siteinfo.id desc");
            while (rs.next()) {
                try {
                    String requestParameter = "";
                    requestParameter += "siteurl=" + rs.getString("siteurl");
                    requestParameter += "&sitename=" + rs.getString("sitename");
                    requestParameter += "&shoulu_baidu=" + rs.getString("shoulu_baidu");
                    requestParameter += "&index_photo_date=" + rs.getString("index_photo_date");
                    requestParameter += "&shoulu_google=" + rs.getString("shoulu_google");
                    requestParameter += "&pagerank=" + rs.getString("pagerank");
                    requestParameter += "&addtime=" + rs.getString("addtime").replace(" ", "|");
                    requestParameter += "&alexa=" + rs.getString("alexa");
                    requestParameter += "&trantype=" + rs.getString("trantype");
                    requestParameter += "&linktype=" + rs.getString("linktype");
                    requestParameter += "&linkposition=" + rs.getString("linkposition");
                    requestParameter += "&sitetype=" + rs.getString("sitetype");
                    requestParameter += "&price=" + rs.getString("price");
                    requestParameter += "&req_shoulu_baidu=" + rs.getString("req_shoulu_baidu");
                    requestParameter += "&req_index_photo_date=" + rs.getString("req_index_photo_date");
                    requestParameter += "&req_pr=" + rs.getString("req_pr");
                    requestParameter += "&req_outlink_num=" + rs.getString("req_outlink_num");
                    requestParameter += "&req_aleax=" + rs.getString("req_aleax");
                    requestParameter += "&qq=" + rs.getString("qq");
                    requestParameter += "&remark=" + rs.getString("remark");
                    requestParameter += "&userid=" + rs.getString("userid");
                    requestParameter += "&email=" + rs.getString("email");
                    requestParameter += "&password=" + rs.getString("password");
                    requestParameter += "&regdate=" + rs.getString("regdate");
                    requestParameter += "&action=savefromremote";

                    httpConn = (HttpURLConnection) new URL(REQUEST_URL).openConnection();
                    httpConn.setRequestMethod(REQUEST_MOTHOD);
                    httpConn.setDoOutput(true);
                    httpConn.getOutputStream().write(requestParameter.getBytes());
                    httpConn.getOutputStream().flush();
                    httpConn.getOutputStream().close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    String lineStr = "";
                    String result = "";
                    while ((lineStr = br.readLine()) != null) {
                        result += lineStr;
                    }
                    log4j.logDebug("�������:" + result);
                    Thread.sleep(3000);
                } catch (Exception e) {
                    log4j.logDebug("������ʱ�����쳣��");
                    log4j.logError(e);
                }
            }
            st.close();
            conn.close();
            log4j.logDebug("Զ�̷�������");
        } catch (Exception e) {
            log4j.logError(e);
        }
    }

    /* ֱ�ӽ���ȡ�����ݷ��������ַ��� */
    public static void doFaBu(int recordNum, int alreadPublished) {
        try {
            int published = 0;
            // ����URL
            String REQUEST_URL = "http://www.yayamai.com/remotefabu.asp";
            // ���󷽷�
            final String REQUEST_MOTHOD = "POST";
            // ����URL��HttpURLConnection����
            HttpURLConnection httpConn = null;
            log4j.logDebug("��ʼԶ�̷�����¼:" + recordNum + ",�Ѿ�������:" + alreadPublished + "����¼");
            Connection conn = ODBCConnection.getConnectionByODBC();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select top "
                                           + recordNum
                                           + " siteinfo.*,linktypes.typename,users.qq,users.email,users.password,users.regdate FROM (linktypes INNER JOIN siteinfo ON linktypes.id = siteinfo.sitetype) INNER JOIN users ON siteinfo.userid = users.id order by siteinfo.id desc");
            while (rs.next()) {
                published++;
                if (published <= alreadPublished) {
                    continue;
                }
                log4j.logDebug("��ʼ������:" + published + "��¼.");
                String requestParameter = "";
                requestParameter += "siteurl=" + rs.getString("siteurl");
                requestParameter += "&sitename=" + rs.getString("sitename");
                requestParameter += "&shoulu_baidu=" + rs.getString("shoulu_baidu");
                requestParameter += "&index_photo_date=" + rs.getString("index_photo_date");
                requestParameter += "&shoulu_google=" + rs.getString("shoulu_google");
                requestParameter += "&pagerank=" + rs.getString("pagerank");
                requestParameter += "&addtime=" + rs.getString("addtime").replace(" ", "|");
                requestParameter += "&alexa=" + rs.getString("alexa");
                requestParameter += "&trantype=" + rs.getString("trantype");
                requestParameter += "&linktype=" + rs.getString("linktype");
                requestParameter += "&linkposition=" + rs.getString("linkposition");
                requestParameter += "&sitetype=" + rs.getString("sitetype");
                requestParameter += "&price=" + rs.getString("price");
                requestParameter += "&req_shoulu_baidu=" + rs.getString("req_shoulu_baidu");
                requestParameter += "&req_index_photo_date=" + rs.getString("req_index_photo_date");
                requestParameter += "&req_pr=" + rs.getString("req_pr");
                requestParameter += "&req_outlink_num=" + rs.getString("req_outlink_num");
                requestParameter += "&req_aleax=" + rs.getString("req_aleax");
                requestParameter += "&qq=" + rs.getString("qq");
                requestParameter += "&remark=" + rs.getString("remark");
                requestParameter += "&userid=" + rs.getString("userid");
                requestParameter += "&email=" + rs.getString("email");
                requestParameter += "&password=" + rs.getString("password");
                requestParameter += "&regdate=" + rs.getString("regdate");
                requestParameter += "&action=savefromremote";
                try {
                    httpConn = (HttpURLConnection) new URL(REQUEST_URL).openConnection();
                    httpConn.setRequestMethod(REQUEST_MOTHOD);
                    httpConn.setDoOutput(true);
                    httpConn.getOutputStream().write(requestParameter.getBytes());
                    httpConn.getOutputStream().flush();
                    httpConn.getOutputStream().close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    String lineStr = "";
                    String result = "";
                    while ((lineStr = br.readLine()) != null) {
                        result += lineStr;
                    }
                    log4j.logDebug("�������:" + result);
                } catch (Exception e) {
                    log4j.logDebug("��ǰ���������쳣��");
                    log4j.logError(e);
                }
                Thread.sleep(1000);
            }
            st.close();
            conn.close();
            log4j.logDebug("Զ�̷�������");
        } catch (Exception e) {
            log4j.logError(e);
        }
    }

    public static void main(String[] args) {
        doFaBu(8000, 1);
    }
}
