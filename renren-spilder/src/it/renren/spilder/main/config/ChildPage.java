package it.renren.spilder.main.config;

public class ChildPage {

    private String       url;
    private String       charset;
    private Title        title;
    private ContentRange keywords;
    private ContentRange description;
    private Content      content;
    private boolean      isAddUrl;
    private String       addUrlDisplayString;
    private boolean      isKeepFileName;
    private Replys       replys                         = new Replys();
    // 是否处理当前页面中类似的URL，默认的值为true。值只可以是true中或false
    private boolean      expandUrl                      = true;
    // 是否需要检查当前页面的URL是否已经处理过来
    private boolean      needToCheckUrlIsAlreadyOperate = true;

    public boolean isKeepFileName() {
        return isKeepFileName;
    }

    public void setKeepFileName(boolean isKeepFileName) {
        this.isKeepFileName = isKeepFileName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Content getContent() {
        if (content == null) {
            content = new Content();
        }
        return content;
    }

    public ContentRange getDescription() {
        if (description == null) {
            description = new ContentRange();
        }
        return description;
    }

    public ContentRange getKeywords() {
        if (keywords == null) {
            keywords = new ContentRange();
        }
        return keywords;
    }

    public Title getTitle() {
        if (title == null) {
            title = new Title();
        }
        return title;
    }

    public boolean isAddUrl() {
        return isAddUrl;
    }

    public void setAddUrl(boolean isAddUrl) {
        this.isAddUrl = isAddUrl;
    }

    public String getAddUrlDisplayString() {
        return addUrlDisplayString;
    }

    public void setAddUrlDisplayString(String addUrlDisplayString) {
        this.addUrlDisplayString = addUrlDisplayString;
    }

    public Replys getReplys() {
        return replys;
    }

    public boolean isExpandUrl() {
        return expandUrl;
    }

    public void setExpandUrl(boolean expandUrl) {
        this.expandUrl = expandUrl;
    }

    public boolean isNeedToCheckUrlIsAlreadyOperate() {
        return needToCheckUrlIsAlreadyOperate;
    }

    public void setNeedToCheckUrlIsAlreadyOperate(boolean needToCheckUrlIsAlreadyOperate) {
        this.needToCheckUrlIsAlreadyOperate = needToCheckUrlIsAlreadyOperate;
    }

}
