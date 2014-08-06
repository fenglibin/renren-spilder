package it.renren.spilder.xiaoshuo.task;

import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.task.Task;
import it.renren.spilder.type.Type;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.google.TranslatorUtil;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;
import it.renren.spilder.xiaoshuo.dao.DownurlDAO;
import it.renren.spilder.xiaoshuo.dataobject.Books;
import it.renren.spilder.xiaoshuo.dataobject.Downurl;

import java.util.Date;

public class WriteBook2DB extends Task {

    private static Log4j log4j            = new Log4j(WriteBook2DB.class.getName());
    private static int   dealedArticleNum = 0;
    BooksDAO             booksDAO;

    Type                 booksType;
    DownurlDAO           downurlDAO;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            boolean saveUrl = false;
            if (isDealed(detail.getUrl())) {
                if (childPageConfig.isNeedToCheckUrlIsAlreadyOperate()) {
                    return;
                }
            } else {
                saveUrl = true;
            }
            ChildPageDetail detailClone = detail.clone();
            translate(parentPageConfig, detailClone);
            dealedArticleNum++;
            log4j.logDebug("开始保存:" + detailClone.getUrl());
            int typeid = booksType.getType(parentPageConfig, detailClone);
            Books book = new Books();
            book.setTypeId(typeid);
            book.setRecommend(Boolean.FALSE);
            book.setSpecialrecommend(Boolean.FALSE);
            book.setIsfinished(Boolean.FALSE);

            String imgUrl = detailClone.getLitpicAddress().replace("-lp", "");
            book.setImg(imgUrl);
            book.setSpilderurl(detailClone.getUrl());
            String author = StringUtil.subString(detailClone.getContent(), "<p><strong>作者: </strong><span>", "</span></p>");
            book.setAuthor(author);

            String desc = StringUtil.subString(detailClone.getContent(), "<div class=\"movieIntro\">", null);
            book.setDescription(desc);
            book.setName(detailClone.getTitle());

            if (booksDAO.selectBySpilderUrl(book.getSpilderurl()) == null) {
                booksDAO.insert(book);
            }
            if (saveUrl) {
                saveDownUrl(detail.getUrl());
            }
            log4j.logDebug("Save OK.");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
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
        if (!StringUtil.isEmpty(from) && !StringUtil.isEmpty(to)) {
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

    public void setBooksType(Type booksType) {
        this.booksType = booksType;
    }

    public void setBooksDAO(BooksDAO booksDAO) {
        this.booksDAO = booksDAO;
    }

}
