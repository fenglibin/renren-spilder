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
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.type.AutoDetectTypes;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.google.TranslatorUtil;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.util.wash.WashUtil;

public class WriteData2DB extends Task {

    private static Log4j log4j            = new Log4j(WriteData2DB.class.getName());
    private static int   dealedArticleNum = 0;
    ArctinyDAO           arctinyDAO;
    ArchivesDAO          archivesDAO;
    AddonarticleDAO      addonarticleDAO;
    DownurlDAO           downurlDAO;
    AutoDetectTypes      autoDetectTypes;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            translate(parentPageConfig, detail);
            saveDownUrl(parentPageConfig, detail);
            dealedArticleNum++;
            log4j.logDebug("开始保存:" + detail.getUrl());
            int typeid = autoDetectTypes.detectType(parentPageConfig, detail);
            ArctinyDO arctinyDO = new ArctinyDO();

            int tempTypeId = (int) (Math.random() * 1000) + 9999;/* 临时ID，主要用于获取当前插入的自增ID */
            arctinyDO.setTypeid(tempTypeId);
            arctinyDAO.insertArctiny(arctinyDO);
            // 将插入的自增ID给查询出来
            arctinyDO = arctinyDAO.selectArctinyByTypeId(arctinyDO);
            arctinyDO.setTypeid(typeid);
            arctinyDAO.updateArctinyTypeidById(arctinyDO);

            String flag = getFlag(parentPageConfig, detail);

            String litpic = detail.getLitpicAddress();// 缩略图地址

            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(arctinyDO.getId());
            archivesDO.setTypeid(typeid);
            archivesDO.setTitle(detail.getTitle().length() > 100 ? detail.getTitle().substring(0, 99) : detail.getTitle());
            archivesDO.setKeywords(detail.getKeywords().length() > 30 ? detail.getKeywords().substring(0, 29) : detail.getKeywords());
            archivesDO.setDescription(detail.getDescription().length() > 255 ? detail.getDescription().substring(0, 254) : detail.getDescription());
            archivesDO.setClick((int) (1000 * Math.random()));
            archivesDO.setWriter(detail.getAuthor());
            archivesDO.setSource(detail.getSource());
            archivesDO.setWeight(arctinyDO.getId());
            archivesDO.setDutyadmin(1);
            archivesDO.setFlag(flag);
            archivesDO.setLitpic(litpic);
            archivesDO.setFilename(detail.getFileName());
            archivesDAO.insertArchives(archivesDO);

            String content = detail.getContent();
            if (!childPageConfig.getContent().getWashContent().equals("")) {
                content = WashUtil.washData(content, childPageConfig.getContent().getWashContent());
            }
            AddonarticleDO addonarticleDO = new AddonarticleDO();
            addonarticleDO.setAid(arctinyDO.getId());
            addonarticleDO.setTypeid(typeid);
            addonarticleDO.setBody(content);
            addonarticleDAO.insertAddonarticle(addonarticleDO);
            log4j.logDebug("Save OK.");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /* 保存已经获取内容的URL，如果保存出现主键重复的异常，说明该URL已经获取过内容，为正常现象 */
    protected void saveDownUrl(ParentPage parentPageConfig, ChildPageDetail detail) throws SQLException {
        if (parentPageConfig.isFilterDownloadUrl()) {
            DownurlDO downurlDO = new DownurlDO();
            downurlDO.setUrl(detail.getUrl());
            downurlDAO.insertDownurl(downurlDO);
        }
    }

    /**
     * 根据配置翻译的条件，将当前内容翻译为指定的语言
     * 
     * @param parentPageConfig
     * @param detail
     * @throws Exception
     */
    private void translate(ParentPage parentPageConfig, ChildPageDetail detail) throws Exception {
        String from = parentPageConfig.getTranslater().getFrom();
        String to = parentPageConfig.getTranslater().getTo();
        if (!StringUtil.isNull(from) && !StringUtil.isNull(to)) {
            log4j.logDebug("Translate begin,from " + from + " to " + to + "." + System.currentTimeMillis());
            detail.setAuthor(TranslatorUtil.translateHTML(detail.getAuthor(), from, to));
            detail.setContent(TranslatorUtil.translateHTML(detail.getContent(), from, to));
            detail.setDescription(TranslatorUtil.translateHTML(detail.getDescription(), from, to));
            detail.setKeywords(TranslatorUtil.translateHTML(detail.getKeywords(), from, to));
            detail.setSource(TranslatorUtil.translateHTML(detail.getSource(), from, to));
            detail.setTitle(TranslatorUtil.translateHTML(detail.getTitle(), from, to));
            log4j.logDebug("Translate end." + System.currentTimeMillis());
        }
    }

    public void setArctinyDAO(ArctinyDAO arctinyDAO) {
        this.arctinyDAO = arctinyDAO;
    }

    public void setArchivesDAO(ArchivesDAO archivesDAO) {
        this.archivesDAO = archivesDAO;
    }

    public void setAddonarticleDAO(AddonarticleDAO addonarticleDAO) {
        this.addonarticleDAO = addonarticleDAO;
    }

    public void setDownurlDAO(DownurlDAO downurlDAO) {
        this.downurlDAO = downurlDAO;
    }

    @Override
    protected int getDealedArticleNum() {
        return dealedArticleNum;
    }

    public void setAutoDetectTypes(AutoDetectTypes autoDetectTypes) {
        this.autoDetectTypes = autoDetectTypes;
    }
}
