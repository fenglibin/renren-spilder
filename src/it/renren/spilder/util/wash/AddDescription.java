package it.renren.spilder.util.wash;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.StringUtil;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AddDescription {

    private static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                  new String[] { Constants.SPRING_CONFIG_FILE });

    private void changeSimple() throws SQLException {
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select a.id,b.body from renrenarchives a,renrenaddonarticle b where a.id=b.aid and a.desciption=''");
        String description = "";
        while (rs.next()) {
            int id = rs.getInt("id");
            description = rs.getString("desciption");
            description = StringUtil.removeHtmlTags(description).trim().substring(0, Constants.CONTENT_LEAST_LENGTH);
            st.execute("update renrenarchives set description='" + description + "' where id=" + id);
        }
        rs.close();
        st.close();
        conn.close();
    }

    private void changeFan() throws SQLException {
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select a.id,b.body from renrenfanti_archives a,renrenfanti_addonarticle b where a.id=b.aid and a.desciption=''");
        String description = "";
        while (rs.next()) {
            int id = rs.getInt("id");
            description = rs.getString("desciption");
            description = StringUtil.removeHtmlTags(description).trim().substring(0, Constants.CONTENT_LEAST_LENGTH);
            st.execute("update renrenfanti_archives set description='" + description + "' where id=" + id);
        }
        rs.close();
        st.close();
        conn.close();
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        AddDescription add = new AddDescription();
        add.changeSimple();
        add.changeFan();
    }
}
