package weiboautoman.timer.util;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.Header;

/**
 * @author fenglibin
 */
public class HttpClientUtil {

    // 获得ConnectionManager，设置相关参数
    private static MultiThreadedHttpConnectionManager manager              = new MultiThreadedHttpConnectionManager();

    private static int                                connectionTimeOut    = 20000;

    private static int                                socketTimeOut        = 10000;

    private static int                                maxConnectionPerHost = 5;

    private static int                                maxTotalConnections  = 40;

    // 标志初始化是否完成的flag
    private static boolean                            initialed            = false;

    private static int                                TIME_OUT             = 15000;
    private static String                             DEFAULT_CHARSET      = "utf-8";

    private static Pattern                            pattern              = Pattern.compile(".*charset\\s*=\\s*(.*?)",
                                                                                             Pattern.CASE_INSENSITIVE);

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
    public static String getGetResponseWithHttpClient(String url, String encode, String cookie) throws HttpException,
                                                                                               IOException {
        HttpClient client = new HttpClient(manager);

        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("User-Agent",
                               "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1"));
        headers.add(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        headers.add(new Header("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3"));
        headers.add(new Header("Accept-Language", "zh-CN,zh;q=0.8"));
        headers.add(new Header("Accept-Encoding", "default"));
        headers.add(new Header("Cache-Control", "max-age=0"));
        headers.add(new Header("Connection", "keep-alive"));

        if (!StringUtil.isNull(cookie)) {
            headers.add(new Header("Cookie", cookie));
        }
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
            result = ConverterStringCode(resultBuffer.toString(), get.getResponseCharSet(), encode);

        } finally {
            get.releaseConnection();

        }
        return result;
    }

    private static String ConverterStringCode(String source, String srcEncode, String destEncode)
                                                                                                 throws UnsupportedEncodingException {

        return new String(source.getBytes(srcEncode), destEncode);

    }

    /**
     * 根据所给url读取远程主机相应内容
     * 
     * @param remoteUrl
     * @return 从参数url读取的内容文本,如果读取失败返回null 失败有2种原因: 网络断开,连接不到url主机 url拼写错误
     */
    public static String readUrl(String remoteUrl, String charset, String cookie) {
        BufferedReader content = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(remoteUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);

            conn.setRequestProperty("User-Agent",
                                    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "default");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Connection", "keep-alive");

            if (!StringUtil.isNull(cookie)) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.connect();
            if (StringUtil.isNull(charset)) {
                charset = getCharset(conn, DEFAULT_CHARSET);
            }
            content = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            StringBuffer result = new StringBuffer();
            for (String line = ""; line != null; line = content.readLine()) {
                result.append(line);
            }
            return result.toString();
        } catch (java.net.MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (java.net.UnknownHostException e) {
            // log.error("UnknownHostException:"+remoteUrl);
            return null;// 主机找不到多数是网络问题
        } catch (java.io.IOException ioe) {
            // log.error(ioe.getMessage()+":"+remoteUrl);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static String getCharset(URLConnection conn, String defaultCharset) {
        String contentType = conn.getContentType();
        if (contentType == null) {
            return defaultCharset;
        }
        Matcher matcher = pattern.matcher(contentType);
        if (matcher.matches()) {
            return matcher.group(1);// 第一个匹配项就是要找的
        }
        return defaultCharset;
    }

    public static void main(String[] arg) throws HttpException, IOException {
        String url = "http://weibo.pp.cc/time/index.php?mod=content&action=list&account=2363930463&tid=0&page=2";
        String encode = "utf-8";
        String content = getGetResponseWithHttpClient(url,
                                                      encode,
                                                      "__utma=56876395.1779520664.1317621310.1317621310.1317621310.1; __utmc=56876395; __utmz=56876395.1317621310.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); j2rZ_2132_auth=e05agqeNrR0PxhhSMLXFRQMWPdI%2Behd%2F5T9wbObLrl8gVl%2B9osb1s83idVVoY2i8jGzwxxdEKd0KAeKfGo%2FUlVHqLYA06u%2FDmqdGMl8o5Dy06kzcaFdnXYo");
        System.out.println(content);
    }
}
