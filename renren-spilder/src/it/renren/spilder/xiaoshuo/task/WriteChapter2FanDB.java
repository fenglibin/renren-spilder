package it.renren.spilder.xiaoshuo.task;

import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.task.Task;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;
import it.renren.spilder.xiaoshuo.dao.ChaptersDAO;
import it.renren.spilder.xiaoshuo.dataobject.Books;
import it.renren.spilder.xiaoshuo.dataobject.Chapters;
import it.renren.spilder.xiaoshuo.dataobject.Downurl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteChapter2FanDB extends Task {

    private static Log4j         log4j            = new Log4j(WriteChapter2DB.class.getName());
    private static int           dealedArticleNum = 0;
    private Map<String, Integer> bookUrlMap       = new HashMap<String, Integer>();

    BooksDAO                     booksDAOFanti;
    ChaptersDAO                  chaptersDAOFanti;
    DownurlDAO                   downurlDAOFanti;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            if (isDealed(detail.getUrl())) {
                return;
            } else {
                saveDownUrl(detail.getUrl());
            }
            ChildPageDetail detailClone = detail.clone();
            dealedArticleNum++;
            log4j.logDebug("¿ªÊ¼±£´æ:" + detailClone.getUrl());

            Chapters chapter = new Chapters();

            Integer bookId = bookUrlMap.get(detail.getParentPageUrl());
            if (bookId == null || bookId <= 0) {
                Books book = booksDAOFanti.selectBySpilderUrl(detail.getParentPageUrl());
                bookId = book.getId();
                bookUrlMap.put(detail.getParentPageUrl(), bookId);
            }

            chapter.setBookId(bookId);
            chapter.setTitle(FontUtil.jian2fan(new StringBuffer(detailClone.getTitle())));
            chapter.setContext(FontUtil.jian2fan(new StringBuffer(detailClone.getContent())));
            chapter.setIntime(new Date());
            chapter.setIsgenhtml(Boolean.FALSE);

            chaptersDAOFanti.insert(chapter);

            log4j.logDebug("Save OK.");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    @Override
    protected int getDealedArticleNum() {
        return dealedArticleNum;
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
        Downurl downurlDO = new Downurl();
        downurlDO.setUrl(url);
        downurlDO.setIntime(new Date());
        downurlDAOFanti.insertDownurl(downurlDO);
    }

    public void setBooksDAOFanti(BooksDAO booksDAOFanti) {
        this.booksDAOFanti = booksDAOFanti;
    }

    public void setChaptersDAOFanti(ChaptersDAO chaptersDAOFanti) {
        this.chaptersDAOFanti = chaptersDAOFanti;
    }

    public void setDownurlDAOFanti(DownurlDAO downurlDAOFanti) {
        this.downurlDAOFanti = downurlDAOFanti;
    }

}
