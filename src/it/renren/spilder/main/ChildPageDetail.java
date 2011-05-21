package it.renren.spilder.main;

public class ChildPageDetail {

    /* 标题 */
    private String  title;
    /* 关键字 */
    private String  keywords;
    /* 描述 */
    private String  description;
    /* 作者 */
    private String  author        = "Internet";
    /* 文章来源 */
    private String  source        = "Internet";
    /* 文章内容 */
    private String  content;
    /* 当前文章获取的URL地址 */
    private String  url;
    /* 是否包括有图片 */
    private boolean isPicArticle;
    /* 缩略图的地址 */
    private String  litpicAddress = "";
    /* 获取内容网页的网页文件名 */
    private String  fileName      = "";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPicArticle() {
        return isPicArticle;
    }

    public void setPicArticle(boolean isPicArticle) {
        this.isPicArticle = isPicArticle;
    }

    public String getLitpicAddress() {
        return litpicAddress;
    }

    public void setLitpicAddress(String litpicAddress) {
        this.litpicAddress = litpicAddress;
    }

    public ChildPageDetail clone() {
        ChildPageDetail detail = new ChildPageDetail();
        detail.author = author;
        detail.content = content;
        detail.description = description;
        detail.isPicArticle = isPicArticle;
        detail.keywords = keywords;
        detail.litpicAddress = litpicAddress;
        detail.source = source;
        detail.title = title;
        detail.url = url;
        return detail;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("title = ").append(title).append(",");
        sb.append("keywords = ").append(keywords).append(",");
        sb.append("description = ").append(description).append(",");
        sb.append("author = ").append(author).append(",");
        sb.append("source = ").append(source).append(",");
        sb.append("content = ").append(content).append(",");
        sb.append("url = ").append(url).append(",");
        sb.append("isPicArticle = ").append(isPicArticle).append(",");
        sb.append("litpicAddress = ").append(litpicAddress).append(",");
        sb.append("fileName = ").append(fileName);
        return sb.toString();
    }
}
