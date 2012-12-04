package net.fanli7.core;

import it.renren.timer.util.DateUtil;
import it.renren.timer.util.FileUtil;
import it.renren.timer.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.request.TaobaokeItemsCouponGetRequest;
import com.taobao.api.response.TaobaokeItemsCouponGetResponse;

/**
 * 查找淘宝折扣 类SearchDiscount.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2012-11-3 下午11:11:06
 */
public class SearchDiscount {

    private static Logger log                      = LoggerFactory.getLogger(SearchDiscount.class);
    private String        MODEL                    = "";
    private List<String>  KEY_WORDS                = new ArrayList<String>();
    private String        TAOBAO_DISCOUNT_MODEL    = "taobao_discount_model.htm";
    private String        TAOBAO_DISCOUNT_KEYWORDS = "taobao_discount_keywords.txt";
    private String        outpath                  = "/home/fenglibin/www/www.fanli7.net/jingxuan";
    private long          smallCommissionRate      = 2000L;
    private long          startCommissionRate      = 3000L;
    private long          endCommissionRate        = 6000L;

    /**
     * @param args
     * @throws ApiException
     * @throws IOException
     */
    public void start() {
        try {
            readModel(TAOBAO_DISCOUNT_MODEL);
            readKeywords(TAOBAO_DISCOUNT_KEYWORDS);
            String date = DateUtil.getNow("yyyy-MM-dd");
            String goods_model = StringUtil.subString(MODEL, "#GOODS_MODEL_START#", "#GOODS_MODEL_END#");
            for (String keyword : KEY_WORDS) {
                StringBuilder goodsAll = new StringBuilder("");
                String model = MODEL;
                String pingyin = keyword.split("-")[0];
                String zhongwen = keyword.split("-")[1];
                model = model.replace("${GOODS_DATE}", date);
                model = model.replace("${GOODS_TYPE_NAME}", zhongwen);
                List<TaobaokeItem> items = getTaobaokeItem(zhongwen, startCommissionRate, endCommissionRate);
                if (items == null) {
                    items = getTaobaokeItem(zhongwen, smallCommissionRate, endCommissionRate);
                }
                if (items != null) {
                    if (items != null) {
                        for (TaobaokeItem item : items) {
                            String goods = goods_model;
                            goods = goods.replace("${GOODS_IID}", "" + item.getNumIid());
                            goods = goods.replace("${IMAGE_URL}", item.getPicUrl() + "_100x100.jpg");
                            goods = goods.replace("${GOODS_TITLE}", item.getTitle());
                            goods = goods.replace("${SHOP_KEEPER}", item.getNick());
                            goods = goods.replace("${SHOP_KEEPER_ENCODE}", "" + URLEncoder.encode(item.getNick()));
                            goods = goods.replace("${MONEY}", item.getPrice());
                            if (item.getCouponPrice() != null) {
                                goods = goods.replace("${COUPON_PRICE}", item.getCouponPrice());
                                goods = goods.replace("${COUPON_START_TIME}", item.getCouponStartTime());
                                goods = goods.replace("${COUPON_END_TIME}", item.getCouponEndTime());
                            } else {
                                goods = goods.replace("${COUPON_PRICE}", item.getPrice());
                                goods = goods.replace("${COUPON_START_TIME}", "");
                                goods = goods.replace("${COUPON_END_TIME}", "");
                            }

                            goods = goods.replace("${COMMISSION_RATE}",
                                                  String.valueOf(Double.parseDouble(item.getCommissionRate()) / 100));
                            goods = goods.replace("${COMMISSION_MONEY}",
                                                  getCommission(item.getPrice(), item.getCouponPrice(),
                                                                item.getCommissionRate()));
                            goodsAll.append(goods).append("\n");
                        }
                    }
                }
                model = model.replace("${ALL_GOODS}", goodsAll.toString());
                if (!new File(outpath).exists()) {
                    new File(outpath).mkdirs();
                }
                FileUtil.writeFile(outpath + "/" + pingyin + ".html", model);
            }
        } catch (Exception e) {
            log.error("Error happend for generating taobao discount data.", e);
        }
    }

