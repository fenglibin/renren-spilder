package it.renren.spilder.util;

import it.renren.spilder.main.Environment;
import it.renren.spilder.util.log.Log4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

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

    /**
     * 返回默认的Http Client Header
     * 
     * @throws IOException
     */
    public static List<Header> getHttpDefaultHeader(String referer) throws IOException {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        headers.add(new Header("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3"));
        // headers.add(new Header("Accept-Encoding", "gzip,deflate,sdch"));
        // 如果支持gzip，那么就要对获取的内容进行判断是否是gzip格式，否则会出错
        headers.add(new Header("Accept-Encoding", "gzip,deflate,sdch"));
        headers.add(new Header("Accept-Language", "zh-CN,zh;q=0.8"));
        headers.add(new Header("Cache-Contro", "max-age=0"));
        headers.add(new Header("Connection", "keep-alive"));
        String cookie = null;
        if (!StringUtil.isEmpty(Environment.cookFile)) {
            cookie = FileUtil.getFileContent(Environment.cookFile);
            if (!StringUtil.isEmpty(cookie)) {
                headers.add(new Header("Cookie", cookie));
            }
        }
        if (StringUtil.isEmpty(Environment.referer)) {
            headers.add(new Header("Referer", referer));
        } else {
            headers.add(new Header("Referer", Environment.referer));
        }
        headers.add(new Header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.8 (KHTML, like Gecko; Google Web Preview) Chrome/19.0.1084.36 Safari/536.8"));
        return headers;
    }

    public static HttpClient getHttpClient(String referer) throws IOException {
        HttpClient client = new HttpClient(manager);
        client.getHostConfiguration().getParams().setParameter("http.default-headers", getHttpDefaultHeader(referer));
        if (initialed) {
            HttpClientUtil.SetPara();
        }
        return client;
    }

    /**
     * 通过get方法获取网页内容，通过代理的方式
     * 
     * @param url 获取内容的URL
     * @param encode 待获取内容的编码
     * @return 根据当前的URL，获取到的网页的内容
     * @throws IOException
     * @throws HttpException
     */
    // 通过get方法获取网页内容
    public static String getGetResponseWithHttpClient(String url, String encode) throws HttpException, IOException {
        return getGetResponseWithHttpClient(url, encode, Environment.isUseProxy);
    }

    public static String getGetResponseWithHttpClient(String url, String encode, boolean byProxy) throws HttpException, IOException {
        String cookie = null;
        if (!StringUtil.isEmpty(Environment.cookFile)) {
            cookie = FileUtil.getFileContent(Environment.cookFile);
        }
        return getGetResponseWithHttpClient(url, encode, byProxy, cookie);
    }

    /**
     * 通过get方法获取网页内容
     * 
     * @param url 获取内容的URL
     * @param encode 待获取内容的编码
     * @param byProxy 是否通过代理的方式
     * @return 根据当前的URL，获取到的网页的内容
     * @throws IOException
     * @throws HttpException
     */
    public static String getGetResponseWithHttpClient(String url, String encode, boolean byProxy, String cookie) throws HttpException, IOException {
        HttpClient client = new HttpClient(manager);
        if (byProxy) {
            // 设置代理开始
            String proxy = getProxy();
            if (!StringUtil.isEmpty(proxy)) {
                String[] hostArray = proxy.split(":");
                client.getHostConfiguration().setProxy(hostArray[0], Integer.parseInt(hostArray[1]));
                client.getParams().setAuthenticationPreemptive(true);
            }
            // 设置代理结束
        }
        client.getHostConfiguration().getParams().setParameter("http.default-headers", getHttpDefaultHeader(url));
        if (initialed) {
            HttpClientUtil.SetPara();
        }
        GetMethod get = new GetMethod(UrlUtil.prettyUrl(url));
        get.setFollowRedirects(true);

        String result = null;
        StringBuffer resultBuffer = new StringBuffer();

        try {

            client.executeMethod(get);
            String contentEncoding = null;
            Header header = get.getResponseHeader(it.renren.spilder.main.Constants.HttpHeader.CONTENT_ENCODING);
            if (header != null) {
                contentEncoding = get.getResponseHeader(it.renren.spilder.main.Constants.HttpHeader.CONTENT_ENCODING).getValue();
            }
            InputStream inputStream = null;
            if (!StringUtil.isEmpty(contentEncoding) && contentEncoding.equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(get.getResponseBodyAsStream());
            } else {
                inputStream = get.getResponseBodyAsStream();
            }

            // 在目标页面情况未知的条件下，不推荐使用getResponseBodyAsString()方法
            // String strGetResponseBody = post.getResponseBodyAsString();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, get.getResponseCharSet()));

            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\n");
            }

            in.close();

            result = resultBuffer.toString();

            // iso-8859-1 is the default reading encode
            result = HttpClientUtil.ConverterStringCode(resultBuffer.toString(), get.getResponseCharSet(), encode);

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

            BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post.getResponseCharSet()));
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
            BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post.getResponseCharSet()));
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

    private static String ConverterStringCode(String source, String srcEncode, String destEncode) throws UnsupportedEncodingException {

        return new String(source.getBytes(srcEncode), destEncode);

    }

    /**
     * 获取代理
     * 
     * @return
     */
    private static String getProxy() {
        String proxy = null;
        if (!StringUtil.isEmpty(Environment.proxy)) {
            proxy = Environment.proxy;
        } else {
            proxy = ProxyServerUtil.getRandomProxy();
        }
        return proxy;
    }

    public static void main(String[] arg) throws HttpException, IOException {
        String url = "http://weibo.pp.cc/time/index.php?mod=content&action=list&account=2363930463&tid=0&page=2";
        String encode = "utf-8";
        String content = getGetResponseWithHttpClient(url,
                                                      encode,
                                                      false,
                                                      "__utma=56876395.1779520664.1317621310.1317621310.1317621310.1; __utmc=56876395; __utmz=56876395.1317621310.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); j2rZ_2132_auth=e05agqeNrR0PxhhSMLXFRQMWPdI%2Behd%2F5T9wbObLrl8gVl%2B9osb1s83idVVoY2i8jGzwxxdEKd0KAeKfGo%2FUlVHqLYA06u%2FDmqdGMl8o5Dy06kzcaFdnXYo");
        System.out.println(content);
    }
}
