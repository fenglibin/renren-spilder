package it.renren.spilder.xiaoshuo.filter;

import it.renren.spilder.filter.UrlListProvider;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;

import java.util.List;

public class BooksUrlListProvider implements UrlListProvider {

    private BooksDAO booksDAO;

    @Override
    public List<String> getUrls() {
        return booksDAO.selectSpilderUrls();
    }

    public void setBooksDAO(BooksDAO booksDAO) {
        this.booksDAO = booksDAO;
    }

}
