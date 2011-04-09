package it.renren.spilder.task;

import java.sql.SQLException;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.main.ChildPage;
import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.Constants;
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.type.AutoDetectTypes;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.log.Log4j;

public class WriteData2FanDB extends Task {

    private static Log4j        log4j            = new Log4j(WriteData2FanDB.class.getName());
    private static int          dealedArticleNum = 0;
    ArctinyDAO                  arctinyDAOFanti;
    ArchivesDAO                 archivesDAOFanti;
    AddonarticleDAO             addonarticleDAOFanti;
    DownurlDAO					downurlDAOFanti; 
    AutoDetectTypes 	 		autoDetectTypes;  
    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            saveDownUrl(parentPageConfig, detail);
            dealedArticleNum++;
            log4j.logDebug("开始保存:" + detail.getUrl());
            int typeid = autoDetectTypes.detectType(parentPageConfig, detail);
            ArctinyDO arctinyDO = new ArctinyDO();

            int tempTypeId = (int)(Math.random()*1000)+9999;/* 临时ID，主要用于获取当前插入的自增ID */
            arctinyDO.setTypeid(tempTypeId);
            arctinyDAOFanti.insertArctiny(arctinyDO);
            //将插入的自增ID给查询出来
            arctinyDO = arctinyDAOFanti.selectArctinyByTypeId(arctinyDO);
            arctinyDO.setTypeid(typeid);
            arctinyDAOFanti.updateArctinyTypeidById(arctinyDO);


            String flag = getFlag(parentPageConfig, detail);
            
            String litpic = detail.getLitpicAddress();// 缩略图地址
            if (!litpic.equals("")) {
                litpic = Constants.RenRen_URL + litpic;
            }
            
            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(arctinyDO.getId());
            archivesDO.setTypeid(typeid);
            archivesDO.setTitle(jian2fan(detail.getTitle().length() > 100 ? detail.getTitle().substring(0, 99) : detail.getTitle()));
            archivesDO.setKeywords(jian2fan(detail.getKeywords().length() > 30 ? detail.getKeywords().substring(0, 29) : detail.getKeywords()));
            archivesDO.setDescription(jian2fan(detail.getDescription().length() > 255 ? detail.getDescription().substring(0, 254) : detail.getDescription()));
            archivesDO.setClick((int) (1000 * Math.random()));
            archivesDO.setWriter(jian2fan(detail.getAuthor()));
            archivesDO.setSource(jian2fan(detail.getSource()));
            archivesDO.setWeight(arctinyDO.getId());
            archivesDO.setDutyadmin(1);
            archivesDO.setFlag(flag);
            archivesDO.setLitpic(litpic);
            archivesDO.setFilename(detail.getFileName());            
            archivesDAOFanti.insertArchives(archivesDO);            

            String content = detail.getContent();
            content = content.replace("www.renren.it", "fan.renren.it");
            if (detail.isPicArticle()) {
                content = content.replace(parentPageConfig.getImageDescUrl(),
                                          Constants.RenRen_URL + parentPageConfig.getImageDescUrl());
            }
            AddonarticleDO addonarticleDO = new AddonarticleDO();
            addonarticleDO.setAid(arctinyDO.getId());
            addonarticleDO.setTypeid(typeid);
            addonarticleDO.setBody(jian2fan(content));
            addonarticleDAOFanti.insertAddonarticle(addonarticleDO);
            log4j.logDebug("Save oK FANTI");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    private String jian2fan(String string) throws Exception {
        try {
            string = FontUtil.jian2fan(new StringBuffer(string));
        } catch (Exception e) {
            string = FontUtil.jian2fan(new StringBuffer(string));
            log4j.logDebug("翻译的时候发生异常，通过直接替换文字的方式进行翻译.");
            log4j.logError(e);
        }
        return string;
    }
    /* 保存已经获取内容的URL，如果保存出现主键重复的异常，说明该URL已经获取过内容，为正常现象 */
    protected void saveDownUrl(ParentPage parentPageConfig, ChildPageDetail detail) throws SQLException {
        if (parentPageConfig.isFilterDownloadUrl()) {
        	DownurlDO downurlDO = new DownurlDO();
        	downurlDO.setUrl(detail.getUrl());
        	downurlDAOFanti.insertDownurl(downurlDO);
        }
    }
    @Override
	protected int getDealedArticleNum() {
		return dealedArticleNum;
	}
    public void setArctinyDAOFanti(ArctinyDAO arctinyDAOFanti) {
        this.arctinyDAOFanti = arctinyDAOFanti;
    }

    public void setArchivesDAOFanti(ArchivesDAO archivesDAOFanti) {
        this.archivesDAOFanti = archivesDAOFanti;
    }

    public void setAddonarticleDAOFanti(AddonarticleDAO addonarticleDAOFanti) {
        this.addonarticleDAOFanti = addonarticleDAOFanti;
    }
    
    public void setDownurlDAOFanti(DownurlDAO downurlDAOFanti) {
		this.downurlDAOFanti = downurlDAOFanti;
	}
    public void setAutoDetectTypes(AutoDetectTypes autoDetectTypes) {
		this.autoDetectTypes = autoDetectTypes;
	}
}
