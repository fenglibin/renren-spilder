package it.renren.spilder.xiaoshuo.task;

import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.task.Task;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;
import it.renren.spilder.xiaoshuo.dao.ChaptersDAO;
import it.renren.spilder.xiaoshuo.dataobject.Books;
import it.renren.spilder.xiaoshuo.dataobject.Chapters;
import it.renren.spilder.xiaoshuo.dataobject.Downurl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteChapter2DB extends Task {

    private static Log4j         log4j            = new Log4j(WriteChapter2DB.class.getName());
    private static int           dealedArticleNum = 0;
    private Map<String, Integer> bookUrlMap       = new HashMap<String, Integer>();

    BooksDAO                     booksDAO;
    ChaptersDAO                  chaptersDAO;
    DownurlDAO                   downurlDAO;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            boolean saveUrl = false;
            if (isDealed(detail.getUrl())) {
                return;
            } else {
                saveUrl = true;
            }
            ChildPageDetail detailClone = detail.clone();
            dealedArticleNum++;
            log4j.logDebug("开始保存:" + detailClone.getUrl());

            Chapters chapter = new Chapters();

            Integer bookId = bookUrlMap.get(detail.getParentPageUrl());
            if (bookId == null || bookId <= 0) {
                Books book = booksDAO.selectBySpilderUrl(detail.getParentPageUrl());
                bookId = book.getId();
                bookUrlMap.put(detail.getParentPageUrl(), bookId);
            }

            chapter.setBookId(bookId);
            chapter.setTitle(detailClone.getTitle().replace("/墨坛文学", ""));
            chapter.setContext(detailClone.getContent());
            chapter.setIntime(new Date());
            chapter.setIsgenhtml(Boolean.FALSE);

            chaptersDAO.insert(chapter);
            if (saveUrl) {
                saveDownUrl(detail.getUrl());
            }
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
        DownurlDO downurlDO = downurlDAO.selectDownurl(url);
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
        downurlDAO.insertDownurl(downurlDO);
    }

    public void setDownurlDAO(DownurlDAO downurlDAO) {
        this.downurlDAO = downurlDAO;
    }

    public void setBooksDAO(BooksDAO booksDAO) {
        this.booksDAO = booksDAO;
    }

    public void setChaptersDAO(ChaptersDAO chaptersDAO) {
        this.chaptersDAO = chaptersDAO;
    }

}
