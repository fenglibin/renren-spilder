package it.renren.spilder.filter.seperatepage;

import it.renren.spilder.main.Constants;

/*
 * IBM�����·�ҳ�����������ģ�<br> http://www.ibm.com/developerworks/cn/education/java/j-grails/index.html
 * http://www.ibm.com/developerworks/cn/education/java/j-grails/section2.html
 * http://www.ibm.com/developerworks/cn/education/java/j-grails/section2.html
 */
public class XS321SeparatePage implements ISeparatePage {

    @Override
    public String getSeparatePageUrl(String pageUrl, int pageNo) {

        if (pageNo == Constants.DEFAULT_SEPERATE_PAGE) {
            return pageUrl;
        }
        pageUrl = pageUrl + Constants.URL_SEPARATOR + pageNo + Constants.URL_SEPARATOR;
        return pageUrl;
    }

}
