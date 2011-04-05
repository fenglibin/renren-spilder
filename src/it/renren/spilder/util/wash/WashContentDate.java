package it.renren.spilder.util.wash;

import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * ��SEO�Ż�
 * ��ֱ�Ӳɼ��������ݣ������в���ո�������ص���վ��Ϣ��������������Ϊ�ⲻ�ǲɼ������¡�
 * ��ʽΪ�����µ�"</p>"�����������ص���վ������Ϣ"<div style="display:none">��������www.renren.it�������<a href="www.renren.it">www.renren.it</a><div>"��
 * @author Administrator
 */
public class WashContentDate {
	private static Log4j log4j = new Log4j(WashContentDate.class.getName());
	private static Connection initConn() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://184.82.12.132:3306/renren?characterEncoding=gbk", "fenglibin",
					"fenglibin");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}

		return con;
	}
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		DBOperator dbo = new DBOperator();
		Connection conn = initConn();
		dbo.setConn(conn);
		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select id,senddate from renrenarctiny order by senddate");
			String id = "";
			String sql="";
			Long l=0L;
			while(rs.next()){
				id = rs.getString("id");				
				Long senddate = Long.parseLong(rs.getString("senddate"));
				boolean doIt = false;
				if(senddate.equals(l)){
					senddate++;	
					doIt=true;
				}else if(senddate.compareTo(l)<0){
					senddate=l+1;
					doIt=true;
				}
				if(doIt==true){
					try{
						sql = "update renrenarctiny set senddate=? where id="+id;
						dbo.setSql(sql);
						ArrayList data = new ArrayList();
						data.add(senddate);
						dbo.setBindValues(data);
						dbo.executeTransactionSql();
						log4j.logDebug("finished "+id);
					}catch(Exception e){
						log4j.logDebug("exception:" +id);
						/*��֤�󲿷ݣ�С���ݾͲ�����*/
						log4j.logError(e);
					}
				}
				l=senddate;
				log4j.logDebug(id + "====" +senddate + "===="+l);
			}
			rs.close();
			st.close();
		}catch(Exception e){
			log4j.logError(e);
		}finally{
			dbo.closeConn();
			dbo = null;
		}
	}
}