    /**
     * @param price 原价
     * @param couponPrice 折后价
     * @param commissionRate 返现比例
     * @return
     */
    private String getCommission(String price, String couponPrice, String commissionRate) {
        DecimalFormat df = new DecimalFormat("###.00");
        if (!StringUtil.isNull(couponPrice)) {
            return String.valueOf(df.format(Double.parseDouble(couponPrice) * Double.parseDouble(commissionRate)
                                            * Constants.TAOBAO_FANXIANBI / 10000));
        } else {
            return String.valueOf(df.format(Double.parseDouble(price) * Double.parseDouble(commissionRate)
                                            * Constants.TAOBAO_FANXIANBI / 10000));
        }
    }

    private void readModel(String modelFilePath) throws IOException {
        MODEL = FileUtil.read(modelFilePath, "UTF-8");
    }

    private void readKeywords(String keywordsFilePath) throws IOException {
        String keywords = FileUtil.read(keywordsFilePath, "UTF-8");
        String[] keyArr = keywords.split(",");
        for (String key : keyArr) {
            KEY_WORDS.add(key);
        }
    }

    private List<TaobaokeItem> getTaobaokeItem(String keyword, long startCommissionRate, long endCommissionRate) {
        // 调用淘宝客API：taobao.taobaoke.items.coupon.get 查询淘客折扣商品
        List<TaobaokeItem> items = null;
        try {
            TaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL, Constants.TAOBAO_APP_KEY, Constants.TAOBAO_SECRET);
            TaobaokeItemsCouponGetRequest req = new TaobaokeItemsCouponGetRequest();
            req.setKeyword(keyword);
            req.setFields(Constants.TAOBAO_FIELDS);
            req.setPageNo(1L);
            req.setPageSize(100L);
            req.setStartCredit("1diamond");
            req.setEndCredit("5goldencrown");
            req.setStartCommissionRate(startCommissionRate);
            req.setEndCommissionRate(endCommissionRate);
            TaobaokeItemsCouponGetResponse response = client.execute(req);
            items = response.getTaobaokeItems();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void setOutpath(String outpath) {
        this.outpath = outpath;
    }

    public void setTAOBAO_DISCOUNT_KEYWORDS(String tAOBAO_DISCOUNT_KEYWORDS) {
        if (!StringUtil.isNull(tAOBAO_DISCOUNT_KEYWORDS)) {
            TAOBAO_DISCOUNT_KEYWORDS = tAOBAO_DISCOUNT_KEYWORDS;
        }
    }

    public void setStartCommissionRate(long startCommissionRate) {
        if (startCommissionRate > 0) {
            this.startCommissionRate = startCommissionRate;
        }
    }

    public void setEndCommissionRate(long endCommissionRate) {
        if (startCommissionRate > 0) {
            this.endCommissionRate = endCommissionRate;
        }
    }

    public static void initArgs(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                String[] keyValue = arg.split("=");
                String value = "";
                if (keyValue.length == 1) {
                    value = "";
                } else {
                    value = keyValue[1];
                }
                System.setProperty(keyValue[0], value);
            }
        }
    }

    public static void main(String[] args) {
        SearchDiscount test = new SearchDiscount();
        initArgs(args);
        if (!StringUtil.isNull(System.getProperty("outpath"))) {
            test.outpath = System.getProperty("outpath");
        }
        if (!StringUtil.isNull(System.getProperty("keywords"))) {
            test.TAOBAO_DISCOUNT_KEYWORDS = System.getProperty("keywords");
        }
        if (!StringUtil.isNull(System.getProperty("startCommissionRate"))) {
            test.startCommissionRate = Long.parseLong(System.getProperty("startCommissionRate"));
        }
        if (!StringUtil.isNull(System.getProperty("endCommissionRate"))) {
            test.endCommissionRate = Long.parseLong(System.getProperty("endCommissionRate"));
        }
        // test.outpath = "output";
        // test.TAOBAO_DISCOUNT_KEYWORDS = "taobao_discount_keywords2.txt";
        // test.startCommissionRate = 5000L;
        // test.endCommissionRate = 7000L;
        test.start();
    }

}
