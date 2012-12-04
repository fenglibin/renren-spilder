package net.fanli7.core;

import it.renren.timer.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fanli7.dao.FanliMallDAO;
import net.fanli7.dao.FanliTypeDAO;
import net.fanli7.dataobject.FanliMall;
import net.fanli7.dataobject.FanliType;
import open4j.OpenYiqifa;
import open4j.OpenYiqifaException;
import open4j.data.Merchant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类GetDataFromYiqifa.java的实现描述：获取商城的数据
 * 
 * @author Administrator 2012-11-19 上午08:00:49
 */
public class GetDataFromYiqifa {

    private static Logger               log  = LoggerFactory.getLogger(GetDataFromYiqifa.class);
    FanliMallDAO                        fanliMallDAO;
    FanliTypeDAO                        fanliTypeDAO;
    OpenYiqifa                          open = new OpenYiqifa();
    private static Map<String, Integer> typeMap;

    public void start() {
        System.out.println("开始获取一起发商城数据...");
        open.setOAuthConsumer(Constants.YIQIFA_CUSTOM_KEY, Constants.YIQIFA_CUSTOM_SECRECT);
        try {
            List<Merchant> listMerchant = getMerchantList();
            for (Merchant c : listMerchant) {
                FanliMall mall = new FanliMall();
                // 广告主名称
                mall.setTitle(c.getName());
                // URL
                mall.setYiqifaurl(c.getUnionurl());
                // 广告主ID
                String cid = StringUtil.subString(mall.getYiqifaurl(), "&c=", "&");
                mall.setYiqifaid(Integer.parseInt(cid));
                mall.setFan(getRandom() + "%");
                mall.setImg(c.getLogo());
                mall.setLm((byte) 3);
                mall.setCid(getTypeId(c.getCat()));
                mall.setType(Boolean.TRUE);
                // mall.setAddtime(0);
                mall.setSort(100);
                // mall.setEdate(0L);
                mall.setRenzheng(Boolean.TRUE);
                mall.setScore(0.00);
                mall.setPjnum(0);

                fanliMallDAO.insert(mall);

            }
            System.out.println("获取一起发商城数据成功结束。");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            System.out.println("获取一起发商城数据发生异常：" + e.getMessage());
        }
    }

    private List<Merchant> getMerchantList() throws UnsupportedEncodingException, OpenYiqifaException {
        List<Merchant> listMerchant = open.getMerchants("", 1, 500);
        return listMerchant;
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

    /**
     * 这个是一起发商品的类别名称与本地类别名称的一个映射，键为一起发的商品类别名称，值为本地的商品类别名称
     * 
     * @return
     */
    private Map<String, String> getType2Type() {
        Map<String, String> type2Type = new HashMap<String, String>();
        type2Type.put("母婴玩具", "母婴用品");
        type2Type.put("综合商城", "综合商城");
        type2Type.put("服装饰品", "服装服饰");
        type2Type.put("食品", "食品饮料");
        type2Type.put("美容化妆", "美容化妆");
        type2Type.put("图书音像", "图书音像");
        type2Type.put("鲜花礼品 ", "鲜花礼品");
        type2Type.put("健康两性", "健康两性");
        type2Type.put("数码家电", "数码家电");
        type2Type.put("日用家居", "综合商城");
        type2Type.put("运动户外", "旅行酒店");
        type2Type.put("箱包眼镜", "鞋包配饰");
        return type2Type;
    }

    /**
     * 获取本地商品名称的类型map，键为本地商城的类型名称，值为本地商城的类型ID
     * 
     * @return
     */
    private Map<String, Integer> getTypeMap() {
        if (typeMap == null) {
            typeMap = new HashMap<String, Integer>();
            List<FanliType> listType = fanliTypeDAO.selectMalls();
            for (FanliType fanliType : listType) {
                typeMap.put(fanliType.getTitle(), fanliType.getId());
            }
        }

        return typeMap;
    }

    /**
     * 根据一起发的类型名称，获取本地商场类型的ID
     * 
     * @param yiqifaTypeName
     * @return
     */
    private int getTypeId(String yiqifaTypeName) {
        int typeId = 0;
        Map<String, String> type2Type = getType2Type();
        Map<String, Integer> typeMap = getTypeMap();
        String localTypeName = null;
        try {
            localTypeName = type2Type.get(yiqifaTypeName);
            if (localTypeName == null) {
                // 默认返回综合商城的ID
                typeId = 14;
            } else {
                typeId = typeMap.get(localTypeName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 默认返回综合商城的ID
            typeId = 14;
        }
        return typeId;
    }

    public void setFanliMallDAO(FanliMallDAO fanliMallDAO) {
        this.fanliMallDAO = fanliMallDAO;
    }

    public void setFanliTypeDAO(FanliTypeDAO fanliTypeDAO) {
        this.fanliTypeDAO = fanliTypeDAO;
    }

}
