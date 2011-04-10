package it.renren.spilder.util.wash;

import it.renren.spilder.dao.ArctypeDAO;
import it.renren.spilder.main.Constants;
import it.renren.spilder.type.DedecmsTypesMap;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 对文章类别进行处理
 * 
 * @author Administrator
 */
public class RedoContentType {

    private static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                  new String[] { Constants.SPRING_CONFIG_FILE });

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        DedecmsTypesMap typesMap = new DedecmsTypesMap();
        typesMap.setArctypeDAO((ArctypeDAO) ctx.getBean("arctypeDAO"));
        Map<Integer, String> typesMapData = typesMap.getTypesMap();

        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        // 65514
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select a.id,a.title,b.body from renrenarchives a,renrenaddonarticle b where a.id>65514 and a.id=b.aid");
        while (rs.next()) {
            int id = rs.getInt("id");
            int type = detectType(typesMapData, rs.getString("title"), rs.getString("body"));
            if (type > -1) {
                Statement up = conn.createStatement();
                up.executeUpdate("update renrenarctiny set typeid=" + type + " where id=" + id);
                up.executeUpdate("update renrenarchives set typeid=" + type + " where id=" + id);
                up.executeUpdate("update renrenaddonarticle set typeid=" + type + " where aid=" + id);
                System.out.println("simple is ok.");
            } else {
                System.out.println("type is -1,keep the typeid.");
            }
        }
        conn.close();
        DataSource dataSource_fanti = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn_fanti = dataSource_fanti.getConnection();
        Statement st_fanti = conn_fanti.createStatement();
        ResultSet rs_fanti = st_fanti.executeQuery("select a.id,a.title,b.body from renrenfanti_archives a,renrenfanti_addonarticle b where a.id>80894 and a.id=b.aid");
        while (rs_fanti.next()) {
            int id = rs_fanti.getInt("id");
            int type = detectTypeFanti(typesMapData, rs_fanti.getString("title"), rs_fanti.getString("body"));
            if (type > -1) {
                Statement up = conn_fanti.createStatement();
                up.executeUpdate("update renrenfanti_arctiny set typeid=" + type + " where id=" + id);
                up.executeUpdate("update renrenfanti_archives set typeid=" + type + " where id=" + id);
                up.executeUpdate("update renrenfanti_addonarticle set typeid=" + type + " where aid=" + id);
                System.out.println("fanti is ok.");
            } else {
                System.out.println("type is -1,keep the typeid.");
            }
        }
        conn_fanti.close();
    }

    private static int detectType(Map<Integer, String> typesMapData, String title, String content) {
        int type = -1;
        try {
            title = title.toLowerCase();
            content = content.toLowerCase();
            title = StringUtil.removeHtmlTags(title);
            content = StringUtil.removeHtmlTags(content);

            int currentType = -1;
            Iterator<Integer> typesMapDataKeys = typesMapData.keySet().iterator();
            while (typesMapDataKeys.hasNext()) {/* 先检测标题中是否包括有分类关键字 */
                currentType = typesMapDataKeys.next();
                if (title.indexOf(typesMapData.get(currentType).toLowerCase()) > 0) {
                    type = currentType;
                    break;
                }
            }
            if (type == -1) {/* 再检测内容中是否包括有分类关键字 */
                typesMapDataKeys = typesMapData.keySet().iterator();
                while (typesMapDataKeys.hasNext()) {
                    currentType = typesMapDataKeys.next();
                    if (content.indexOf(typesMapData.get(currentType).toLowerCase()) > 0) {
                        type = currentType;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    private static int detectTypeFanti(Map<Integer, String> typesMapData, String title, String content) {
        int type = -1;
        try {
            title = title.toLowerCase();
            content = content.toLowerCase();
            title = StringUtil.removeHtmlTags(title);
            content = StringUtil.removeHtmlTags(content);

            int currentType = -1;
            Iterator<Integer> typesMapDataKeys = typesMapData.keySet().iterator();
            while (typesMapDataKeys.hasNext()) {/* 先检测标题中是否包括有分类关键字 */
                currentType = typesMapDataKeys.next();
                if (title.indexOf(FontUtil.jian2fan(typesMapData.get(currentType).toLowerCase())) > 0) {
                    type = currentType;
                    break;
                }
            }
            if (type == -1) {/* 再检测内容中是否包括有分类关键字 */
                typesMapDataKeys = typesMapData.keySet().iterator();
                while (typesMapDataKeys.hasNext()) {
                    currentType = typesMapDataKeys.next();
                    if (content.indexOf(FontUtil.jian2fan(typesMapData.get(currentType).toLowerCase())) > 0) {
                        type = currentType;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

}
