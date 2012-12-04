package net.fanli7.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.fanli7.core.db.DBConn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TaobaokeItemsDetailGetRequest;
import com.taobao.api.response.TaobaokeItemsDetailGetResponse;

public class GetSmallImgUrlByIID {

    private static Logger log = LoggerFactory.getLogger(GetSmallImgUrlByIID.class);

    /**
     * 获取用户购物的商品，但还没有获取图片URL商品，将图片的URL获取下来
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private List<String> getIISList() throws ClassNotFoundException, SQLException {
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = DBConn.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select num_iid from fanli_tradelist where pic_url is null");
            int count = 0;
            StringBuilder iids = new StringBuilder("");
            while (rs.next()) {
                count++;
                iids.append(rs.getString("num_iid")).append(",");
                if (count == 10) {
                    list.add(iids.deleteCharAt(iids.length()).toString());
                    iids.delete(0, iids.length());
                    count = 0;
                }
            }
            if (iids.length() > 0) {
                list.add(iids.deleteCharAt(iids.length()).toString());
                iids.delete(0, iids.length());
                count = 0;
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    private void updateImageUrl() throws ClassNotFoundException, SQLException, ApiException {
        List<String> list = getIISList();
        if (list.size() > 0) {
            for (String iids : list) {
                try {
                    TaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL, Constants.TAOBAO_APP_KEY, Constants.TAOBAO_SECRET);
                    TaobaokeItemsDetailGetRequest req = new TaobaokeItemsDetailGetRequest();
                    req.setFields("pic_url,num_iid");
                    req.setNumIids(iids);
                    TaobaokeItemsDetailGetResponse response = client.execute(req);
                } catch (ApiException e) {
                    log.error("获取商品小图片发生异常:" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
