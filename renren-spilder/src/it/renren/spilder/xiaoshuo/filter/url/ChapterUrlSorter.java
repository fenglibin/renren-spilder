package it.renren.spilder.xiaoshuo.filter.url;

import it.renren.spilder.filter.url.UrlSorter;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.util.StringUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChapterUrlSorter implements UrlSorter {

    @Override
    public void sortUrl(List<AHrefElement> urlList) {
        if (urlList == null || urlList.isEmpty()) {
            return;
        }
        try {
            Collections.sort(urlList, new Comparator<AHrefElement>() {

                @Override
                public int compare(AHrefElement o1, AHrefElement o2) {
                    String o1Url = o1.getHref();
                    String o2Url = o2.getHref();
                    if (StringUtil.isEmpty(o1Url) || StringUtil.isEmpty(o2Url)) {
                        return 0;
                    }
                    return getChapterId(o1Url) - getChapterId(o2Url);
                }

                private int getChapterId(String url) {
                    String[] arr = url.split("/");
                    url = arr[arr.length - 1];
                    return Integer.parseInt(url);
                }

            });
        } catch (Exception e) {

        }
    }
}
