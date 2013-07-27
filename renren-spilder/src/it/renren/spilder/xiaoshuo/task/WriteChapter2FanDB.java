package it.renren.spilder.xiaoshuo.task;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dao.FeedbackDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.dataobject.FeedbackDO;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.task.Task;
import it.renren.spilder.type.Type;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.log.Log4j;

public class WriteChapter2FanDB extends Task {

    private static Log4j log4j            = new Log4j(WriteChapter2FanDB.class.getName());
    private static int   dealedArticleNum = 0;
    ArctinyDAO           arctinyDAOFanti;
    ArchivesDAO          archivesDAOFanti;
    AddonarticleDAO      addonarticleDAOFanti;
    DownurlDAO           downurlDAOFanti;
    Type                 type;
    FeedbackDAO          feedbackDAOFanti;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            if (isDealed(detail.getUrl())) {
                return;
            } else {
                saveDownUrl(detail.getUrl());
            }
            ChildPageDetail detailClone = detail.clone();
            dealedArticleNum++;
            log4j.logDebug("开始保存:" + detailClone.getUrl());
            int typeid = type.getType(parentPageConfig, detailClone);
            ArctinyDO arctinyDO = new ArctinyDO();

            int tempTypeId = (int) (Math.random() * 1000) + 9999;/* 临时ID，主要用于获取当前插入的自增ID */
            arctinyDO.setTypeid(tempTypeId);
            arctinyDAOFanti.insertArctiny(arctinyDO);
            // 将插入的自增ID给查询出来
            arctinyDO = arctinyDAOFanti.selectArctinyByTypeId(arctinyDO);
            arctinyDO.setTypeid(typeid);
            arctinyDAOFanti.updateArctinyTypeidById(arctinyDO);

            String flag = getFlag(parentPageConfig, detailClone);

            String litpic = detailClone.getLitpicAddress();// 缩略图地址

            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(arctinyDO.getId());
            archivesDO.setTypeid(typeid);
            archivesDO.setTitle(jian2fan(detailClone.getTitle().length() > 100 ? detailClone.getTitle().substring(0, 99) : detailClone.getTitle()));
            archivesDO.setKeywords(jian2fan(detailClone.getKeywords().length() > 30 ? detailClone.getKeywords().substring(0,
                                                                                                                          29) : detailClone.getKeywords()));
            archivesDO.setDescription(jian2fan(detailClone.getDescription().length() > 255 ? detailClone.getDescription().substring(0,
                                                                                                                                    254) : detailClone.getDescription()));
            archivesDO.setClick((int) (1000 * Math.random()));
            archivesDO.setWriter(jian2fan(detailClone.getAuthor()));
            archivesDO.setSource(jian2fan(detailClone.getSource()));
            archivesDO.setWeight(arctinyDO.getId());
            archivesDO.setDutyadmin(1);
            archivesDO.setFlag(flag);
            archivesDO.setLitpic(litpic);
            archivesDO.setFilename(detailClone.getFileName());
            archivesDAOFanti.insertArchives(archivesDO);

            String content = detailClone.getContent();
            content = addSourceUrl(childPageConfig, detailClone, content);
            AddonarticleDO addonarticleDO = new AddonarticleDO();
            addonarticleDO.setAid(arctinyDO.getId());
            addonarticleDO.setTypeid(typeid);
            addonarticleDO.setBody(jian2fan(content));
            addonarticleDAOFanti.insertAddonarticle(addonarticleDO);

            /** 对回复的处理 */
            if (detailClone.getReplys().size() > 0) {
                for (String reply : detailClone.getReplys()) {
                    FeedbackDO feedbackDO = new FeedbackDO();
                    feedbackDO.setAid(arctinyDO.getId());
                    feedbackDO.setArctitle(archivesDO.getTitle());
                    feedbackDO.setTypeid(typeid);
                    feedbackDO.setMsg(jian2fan(reply));
                    feedbackDAOFanti.insertFeedback(feedbackDO);
                }
            }
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

    @Override
    public boolean isDealed(String url) {
        boolean is = Boolean.FALSE;
        DownurlDO downurlDO = downurlDAOFanti.selectDownurl(url);
        if (downurlDO != null) {
            is = Boolean.TRUE;
        }
        return is;
    }

    @Override
    public void saveDownUrl(String url) {
        DownurlDO downurlDO = new DownurlDO();
        downurlDO.setUrl(url);
        downurlDAOFanti.insertDownurl(downurlDO);

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

    public void setType(Type type) {
        this.type = type;
    }

    public void setFeedbackDAOFanti(FeedbackDAO feedbackDAOFanti) {
        this.feedbackDAOFanti = feedbackDAOFanti;
    }

}
