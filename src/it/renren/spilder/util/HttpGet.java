package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient��ȡҳ���ʹ������
 * 
 * @author ������(java2000.net)
 * 
 */
public class HttpGet {
	private static Log4j log4j = new Log4j(HttpGet.class.getName());
	public static void main(String[] args) throws Exception {

		HttpParams params = new BasicHttpParams();
		// HTTP Э��İ汾,1.1/1.0/0.9
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// �ַ���
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		// αװ�����������
		// IE7 ��
		// Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)
		//
		// Firefox3.03
		// Mozilla/5.0 (Windows; U; Windows NT 5.2; zh-CN; rv:1.9.0.3)
		// Gecko/2008092417 Firefox/3.0.3
		//
		HttpProtocolParams.setUserAgent(params,
				"Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
		HttpProtocolParams.setUseExpectContinue(params, true);
		BasicHttpProcessor httpproc = new BasicHttpProcessor();
		httpproc.addInterceptor(new RequestContent());
		httpproc.addInterceptor(new RequestTargetHost());

		httpproc.addInterceptor(new RequestConnControl());
		httpproc.addInterceptor(new RequestUserAgent());
		httpproc.addInterceptor(new RequestExpectContinue());

		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpContext context = new BasicHttpContext(null);
		HttpHost host = new HttpHost("blog.csdn.net", 80);

		DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
		ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

		context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
		context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

		try {

			String[] targets = { "/",
					"/cping1982/archive/2010/08/14/5811779.aspx" };

			for (int i = 0; i < targets.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(),
							host.getPort());
					conn.bind(socket, params);
				}
				BasicHttpRequest request = new BasicHttpRequest("GET",
						targets[i]);
				log4j.logDebug(">> Request URI: "
						+ request.getRequestLine().getUri());

				context.setAttribute(ExecutionContext.HTTP_REQUEST, request);
				request.setParams(params);
				httpexecutor.preProcess(request, httpproc, context);
				HttpResponse response = httpexecutor.execute(request, conn,
						context);
				response.setParams(params);
				httpexecutor.postProcess(response, httpproc, context);

				// ������
				log4j.logDebug("<< Response: " + response.getStatusLine());
				// ���ص��ļ�ͷ��Ϣ
				Header[] hs = response.getAllHeaders();
				for (Header h : hs) {
					log4j.logDebug(h.getName() + ":" + h.getValue());
				}
				// ���������Ϣ
				log4j.logDebug(EntityUtils.toString(response.getEntity()));
				log4j.logDebug("==============");
				if (!connStrategy.keepAlive(response, context)) {
					conn.close();
				} else {
					log4j.logDebug("Connection kept alive...");
				}
			}
		} finally {
			conn.close();
		}
	}
}