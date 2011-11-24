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
        String cookie = "__utma=56876395.514759198.1316249300.1317717397.1317797402.11; __utmz=56876395.1317797402.11.2.utmcsr=api.t.sina.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/oauth/authorize; Hm_lvt_240de918a00b0e609c0e7b5c81bbf561=1321268230968; j2rZ_2132_sid=6SqPt8tsiPsZQj8z; pp_weibo_content_time=; j2rZ_2132_auth=db37pZpZUSKSq3d9%2BBh4GnPgnmDrYDiYg3iUk6Ons7%2Fq9mRKWFSDRbpnvCd5L7yWB13oyd4qCmRqynZuJXoyVoR1j1DY; Hm_lpvt_240de918a00b0e609c0e7b5c81bbf561=1321268271280";
        String url = "http://weibo.pp.cc/time/index.php?mod=content&action=show&account=2363930463&random=12&cid=0&tid=1&page=1";
        String encode = "utf-8";
        String content = HttpClientUtil.getGetResponseWithHttpClient(url, encode, Boolean.FALSE, cookie);
        System.out.println(content);
        PPTimeContentObject p = JSONObject.parseObject(content, PPTimeContentObject.class);
        System.out.println(p.getLastTimer());
    }

}
