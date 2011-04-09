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
 * @version 1.0 创建时间：2010-3-1 上午10:11:22 类说明
 */
class RemoteFaBu {

    private static Log4j log4j = new Log4j(RemoteFaBu.class.getName());

    /* 直接将获取到数据发布到乐乐发发 */
    public static void doFaBu(int recordNum) {
        try {
            // 请求URL
            String REQUEST_URL = "http://www.yayamai.com/remotefabu.asp";
            // 请求方法
            final String REQUEST_MOTHOD = "POST";
            // 连接URL的HttpURLConnection对象
            HttpURLConnection httpConn = null;
            log4j.logDebug("开始远程发布记录:" + recordNum);
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
                    log4j.logDebug("发布结果:" + result);
                    Thread.sleep(3000);
                } catch (Exception e) {
                    log4j.logDebug("发布的时候发生异常：");
                    log4j.logError(e);
                }
            }
            st.close();
            conn.close();
            log4j.logDebug("远程发布结束");
        } catch (Exception e) {
            log4j.logError(e);
        }
    }

    /* 直接将获取到数据发布到乐乐发发 */
    public static void doFaBu(int recordNum, int alreadPublished) {
        try {
            int published = 0;
            // 请求URL
            String REQUEST_URL = "http://www.yayamai.com/remotefabu.asp";
            // 请求方法
            final String REQUEST_MOTHOD = "POST";
            // 连接URL的HttpURLConnection对象
            HttpURLConnection httpConn = null;
            log4j.logDebug("开始远程发布记录:" + recordNum + ",已经发布了:" + alreadPublished + "条记录");
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
                log4j.logDebug("发始发布第:" + published + "记录.");
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
                    log4j.logDebug("发布结果:" + result);
                } catch (Exception e) {
                    log4j.logDebug("当前发布发生异常：");
                    log4j.logError(e);
                }
                Thread.sleep(1000);
            }
            st.close();
            conn.close();
            log4j.logDebug("远程发布结束");
        } catch (Exception e) {
            log4j.logError(e);
        }
    }

    public static void main(String[] args) {
        doFaBu(8000, 1);
    }
}
