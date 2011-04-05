package it.renren.spilder.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import it.renren.spilder.main.ChildPage;
import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Main;
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.type.AutoDetectTypes;
import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.google.TranslatorUtil;
import it.renren.spilder.util.log.Log4j;

public class WriteData2FanDB implements Task {
	private static Log4j log4j = new Log4j(WriteData2FanDB.class.getName());
	private DBOperator dbo = new DBOperator();
	private int dealedArticleNum=0;

	public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig,ChildPageDetail detail) throws Exception {		
		initDbConn();
		dbo.getConn().setAutoCommit(false);
		try{
			detail.setDescription("");
			saveDownUrl(parentPageConfig,detail);
			dealedArticleNum++;
			log4j.logDebug("开始保存FANTI:" + detail.getUrl());
			String typeid = String.valueOf(AutoDetectTypes.detectType(parentPageConfig, detail));
			String sql = "";
			String tempTypeId = "9999";/* 临时ID，主要用于获取当前插入的自增ID */
			sql = "INSERT INTO renrenfanti_arctiny(typeid,channel,senddate,sortrank,mid) VALUES("
					+ tempTypeId
					+ ",'1',unix_timestamp(now()),unix_timestamp(now()),'1')";
			dbo.setSql(sql);
			dbo.executeSql();
	
			sql = "select id from renrenfanti_arctiny where typeid=" + tempTypeId;
			dbo.setSql(sql);
			ResultSet rs = dbo.executeQuery();
			String id = rs.getString("id");
	
			sql = "update renrenfanti_arctiny set typeid=" + typeid + " where id=" + id;
			dbo.setSql(sql);
			dbo.executeSql();
	
			String flag = "";
			if (detail.isPicArticle()) {
				flag = Constants.ARTICLE_TU;
			}
			if (parentPageConfig.getRandRecommandFrequency() != 0) {
				if (dealedArticleNum % parentPageConfig.getRandRecommandFrequency() == 0) {
					if (flag.equals(Constants.ARTICLE_TU)) {
						flag = flag + "," + Constants.ARTICLE_TUIJIAN;
					}else{
						flag = Constants.ARTICLE_TUIJIAN;
					}
				}
			}
			if(parentPageConfig.isSRcommand()){
				flag = flag + "," + Constants.ARTICLE_TEJIAN;
			}
			String litpic = detail.getLitpicAddress();// 缩略图地址
			if(!litpic.equals("")){
				litpic = Constants.RenRen_URL+litpic;
			}		
			ArrayList data = new ArrayList();
			String source="",writer;
			source = detail.getSource();
			writer = detail.getAuthor();
		
			source=jian2fan(source);
			writer=jian2fan(writer);
			sql = "INSERT INTO renrenfanti_archives(id,typeid,ismake,channel,click,title,writer,source,pubdate,senddate,sortrank,mid,keywords,description,weight,dutyadmin,flag,litpic,filename) VALUES(?,"
					+ typeid
					+ ",'0','1','"
					+ String.valueOf((int) (1000 * Math.random()))
					+ "',?,'"+writer+"','"+source+"',unix_timestamp(now()),unix_timestamp(now()),unix_timestamp(now()),'1',?,?,?,1,?,?,?)";
			dbo.setSql(sql);
			data = new ArrayList();
			data.add(Integer.parseInt(id));
			data.add(jian2fan(detail.getTitle().length() > 100 ? detail.getTitle().substring(0, 99) : detail.getTitle()));
			data.add(jian2fan(detail.getKeywords().length() > 30 ? detail.getKeywords().substring(0, 29) : detail.getKeywords()));
			data.add(jian2fan(detail.getDescription().length() > 255 ? detail.getDescription().substring(0, 254) : detail.getDescription()));
			data.add(Integer.parseInt(id));
			data.add(flag);
			data.add(litpic);
			data.add(detail.getFileName());
			dbo.setBindValues(data);
			dbo.executeTransactionSql();			
	
	
			sql = "INSERT INTO renrenfanti_addonarticle (aid,typeid,body) VALUES(?,"
					+ typeid + ",?)";
			dbo.setSql(sql);
			String content = detail.getContent();
			content = content.replace("www.renren.it", "fan.renren.it");

			if(detail.isPicArticle()){
				content = content.replace(parentPageConfig.getImageDescUrl(), Constants.RenRen_URL+parentPageConfig.getImageDescUrl());
			}
			data = new ArrayList();
			data.add(Integer.parseInt(id));
			data.add(content);
			dbo.setBindValues(data);
			dbo.executeTransactionSql();
			
			sql = "update renrenfanti_addonarticle set body=? where aid=?";
			dbo.setSql(sql);
			data = new ArrayList();
			data.add(jian2fan(content));
			data.add(Integer.parseInt(id));			
			dbo.setBindValues(data);
			dbo.executeTransactionSql();

			data = null;
			log4j.logDebug("Save oK FANTI");
			dbo.getConn().commit();
		}catch(Exception e){
			dbo.getConn().rollback();
			throw new Exception(e.getMessage(),e);
		}finally{
			/*重置分类ID*/
			AutoDetectTypes.resetCurrentTypeId();
		}
	}


	public void closeTask() {
		// TODO Auto-generated method stub
		dbo.closeConn();
		dbo = null;
	}
	private void initDbConn(){
		if(dbo.getConn()==null){
			Document taskDoc = Main.getTaskDoc();
			String jdbcDriverClass,linkString,username,password;
			String classname=this.getClass().getName();
			jdbcDriverClass = JDomUtil.getValueByXpath(taskDoc,"/Tasks/Task[@java='"+classname+"']/Database/JdbcDriverClass/Value");
			linkString = JDomUtil.getValueByXpath(taskDoc,"/Tasks/Task[@java='"+classname+"']/Database/LinkString/Value");
			username = JDomUtil.getValueByXpath(taskDoc,"/Tasks/Task[@java='"+classname+"']/Database/Username/Value");
			password = JDomUtil.getValueByXpath(taskDoc,"/Tasks/Task[@java='"+classname+"']/Database/Password/Value");
			dbo.initConn(jdbcDriverClass, linkString, username, password);
		}
	}
	private String jian2fan(String string) throws Exception{
		try{
			Document taskDoc = Main.getTaskDoc();
			String classname=this.getClass().getName();
			Element translater = (Element)XPath.selectSingleNode(taskDoc, "/Tasks/Task[@java='"+classname+"']/Translater");
			if(translater!=null && !StringUtil.isNull(string)){
				String from = translater.getChild("From").getChildText("Value").trim();
				String to = translater.getChild("To").getChildText("Value").trim();
				string = TranslatorUtil.translateHTML(string,from,to);
			}else if(!StringUtil.isNull(string)){
				string = FontUtil.jian2fan(new StringBuffer(string));
			}
		}catch(Exception e){
			string = FontUtil.jian2fan(new StringBuffer(string));
			log4j.logDebug("翻译的时候发生异常，通过直接替换文字的方式进行翻译.");
			log4j.logError(e);
		}
		return string;
	}
	/* 保存已经获取内容的URL，如果保存出现主键重复的异常，说明该URL已经获取过内容，为正常现象 */
	private void saveDownUrl(ParentPage parentPageConfig,ChildPageDetail detail) throws SQLException {
		if(parentPageConfig.isFilterDownloadUrl()){ 
			String sql = "";
			sql = "INSERT INTO downurl(url) values(?)";
			dbo.setSql(sql);
			ArrayList data = new ArrayList();
			data.add(detail.getUrl());
			dbo.setBindValues(data);
			dbo.executeTransactionSql();
			data = null;
		}
	}
}
