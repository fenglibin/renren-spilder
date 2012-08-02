package it.renren.spilder.filter;

import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;

public interface Filter {

    public String filterContent(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent)
                                                                                                           throws Exception;
}
