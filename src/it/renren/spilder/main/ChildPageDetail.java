package it.renren.spilder.main;

public class ChildPageDetail {

    /* ���� */
    private String  title;
    /* �ؼ��� */
    private String  keywords;
    /* ���� */
    private String  description;
    /* ���� */
    private String  author        = "������";
    /* ������Դ */
    private String  source        = "������";
    /* �������� */
    private String  content;
    /* ��ǰ���»�ȡ��URL��ַ */
    private String  url;
    /* �Ƿ������ͼƬ */
    private boolean isPicArticle;
    /* ����ͼ�ĵ�ַ */
    private String  litpicAddress = "";
    /* ��ȡ������ҳ����ҳ�ļ��� */
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
}
