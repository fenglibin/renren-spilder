package it.renren.spilder.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.renren.spilder.main.ChildPage;
import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.Constants;
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.type.AutoDetectTypes;
import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.google.TranslatorUtil;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.util.wash.WashUtil;

public class WriteData2DB implements Task {
	
	private static Log4j log4j = new Log4j(WriteData2DB.class.getName());
	private DBOperator dbo = new DBOperator();
	private int dealedArticleNum=0;
	public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig,ChildPageDetail detail) throws Exception {
		if(dbo.getConn()==null){
			dbo.initConn(parentPageConfig.getDatabase().getJdbcDriverClass(),parentPageConfig.getDatabase().getLinkString(),parentPageConfig.getDatabase().getUsername(),parentPageConfig.getDatabase().getPassword());
		}
		dbo.getConn().setAutoCommit(false);
		try{
		translate(parentPageConfig,detail);
		saveDownUrl(parentPageConfig,detail);
		dealedArticleNum++;
		log4j.logDebug("开始保存:" + detail.getUrl());
		String typeid = String.valueOf(AutoDetectTypes.detectType(parentPageConfig, detail));
		String sql = "";
		String tempTypeId = "9999";/* 临时ID，主要用于获取当前插入的自增ID */
		sql = "INSERT INTO renrenarctiny(typeid,channel,senddate,sortrank,mid) VALUES("
				+ tempTypeId
				+ ",'1',unix_timestamp(now()),unix_timestamp(now()),'1')";
		dbo.setSql(sql);
		dbo.executeSql();

		sql = "select id from renrenarctiny where typeid=" + tempTypeId;
		dbo.setSql(sql);
		ResultSet rs = dbo.executeQuery();
		String id = rs.getString("id");

		sql = "update renrenarctiny set typeid=" + typeid + " where id=" + id;
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
		sql = "INSERT INTO renrenarchives(id,typeid,ismake,channel,click,title,writer,source,pubdate,senddate,sortrank,mid,keywords,description,weight,dutyadmin,flag,litpic,filename) VALUES(?,"
				+ typeid
				+ ",'0','1','"
				+ String.valueOf((int) (1000 * Math.random()))
				+ "',?,'"+detail.getAuthor()+"','"+detail.getSource()+"',unix_timestamp(now()),unix_timestamp(now()),unix_timestamp(now()),'1',?,?,?,1,?,?,?)";
		dbo.setSql(sql);
		ArrayList data = new ArrayList();
		data.add(Integer.parseInt(id));
		data.add(detail.getTitle().length() > 100 ? detail.getTitle()
				.substring(0, 99) : detail.getTitle());
		data.add(detail.getKeywords().length() > 30 ? detail.getKeywords()
				.substring(0, 29) : detail.getKeywords());
		data.add(detail.getDescription().length() > 255 ? detail
				.getDescription().substring(0, 254) : detail.getDescription());
		data.add(Integer.parseInt(id));
		data.add(flag);
		data.add(litpic);
		data.add(detail.getFileName());
		dbo.setBindValues(data);
		dbo.executeTransactionSql();

		String content = detail.getContent();
		if(!childPageConfig.getContent().getWashContent().equals("")){
			content = WashUtil.washData(content,childPageConfig.getContent().getWashContent());
		}
		sql = "INSERT INTO renrenaddonarticle (aid,typeid,body) VALUES(?," + typeid + ",?)";
		dbo.setSql(sql);
		data = new ArrayList();
		data.add(Integer.parseInt(id));
		data.add(content);
		dbo.setBindValues(data);
		dbo.executeTransactionSql();
		data = null;
		content = null;
		log4j.logDebug("保存完成");
		dbo.getConn().commit();
		}catch(Exception e){
			dbo.getConn().rollback();
			throw new Exception(e.getMessage(),e);
		}
	}

	public void closeTask() {
		// TODO Auto-generated method stub
		dbo.closeConn();
		dbo = null;
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
	private void translate(ParentPage parentPageConfig, ChildPageDetail detail) throws Exception{
		String from = parentPageConfig.getTranslater().getFrom();
		String to = parentPageConfig.getTranslater().getTo();
		if(!StringUtil.isNull(from) && !StringUtil.isNull(to)){
			log4j.logDebug("Translate begin,from "+from+" to "+to+"."+System.currentTimeMillis());
			detail.setAuthor(TranslatorUtil.translateHTML(detail.getAuthor(), from, to));
			detail.setContent(TranslatorUtil.translateHTML(detail.getContent(), from, to));
			detail.setDescription(TranslatorUtil.translateHTML(detail.getDescription(), from, to));
			detail.setKeywords(TranslatorUtil.translateHTML(detail.getKeywords(), from, to));
			detail.setSource(TranslatorUtil.translateHTML(detail.getSource(), from, to));
			detail.setTitle(TranslatorUtil.translateHTML(detail.getTitle(), from, to));
			log4j.logDebug("Translate end."+System.currentTimeMillis());
		}
	}
}
