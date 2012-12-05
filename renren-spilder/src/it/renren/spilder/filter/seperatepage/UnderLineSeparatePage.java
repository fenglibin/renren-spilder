package it.renren.spilder.filter.seperatepage;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.FileUtil;

/**
 * Get the given page's separate page url,this can only deal the url file with extension name,such as:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<b>http://www.onlinedown.net/soft/50127.html?a=b&c=d</b><br>
 * And will the result like this:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<b>http://www.onlinedown.net/soft/50127_2.html?a=b&c=d</b><br>
 * 
 * @author fenglibin
 */
public class UnderLineSeparatePage implements ISeparatePage {

    @Override
    public String getSeparatePageUrl(String pageUrl, int pageNo) {
        if (pageNo == Constants.DEFAULT_SEPERATE_PAGE) {
            return pageUrl;
        }
        String urlFileName = FileUtil.getFileName(pageUrl);
        if (urlFileName.indexOf(Constants.DOT) < 0) {
            throw new RuntimeException("Can't deal the url without extension file name:" + pageUrl);
        }
        String urlFileExtName = FileUtil.getFileExtensation(urlFileName);
        urlFileExtName = Constants.DOT + urlFileExtName;
        String newUrlFileName = urlFileName.replace(urlFileExtName, Constants.SEPARATE_PAGE_SEPARATOR + pageNo
                                                                    + urlFileExtName);
        String childUrl = pageUrl.replace(urlFileName, newUrlFileName);
        return childUrl;
    }

    public static void main(String[] args) {
        UnderLineSeparatePage page = new UnderLineSeparatePage();
        String url = "http://www.onlinedown.net/soft/1.2.3.50127.html?a=b&c=d";
        int pageNo = 2;
        String resultUrl = page.getSeparatePageUrl(url, pageNo);
        System.out.println(resultUrl);
    }
}
