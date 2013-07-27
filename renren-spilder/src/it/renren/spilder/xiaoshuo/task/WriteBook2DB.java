package it.renren.spilder.xiaoshuo.task;

import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.parser.ImageElement;
import it.renren.spilder.parser.ImageParser;
import it.renren.spilder.task.Task;
import it.renren.spilder.type.Type;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.google.TranslatorUtil;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;
import it.renren.spilder.xiaoshuo.dao.DownurlDAO;
import it.renren.spilder.xiaoshuo.dataobject.Books;
import it.renren.spilder.xiaoshuo.dataobject.Downurl;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class WriteBook2DB extends Task {

    private static Log4j log4j            = new Log4j(WriteBook2DB.class.getName());
    private static int   dealedArticleNum = 0;
    BooksDAO             booksDAO;

    Type                 booksType;
    DownurlDAO           downurlDAO;

    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        try {
            if (isDealed(detail.getUrl())) {
                if (childPageConfig.isNeedToCheckUrlIsAlreadyOperate()) {
                    return;
                }
            } else {
                saveDownUrl(detail.getUrl());
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
            String start = "<div class=\"lb1\">";
            String end = "</h3></div></div>";
            List<String> bookList = StringUtil.getListFromStart2End(detailClone.getContent(), start, end, Boolean.FALSE);
            String authorStart = "<h2>";
            String authorEnd = "</h2>";
            String descStart = "<h3>";
            for (String bookInfo : bookList) {
                Set<AHrefElement> childLinksList = AHrefParser.ahrefParser(bookInfo, "gb2312");
                String orginalUrl = null;
                String bookName = null;
                String imgUrl = null;
                String author = StringUtil.subString(bookInfo, authorStart, authorEnd);
                author = StringUtil.subStringSmart(author, "：", null);
                String desc = StringUtil.subStringSmart(bookInfo, descStart, null);
                if (childLinksList != null && !childLinksList.isEmpty()) {
                    for (AHrefElement e : childLinksList) {
                        String url = e.getHref().replace(UrlUtil.GO_URL, "");
                        orginalUrl = UrlUtil.makeUrl(detailClone.getUrl(), url);
                        break;
                    }
                }

                List<ImageElement> imageElements = ImageParser.imageParser(bookInfo, "gb2312");
                if (imageElements != null && !imageElements.isEmpty()) {
                    for (ImageElement e : imageElements) {
                        imgUrl = e.getSrc();
                        bookName = e.getAlt();
                        break;
                    }
                }
                book.setAuthor(author);
                book.setImg(imgUrl);
                book.setSpilderurl(orginalUrl);
                book.setDescription(desc);
                book.setName(bookName);
                if (booksDAO.selectBySpilderUrl(orginalUrl) == null) {
                    booksDAO.insert(book);
                }

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
