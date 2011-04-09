package it.renren.spilder.main;

import it.renren.spilder.util.BshUtil;
import it.renren.spilder.util.StringUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import bsh.EvalError;

public class ParentPage {

    private String     charset;
    private String     imageDescUrl;
    private String     imageSaveLocation;
    private String     desArticleId;
    private int        randRecommandFrequency;
    private boolean    isSRcommand;
    private boolean    filterDownloadUrl;
    private long       oneUrlSleepTime = 0;
    private Translater translater;
    private String     autoDetectTypeMapClass;

    public boolean isSRcommand() {
        return isSRcommand;
    }

    public void setSRcommand(boolean isSRcommand) {
        this.isSRcommand = isSRcommand;
    }

    public String getDesArticleId() {
        return desArticleId;
    }

    public void setDesArticleId(String desArticleId) {
        this.desArticleId = desArticleId;
    }

    private UrlListPages urlListPages;
    private Content      content;
    private UrlFilter    urlFilter;

    class UrlListPages {

        private List<String> listPages = new ArrayList<String>();

        public List<String> getListPages() {
            return listPages;
        }

        public void setValues(Element values, boolean onePage) throws EvalError {
            analysisLisPages(values, onePage);
        }

        private void analysisLisPages(Element values, boolean onePage) throws EvalError {
            Element value = values.getChild("Value");
            if (value != null && value.getTextTrim() != "") {
                String[] urls = value.getTextTrim().split(",");
                for (String url : urls) {
                    listPages.add(url);
                }
            }
            Element batValues = values.getChild("BatValues");
            if (batValues != null) {
                String batBaseUrl = batValues.getChild("Value").getTextTrim();
                String wildcastType = batValues.getChild("WildcastType").getChild("Value").getTextTrim();

                if (wildcastType.equals(Constants.WILDCAST_TYPE_NUMBER)) {
                    int start = Integer.parseInt(batValues.getChild("WildcastType").getChild("Start").getChild("Value").getTextTrim());
                    int end = Integer.parseInt(batValues.getChild("WildcastType").getChild("End").getChild("Value").getTextTrim());
                    for (int i = start; i <= end; i++) {
                        listPages.add(getAddUrl(batBaseUrl, String.valueOf(i)));
                        if (onePage) {
                            i = end;
                        }
                    }
                } else if (wildcastType.equals(Constants.WILDCAST_TYPE_STRING)) {
                    String start = batValues.getChild("WildcastType").getChild("Start").getChild("Value").getTextTrim();
                    int intStart = (int) start.charAt(0);
                    String end = batValues.getChild("WildcastType").getChild("End").getChild("Value").getTextTrim();
                    int intEnd = (int) end.charAt(0);
                    for (int i = intStart; i <= intEnd; i++) {
                        char c = (char) i;
                        listPages.add(getAddUrl(batBaseUrl, String.valueOf(c)));
                        if (onePage) {
                            i = intEnd;
                        }
                    }
                }
            }
        }

        /**
         * 根据规则返回生成的URL，如变化位非常规则递增，可以通过运算得到其结果，如需要计算，变化位需要需要这种格式：
         * ((*)*10-10),如：http://www.google.org.cn/page/((*)*10-10) 普通格式，只需要： (*),如：http://www.google.org.cn/page/(*)
         * 这样即可
         * 
         * @param batBaseUrl
         * @param index
         * @return
         * @throws EvalError
         */
        private String getAddUrl(String batBaseUrl, String index) throws EvalError {
            String url = "";
            if (batBaseUrl.indexOf("((*)") > 0) {
                String s1 = "((*)" + StringUtil.subString(batBaseUrl, "((*)", ")") + ")";
                String s2 = s1.replace("(*)", String.valueOf(index));
                s2 = BshUtil.eval(s2).toString();
                url = batBaseUrl.replace(s1, s2);
            } else {
                url = batBaseUrl.replace("(*)", index);
            }
            if (url.indexOf("urlencode(") > 0) {
                String cnwords = StringUtil.subString(url, "urlencode(", ")");
                String s1 = "urlencode(" + cnwords + ")";
                String s2 = URLEncoder.encode(cnwords);
                url = url.replace(s1, s2);
            }
            return url;
        }
    }

    class UrlFilter {

        private String  mustInclude;
        private String  mustNotInclude;
        private boolean isCompByRegex;

        public String getMustInclude() {
            return mustInclude;
        }

        public void setMustInclude(String mustInclude) {
            this.mustInclude = mustInclude;
        }

        public String getMustNotInclude() {
            return mustNotInclude;
        }

        public void setMustNotInclude(String mustNotInclude) {
            this.mustNotInclude = mustNotInclude;
        }

        public boolean isCompByRegex() {
            return isCompByRegex;
        }

        public void setCompByRegex(boolean isCompByRegex) {
            this.isCompByRegex = isCompByRegex;
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getImageDescUrl() {
        return imageDescUrl;
    }

    public void setImageDescUrl(String imageDescUrl) {
        this.imageDescUrl = imageDescUrl;
    }

    public String getImageSaveLocation() {
        return imageSaveLocation;
    }

    public void setImageSaveLocation(String imageSaveLocation) {
        this.imageSaveLocation = imageSaveLocation;
    }

    public Content getContent() {
        if (content == null) {
            content = new Content();
        }
        return content;
    }

    public UrlFilter getUrlFilter() {
        if (urlFilter == null) {
            urlFilter = new UrlFilter();
        }
        return urlFilter;
    }

    public UrlListPages getUrlListPages() {
        if (urlListPages == null) {
            urlListPages = new UrlListPages();
        }
        return urlListPages;
    }

    public int getRandRecommandFrequency() {
        return randRecommandFrequency;
    }

    public void setRandRecommandFrequency(int randRecommandFrequency) {
        this.randRecommandFrequency = randRecommandFrequency;
    }

    public boolean isFilterDownloadUrl() {
        return filterDownloadUrl;
    }

    public void setFilterDownloadUrl(boolean filterDownloadUrl) {
        this.filterDownloadUrl = filterDownloadUrl;
    }

    public long getOneUrlSleepTime() {
        return oneUrlSleepTime;
    }

    public void setOneUrlSleepTime(long oneUrlSleepTime) {
        this.oneUrlSleepTime = oneUrlSleepTime;
    }

    public Translater getTranslater() {
        if (translater == null) {
            translater = new Translater();
        }
        return translater;
    }

    public String getAutoDetectTypeMapClass() {
        return autoDetectTypeMapClass;
    }

    public void setAutoDetectTypeMapClass(String autoDetectTypeMapClass) {
        this.autoDetectTypeMapClass = autoDetectTypeMapClass;
    }
}
