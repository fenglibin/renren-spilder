package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.Header;

/**
 * @author steven
 */
public class HttpClientUtil {

    private static Log4j                              log4j                = new Log4j(HttpClientUtil.class.getName());
    // 获得ConnectionManager，设置相关参数
    private static MultiThreadedHttpConnectionManager manager              = new MultiThreadedHttpConnectionManager();

    private static int                                connectionTimeOut    = 20000;

    private static int                                socketTimeOut        = 10000;

    private static int                                maxConnectionPerHost = 5;

    private static int                                maxTotalConnections  = 40;

    // 标志初始化是否完成的flag
    private static boolean                            initialed            = false;

    // 初始化ConnectionManger的方法
    public static void SetPara() {
        manager.getParams().setConnectionTimeout(connectionTimeOut);
        manager.getParams().setSoTimeout(socketTimeOut);
        manager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        manager.getParams().setMaxTotalConnections(maxTotalConnections);

        initialed = true;
    }

    public static HttpClient getHttpClient() {
        HttpClient client = new HttpClient(manager);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", "Mozilla/3.0 (compatible; MSIE 6.0; Windows NT 6.1)"));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        if (initialed) {
            HttpClientUtil.SetPara();
        }
        return client;
    }

    // 通过get方法获取网页内容
    public static String getGetResponseWithHttpClient(String url, String encode) {
        HttpClient client = new HttpClient(manager);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent", "Mozilla/3.0 (compatible; MSIE 6.0; Windows NT 6.1)"));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        if (initialed) {
            HttpClientUtil.SetPara();
        }
        GetMethod get = new GetMethod(url);
        get.setFollowRedirects(true);

        String result = null;
        StringBuffer resultBuffer = new StringBuffer();

        try {

            client.executeMethod(get);

            // 在目标页面情况未知的条件下，不推荐使用getResponseBodyAsString()方法
            // String strGetResponseBody = post.getResponseBodyAsString();
            BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(),
                                                                         get.getResponseCharSet()));

            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\n");
            }

            in.close();

            result = resultBuffer.toString();

            // iso-8859-1 is the default reading encode
            result = HttpClientUtil.ConverterStringCode(resultBuffer.toString(), get.getResponseCharSet(), encode);
        } catch (Exception e) {
            log4j.logError(e);

            result = "";
        } finally {
            get.releaseConnection();

        }
        return result;
    }

    public static String getPostResponseWithHttpClient(String url, String encode) {
        HttpClient client = new HttpClient(manager);

        if (initialed) {
            HttpClientUtil.SetPara();
        }

        PostMethod post = new PostMethod(url);
        post.setFollowRedirects(false);

        StringBuffer resultBuffer = new StringBuffer();

        String result = null;

        try {
            client.executeMethod(post);

            BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),
                                                                         post.getResponseCharSet()));
            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\n");
            }

            in.close();

            // iso-8859-1 is the default reading encode
            result = HttpClientUtil.ConverterStringCode(resultBuffer.toString(), post.getResponseCharSet(), encode);
        } catch (Exception e) {
            log4j.logError(e);

            result = "";
        } finally {
            post.releaseConnection();

        }
        return result;
    }

    public static String getPostResponseWithHttpClient(String url, String encode, NameValuePair[] nameValuePair) {
        HttpClient client = new HttpClient(manager);

        if (initialed) {
            HttpClientUtil.SetPara();
        }

        PostMethod post = new PostMethod(url);
        post.setRequestBody(nameValuePair);
        post.setFollowRedirects(false);

        String result = null;
        StringBuffer resultBuffer = new StringBuffer();

        try {
            client.executeMethod(post);
            BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),
                                                                         post.getResponseCharSet()));
            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\n");
            }

            in.close();

            // iso-8859-1 is the default reading encode
            result = HttpClientUtil.ConverterStringCode(resultBuffer.toString(), post.getResponseCharSet(), encode);
        } catch (Exception e) {
            log4j.logError(e);

            result = "";
        } finally {
            post.releaseConnection();

        }
        return result;
    }

    private static String ConverterStringCode(String source, String srcEncode, String destEncode) {
        if (source != null) {
            try {
                return new String(source.getBytes(srcEncode), destEncode);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                log4j.logError(e);
                return "";
            }
        } else {
            return "";

        }
    }

    public static void main(String[] arg) {
        String content = getGetResponseWithHttpClient("http://www.ibm.com", "gbk");
        log4j.logDebug(content);
    }
}
