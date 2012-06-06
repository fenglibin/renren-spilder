package it.renren.spilder.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

/**
 * ��ProxyServerUtil.java��ʵ����������ȡ��ǰ���ܴ���Ĺ�����
 * 
 * @author Administrator 2011-6-12 ����09:20:49
 */
public class ProxyServerUtil {

    private static List<String> proxyServerList;

    private static void initProxyServerList() throws HttpException, IOException {
        if (proxyServerList != null) {
            return;
        }
        String proxyListServer = "http://aliveproxy.com/proxy-list-port-8080";
        String proxyListServerEncode = "iso-8859-1";
        String content = HttpClientUtil.getGetResponseWithHttpClient(proxyListServer, proxyListServerEncode,
                                                                     Boolean.FALSE);
        String start = "Smart<br>traceroute";
        String end = "</tr></TABLE><br><center>";
        content = StringUtil.subString(content, start, end);
        String proxyBeforeString = "<tr valign=center class=\"cw-list\"><td  class=\"dt-tb2\" >";
        String proxyEndStrng = "<br>";
        proxyServerList = StringUtil.getListFromStart2End(content, proxyBeforeString, proxyEndStrng, Boolean.FALSE);

    }

    public static List<String> getProxyServerList() throws HttpException, IOException {
        if (proxyServerList == null) {
            initProxyServerList();

        }
        return proxyServerList;
    }

    /**
     * ��ȡһ������Ĵ���
     * 
     * @return
     */
    public static String getRandomProxy() {
        if (proxyServerList == null || proxyServerList.size() == 0) {
            return null;
        }
        int num = proxyServerList.size();
        int index = (int) (Math.random() * num);
        if (index >= num) {
            index = num - 1;
        }
        return proxyServerList.get(index);
    }

    private void checkProxy() {

    }

    public static void main(String[] args) throws HttpException, IOException {
        getProxyServerList();
        System.out.println("-=");
        System.out.println(getRandomProxy());
    }

}
