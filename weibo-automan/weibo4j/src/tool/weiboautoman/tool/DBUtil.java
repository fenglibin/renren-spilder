package weiboautoman.tool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import weiboautoman.timer.core.Constants;
import weiboautoman.timer.util.FileUtil;

public class DBUtil {

    private static final ConfigurableApplicationContext ctx  = new FileSystemXmlApplicationContext(
                                                                                                   new String[] { Constants.SPRING_CONFIG_FILE });

    private static Connection                           conn = null;
    static {
        try {
            if (conn == null) {
                conn = ((DataSource) ctx.getBean("dataSource")).getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行单条SQL
     * 
     * @param sql
     * @throws SQLException
     */
    public static void executeSql(String sql) throws SQLException {
        Statement st = conn.createStatement();
        st.execute(sql);
    }

    /**
     * 执行存放于一个文件中的SQL，每条SQL放一行
     * 
     * @param file
     * @throws IOException
     * @throws SQLException
     */
    public static void executeSqlInFile(String file) throws IOException, SQLException {
        List<String> fileList = FileUtil.getFile2List(file);
        for (String sql : fileList) {
            executeSql(sql);
        }
    }
}
