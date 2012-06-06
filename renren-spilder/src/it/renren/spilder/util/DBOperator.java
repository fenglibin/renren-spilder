package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBOperator {

    private static Log4j log4j = new Log4j(DBOperator.class.getName());
    private Connection   conn  = null;

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * ps绑定的参数列表
     */
    protected ArrayList         bindValues = new ArrayList();

    protected PreparedStatement ps         = null;

    String                      sql        = null;

    public Connection initConn(String driverClass, String linkString, String username, String password) {
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(linkString, username, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }
        return conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void executeSql() throws SQLException {
        Statement st = conn.createStatement();
        st.execute(sql);
        st.close();
    }

    public void closeConn() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }
    }

    /**
     * 设置ps绑定值
     * 
     * @param bindVals ArrayList {参数1,参数2,...}
     * @throws SQLException
     */
    public void setBindValues(ArrayList bindVals) throws SQLException {
        if (null == ps) {
            ps = conn.prepareStatement(sql);
        }
        bindValues = new ArrayList(bindVals);
    }

    /**
     * 设置批量绑定值
     * 
     * @param bindVals ArrayList {参数1,参数2,...}
     * @throws SQLException
     */
    public void addBatch(ArrayList bindVals) throws SQLException {
        if (null == ps) {
            ps = conn.prepareStatement(sql);
        }
        bindValues(bindVals);
        ps.addBatch();
    }

    /**
     * 绑定ps参数
     * 
     * @param bindVals ArrayList {参数1,参数2,...}
     * @throws SQLException
     */
    protected void bindValues(ArrayList bindVals) throws SQLException {
        if (null != bindVals) {
            for (int i = 0; i < bindVals.size(); i++) {
                Object val = bindVals.get(i);
                // val为null则默认为VARCHAR类型
                int sqlType = java.sql.Types.VARCHAR;

                if (val instanceof SqlTypes.NullType) {
                    sqlType = SqlTypes.getSQLType(((SqlTypes.NullType) val).getClassType());
                    SqlTypes.setObject(ps, i + 1, null, sqlType);
                } else {
                    if (null != val) sqlType = SqlTypes.getSQLType(val.getClass());
                    SqlTypes.setObject(ps, i + 1, val, sqlType);
                }
            }
        }
        this.setBindValues(bindVals);
    }

    /**
     * 执行批量操作
     * 
     * @return int[]
     * @throws SQLException
     */
    public int[] executeBatch() throws SQLException {
        try {
            return ps.executeBatch();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                    ps = null;
                } catch (Exception e) {

                }
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    // 执行一个涉及事务的SQL语句
    public int executeTransactionSql() throws SQLException {
        ps = conn.prepareStatement(sql);
        try {
            bindValues(bindValues);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("executeTransactionSql Exception SQL:" + sql);
            throw e;
        }

    }

    public ResultSet executeQuery() throws SQLException {
        ResultSet rs = null;
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (!rs.next()) {
            rs = null;
        }
        return rs;

    }
}
