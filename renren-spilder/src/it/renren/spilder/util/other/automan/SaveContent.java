package it.renren.spilder.util.other.automan;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.HttpClientUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 类SaveContent.java的实现描述：获取皮皮时光机的内容并保存
 * 
 * @author fenglibin 2011-10-3 下午04:54:24
 */
public class SaveContent {

    protected static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                    new String[] { Constants.SPRING_CONFIG_FILE });

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     */
    public static void main(String[] args) throws HttpException, IOException {
        // TODO Auto-generated method stub
        String cookie = "__utma=56876395.1779520664.1317621310.1317621310.1317621310.1; __utmc=56876395; __utmz=56876395.1317621310.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); j2rZ_2132_auth=e05agqeNrR0PxhhSMLXFRQMWPdI%2Behd%2F5T9wbObLrl8gVl%2B9osb1s83idVVoY2i8jGzwxxdEKd0KAeKfGo%2FUlVHqLYA06u%2FDmqdGMl8o5Dy06kzcaFdnXYo";
        String url = "http://weibo.pp.cc/time/index.php?mod=content&action=show&account=2363930463&random=12&cid=0&tid=1&page=1";
        String encode = "utf-8";
        String content = HttpClientUtil.getGetResponseWithHttpClient(url, encode, Boolean.FALSE, cookie);
        System.out.println(content);
        PPTimeContentObject p = JSONObject.parseObject(content, PPTimeContentObject.class);
        System.out.println(p.getLastTimer());
    }

}
