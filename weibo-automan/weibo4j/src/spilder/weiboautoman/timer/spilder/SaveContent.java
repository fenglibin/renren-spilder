package weiboautoman.timer.spilder;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import weiboautoman.timer.core.Constants;
import weiboautoman.timer.dao.MsgDAO;
import weiboautoman.timer.dataobject.Msg;
import weiboautoman.timer.util.CodeUtil;
import weiboautoman.timer.util.FileUtil;
import weiboautoman.timer.util.HttpClientUtil;

/**
 * 类SaveContent.java的实现描述：获取皮皮时光机的内容并保存
 * 
 * @author fenglibin 2011-10-3 ����04:54:24
 */
public class SaveContent {

    private static final ConfigurableApplicationContext ctx    = new FileSystemXmlApplicationContext(
                                                                                                     new String[] { Constants.SPRING_CONFIG_FILE });
    private static MsgDAO                               msgDAO = (MsgDAO) ctx.getBean("msgDAO");
    private static Logger                               log4j  = LoggerFactory.getLogger(SaveContent.class);

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ctx.close();
            }
        });
        String content = "";
        try {
            // String cookie = FileUtil.getFileContent(args[0].trim());
            String cookie = "__utma=56876395.514759198.1316249300.1317717397.1317797402.11; __utmz=56876395.1317797402.11.2.utmcsr=api.t.sina.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/oauth/authorize; Hm_lvt_240de918a00b0e609c0e7b5c81bbf561=1321268230968; j2rZ_2132_sid=6SqPt8tsiPsZQj8z; pp_weibo_content_time=; j2rZ_2132_auth=db37pZpZUSKSq3d9%2BBh4GnPgnmDrYDiYg3iUk6Ons7%2Fq9mRKWFSDRbpnvCd5L7yWB13oyd4qCmRqynZuJXoyVoR1j1DY; Hm_lpvt_240de918a00b0e609c0e7b5c81bbf561=1321268271280";
            cookie = cookie.trim();
            log4j.warn("cookie:" + cookie);
            String encode = "utf-8";
            for (int type_id = 1; type_id <= 22; type_id++) {
                if (type_id == 18) {
                    continue;
                }
                String url = "http://weibo.pp.cc/time/index.php?mod=content&action=show&account=2363930463&random=279&cid=0&page=1&tid="
                             + type_id;
                log4j.warn(url);
                content = HttpClientUtil.getGetResponseWithHttpClient(url, encode, cookie);
                PPTimeContentObject contentObject = (PPTimeContentObject) JSONObject.toBean(JSONObject.fromObject(content),
                                                                                            PPTimeContentObject.class);
                int totalPages = contentObject.getPages().getLastpage();
                for (int i = 1; i <= totalPages; i++) {
                    log4j.warn(url);
                    url = "http://weibo.pp.cc/time/index.php?mod=content&action=show&account=2363930463&random=279&cid=0&tid="
                          + type_id + "&page=" + i;
                    content = HttpClientUtil.getGetResponseWithHttpClient(url, encode, cookie);
                    contentObject = (PPTimeContentObject) JSONObject.toBean(JSONObject.fromObject(content),
                                                                            PPTimeContentObject.class);
                    for (Rowset row : contentObject.getRowset()) {
                        saveContent(type_id, row);
                    }
                }
            }
        } catch (Exception e) {
            if (log4j.isErrorEnabled()) {
                log4j.error("error happened,content:" + content, e);
            }
        }

    }

    public static void saveContent(int type_id, Rowset row) {
        try {
            log4j.warn("saving one message.." + new Date());
            Msg msgDO = new Msg();
            msgDO.setContent(CodeUtil.ascii2Native(row.getContent()));
            msgDO.setId(Long.parseLong(row.getCid()));
            msgDO.setPicture("images/" + row.getPicture().getPath());
            msgDO.setSortno(1);
            msgDO.setTypeId(type_id);
            Msg msg = msgDAO.selectByPrimaryKey(Long.parseLong(row.getCid()));
            if (msg == null) {
                msgDAO.insert(msgDO);
            }
            saveImage(row.getPicture().getPath());
            log4j.warn("finish saved one message.." + new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(String imageSrc) {
        String imgUrl = "http://img2.pp.cc/attachment/weibo/";
        String fileName = FileUtil.getFileName(imageSrc);
        String imageSavePath = imageSrc.replace(fileName, "");
        File file = new File("images/" + imageSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File savedImage = new File(imageSrc);
        if (!savedImage.exists()) {
            log4j.warn("save image:" + (imgUrl + imageSrc));
            FileUtil.downloadFileByUrl(imgUrl + imageSrc, "images/" + imageSrc);
            /** 下载缩略图 */
            imageSrc += ".thumb.jpg";
            log4j.warn("save image:" + (imgUrl + imageSrc));
            FileUtil.downloadFileByUrl(imgUrl + imageSrc, "images/" + imageSrc);
        }
    }
}
