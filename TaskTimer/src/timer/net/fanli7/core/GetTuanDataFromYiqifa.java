package net.fanli7.core;

import it.renren.timer.util.StringUtil;

import java.util.List;

import net.fanli7.dao.FanliMallDAO;
import net.fanli7.dataobject.FanliMall;
import open4j.OpenYiqifa;
import open4j.OpenYiqifaException;
import open4j.data.GwkTuanWebSite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类GetTuanDataFromYiqifa.java的实现描述：获取团购商城的数据
 * 
 * @author Administrator 2012-11-19 上午08:00:29
 */
public class GetTuanDataFromYiqifa {

    private static Logger log  = LoggerFactory.getLogger(GetTuanDataFromYiqifa.class);
    FanliMallDAO          fanliMallDAO;
    OpenYiqifa            open = new OpenYiqifa();

    public void start() {
        System.out.println("开始获取一起发团购商城数据...");
        open.setOAuthConsumer(Constants.YIQIFA_CUSTOM_KEY, Constants.YIQIFA_CUSTOM_SECRECT);
        try {
            List<GwkTuanWebSite> listTuan = getTuanWebSite();
            for (GwkTuanWebSite c : listTuan) {
                FanliMall mall = new FanliMall();
                // 广告主名称
                mall.setTitle(c.getName());
                // URL
                mall.setYiqifaurl(c.getUnionUrl());
                // 广告主ID
                String cid = StringUtil.subString(mall.getYiqifaurl(), "&c=", "&");
                mall.setYiqifaid(Integer.parseInt(cid));
                mall.setFan(getRandom() + "%");
                mall.setImg("");
                mall.setLm((byte) 3);
                mall.setCid(21);
                mall.setType(Boolean.TRUE);
                // mall.setAddtime(0);
                mall.setSort(100);
                // mall.setEdate(0L);
                mall.setRenzheng(Boolean.TRUE);
                mall.setScore(0.00);
                mall.setPjnum(0);

                fanliMallDAO.insert(mall);

            }
            System.out.println("获取一起发团购商城数据成功结束。");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            System.out.println("获取一起发团购商城数据发生异常：" + e.getMessage());
        }
    }

    private List<GwkTuanWebSite> getTuanWebSite() throws OpenYiqifaException {
        List<GwkTuanWebSite> list = open.getGwkTuanWibsetList();
        return list;
    }

    /**
     * 获取随机数
     * 
     * @return
     */
    private int getRandom() {
        int random = 0;
        random = (int) (Math.random() * 15);
        if (random < 3) {
            random = random + 5;
        } else if (random < 5) {
            random = random + 3;
        }
        return random;
    }

    public void setFanliMallDAO(FanliMallDAO fanliMallDAO) {
        this.fanliMallDAO = fanliMallDAO;
    }

}
