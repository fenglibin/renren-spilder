package it.renren.spilder.yayamai;

import it.renren.spilder.util.DateUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * @author Fenglb E-mail:fenglb@sunline.cn
 * @version 1.0
 * ����ʱ�䣺2009-12-28 ����03:57:05
 * ��˵��:��www.go9go.cn��������ȡ����
 */
public class GetSiteInfoFromHtml {
	private static Log4j log4j = new Log4j(GetSiteInfoFromHtml.class.getName());
	//static int maxId=79500;
	//static int maxId=77849;
	//static int maxId=77724;
	static int nowMaxId=80000;
	static int lastMaxId = 79900;
	static int getRecordNum=0;
	static HashMap<String,String> linktypes = new HashMap<String,String>();
	
	static {
		Connection conn = ODBCConnection.getConnectionByODBC();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from linktypes");
			while(rs.next()){
				String id = rs.getString("id");
				String typename = rs.getString("typename");
				linktypes.put(typename, id);
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}
	}
	private static int getMaxUserId(){
		int maxId=0;
		Connection conn = ODBCConnection.getConnectionByODBC();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(id) as maxid from users");
			if(rs.next()){
				maxId = rs.getInt("maxid");
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}
		return maxId;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String headUrl="http://www.go9go.cn/detail_";
		try {
			boolean isFaBu = true;
			if(args.length>=1){
				nowMaxId = Integer.parseInt(args[0]);
				log4j.logDebug("nowMaxId="+nowMaxId);
			}
			if(args.length==2){
				lastMaxId = Integer.parseInt(args[1]);
				log4j.logDebug("lastMaxId="+lastMaxId);
			}
			getInfo(headUrl);
			if(isFaBu){
				log4j.logDebug("�����ܹ���ȡ�ļ�¼��:"+getRecordNum);
				RemoteFaBu.doFaBu(getRecordNum);
			}
//			while(true){
//				getInfo(headUrl);
//				log4j.logDebug("�����ܹ���ȡ�ļ�¼��:"+getRecordNum);
//				RemoteFaBu.run2(getRecordNum);
//				getRecordNum=0;
//				Thread.sleep(30*60*1000);//ÿ����Ϣ���Сʱ
//			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}
	}
	public static void getInfo(String headUrl) throws IOException, SQLException{
		int userid=getMaxUserId()+1;
		Connection conn = ODBCConnection.getConnectionByODBC();
		for(int i=nowMaxId;i>=lastMaxId;i--){
			String url = headUrl+i+".html";
			log4j.logDebug("��ʼ����ǰURL:"+url);
			//String content = Util.getContentByURL(url);
			String content = HttpClientUtil.getGetResponseWithHttpClient(url,"gb2312");
			if(content.indexOf("<table id=\"detail\" border=\"0\" style=\"width:700px;margin:10px 0 10px 0;\">")>0){
				try{
					content=StringUtil.subString(content, "<table id=\"detail\" border=\"0\" style=\"width:700px;margin:10px 0 10px 0;\">","<td align=left valign=top>");
					analysisThisSite(content,userid,conn);					
				}catch(Exception e){
					log4j.logDebug("��ǰURL�������쳣:"+url);
					log4j.logError(e);
				}
				userid++;
			}else{
				log4j.logDebug("��ǰURLδ��ȡ����ϸ���ݣ�"+url+",�п����ǻ�û�е�ǰIDָ����ҳ�棡");
			}
			log4j.logDebug("��ǰURL���������");
		}		
		conn.close();
	}
	public static void analysisThisSite(String content,int userid,Connection conn) throws SQLException, IOException{
		String sql_user="";
		String sql_siteinfo="";	
		String sitename="";
		String siteurl="";
		String price="";
		String shoulu_baidu="";
		String shoulu_google="";
		String alexa="";
		String index_photo_date="";
		String pagerank="";
		String addtime="";
		String sitetype="";
		String trantype="";
		String qq="";
		String linktype="";
		String linkposition="";
		String req_shoulu_baidu="";
		String req_index_photo_date="";
		String req_pr="";
		String req_outlink_num="";
		String req_aleax="";
		int current=1;
		Statement st = conn.createStatement();
		while(content.indexOf("<tr>")>0){
			String subContent = StringUtil.subString(content, "<tr>", "</tr>");
			content = content.replace("<tr>"+subContent+"</tr>", "");
			if(current==2){//����Ҫ�ڶ��е���Ϣ
				current++;
				continue;
			}
			int subCurrent = 1;
			String value="";
			while(subContent.indexOf("<td")>=0){
				int s = subContent.indexOf(">")+1;
				String startStr = subContent.substring(0,s);
				value = StringUtil.subString(subContent, startStr, "</td>");
				subContent = subContent.replace(startStr+value+"</td>", "");
				//value = value.substring(value.indexOf(">")+1);
				if(subCurrent == 2){
					
				}
				
				subCurrent++;
			}
			if(current==1){//��ַ
				value = StringUtil.subString(value, "url=", ">");
				siteurl = value;
			}
			if(current==2){
				
			}
			if(current==3){//���ƣ�����
				sitename = value;			
			}
			if(current==4){//URL����current==1��ʱ���Ѿ���ȡ����
				
			}
			if(current==5){//Baidu��¼
				shoulu_baidu = value;
			}
			if(current==6){//Baidu����
				index_photo_date = value;
			}
			if(current==7){//Google��¼
				shoulu_google = value;
			}
			if(current==8){//pr
				value = StringUtil.subString(value, "pr", ".gif");
				pagerank = value;
			}
			if(current==9){//Alexa����
				alexa = value;
			}
			if(current==10){//���׷�ʽ
				if(value.indexOf("��")>=0){
					trantype="1|";
				}
				if(value.indexOf("��")>=0){
					trantype += "2|";
				}
				if(value.indexOf("��")>=0){
					trantype += "3|";
				}
			}
			if(current==11){//��������
				linktype = value;
			}
			if(current==12){//����λ��
				linkposition = value;
			}
			if(current==13){//���ӷ���
				sitetype = getSiteType(value);
			}
			if(current==14){//���׼۸�
				if(value.indexOf("--")>=0){
					price="0";
				}
				if(value.indexOf("����")>=0){
					price="-1";
				}
				if(value.equals("")){
					price="0";
				}
				if(price.equals("")){
					price = "0";
				}
			}
			if(current==15){//Ҫ��ٶ���¼
				req_shoulu_baidu = value;
			}
			if(current==16){//Ҫ��ٶȿ���
				req_index_photo_date = value;
			}
			if(current==17){//Ҫ��PRֵ
				req_pr = value;
			}
			if(current==18){//Ҫ����ҳ��������
				req_outlink_num = value;
			}
			if(current==19){//Ҫ��Alexa����
				req_aleax = value;
			}
			if(current==20){//��ϵQQ
				//value = Util.subString(value, "?uin=", "&Site");
				value = value.substring(value.indexOf("</a>")+4);
				qq = value;
			}
			if(current==21){//����˵��
				
			}			
			current++;
		}
		sql_user="";
		sql_siteinfo="";
		addtime = DateUtil.getNow("yyyy-MM-dd hh:mm:ss");
		
		sql_siteinfo = "insert into siteinfo(sitename,siteurl,price,shoulu_baidu,shoulu_google,alexa,index_photo_date,pagerank,addtime,sitetype,trantype,linktype,linkposition,req_shoulu_baidu,req_index_photo_date,req_pr,req_outlink_num,req_aleax,userid) values(";
		sql_siteinfo += "'"+sitename+"',";
		sql_siteinfo += "'"+siteurl+"',";
		sql_siteinfo += ""+price+",";
		sql_siteinfo += ""+shoulu_baidu+",";
		sql_siteinfo += ""+shoulu_google+",";
		sql_siteinfo += ""+alexa+",";
		sql_siteinfo += "'"+index_photo_date+"',";
		sql_siteinfo += "'"+pagerank+"',";
		sql_siteinfo += "'"+addtime+"',";
		sql_siteinfo += ""+sitetype+",";
		sql_siteinfo += "'"+trantype+"',";		
		sql_siteinfo += "'"+linktype+"',";
		sql_siteinfo += "'"+linkposition+"',";
		sql_siteinfo += "'"+req_shoulu_baidu+"',";
		sql_siteinfo += "'"+req_index_photo_date+"',";
		sql_siteinfo += "'"+req_pr+"',";
		sql_siteinfo += "'"+req_outlink_num+"',";
		sql_siteinfo += "'"+req_aleax+"',";
		sql_siteinfo += ""+userid+"";		
		sql_siteinfo += ")";		
		
		sql_user="insert into users(id,email,password,logintimes,qq,regdate) values(";
		sql_user += ""+userid+",";
		sql_user += "'"+qq+"@qq.com',";
		sql_user += "'888888',1,";
		sql_user += "'"+qq+"',";
		sql_user += "'"+index_photo_date+"'";
		sql_user += ")";
		
		ResultSet rs = st.executeQuery("select * from siteinfo where siteurl='"+siteurl+"'");
		try{
			if(!rs.next()){
				getRecordNum++;
				log4j.logDebug(sql_siteinfo);
				log4j.logDebug(sql_user);
				st.execute(sql_user);
				st.execute(sql_siteinfo);
			}
		}catch(Exception e){
			log4j.logError(e);
		}finally{
			st.close();
		}
		
	}
	private static String getSiteType(String sitetype){
		return linktypes.get(sitetype.trim());
	}
}

