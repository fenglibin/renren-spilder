package it.renren.spilder.filter.seperatepage;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.FileUtil;

/*
 * IBM的文章分页是如下这样的：<br> http://www.ibm.com/developerworks/cn/education/java/j-grails/index.html
 * http://www.ibm.com/developerworks/cn/education/java/j-grails/section2.html
 * http://www.ibm.com/developerworks/cn/education/java/j-grails/section2.html
 */
public class IBMSeparatePage implements ISeparatePage {

    @Override
    public String getSeparatePageUrl(String pageUrl, int pageNo) {

        if (pageNo == Constants.DEFAULT_SEPERATE_PAGE) {
            return pageUrl;
        }
        String urlFileName = FileUtil.getFileName(pageUrl);
        if (urlFileName.indexOf(Constants.DOT) < 0) {
            throw new RuntimeException("Can't deal the url without extension file name:" + pageUrl);
        }
        pageUrl = pageUrl.replace(urlFileName, "");
        pageUrl = pageUrl + "section" + pageNo + ".html";
        return pageUrl;
    }

    public static void main(String[] args) {
        IBMSeparatePage page = new IBMSeparatePage();
        String url = "http://www.ibm.com/developerworks/cn/education/java/j-grails/index.html";
        System.out.println(FileUtil.getFileName(url));
        int pageNo = 3;
        for (int i = 1; i <= pageNo; i++) {
            String resultUrl = page.getSeparatePageUrl(url, i);
            System.out.println(resultUrl);
        }
    }

}
