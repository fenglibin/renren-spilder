package it.renren.spilder.main.detail;

import it.renren.spilder.main.Constants;

import java.util.List;

public class ChildPageDetail {

    /* ���� */
    private String       title;
    /* �ؼ��� */
    private String       keywords;
    /* ���� */
    private String       description;
    /* ���� */
    private String       author          = "Internet";
    /* ������Դ */
    private String       source          = "Internet";
    /* �������� */
    private String       content         = Constants.EMPTY_STRING;
    /* ��ǰ���»�ȡ��URL��ַ */
    private String       url;
    /* �Ƿ������ͼƬ */
    private boolean      isPicArticle;
    /* ����ͼ�ĵ�ַ */
    private String       litpicAddress   = Constants.EMPTY_STRING;
    /* ��ȡ������ҳ����ҳ�ļ��� */
    private String       fileName        = Constants.EMPTY_STRING;

    private List<String> replys;
    private boolean      dealResult;

    // ҳ���ԭʼ����
    private String       originalContent = Constants.EMPTY_STRING;
    // ��ҳ���URL
    private String       parentPageUrl   = Constants.EMPTY_STRING;

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
        detail.fileName = fileName;
        detail.replys = replys;
        detail.originalContent = originalContent;
        return detail;
    }

    public List<String> getReplys() {
        return replys;
    }

    public void setReplys(List<String> replys) {
        this.replys = replys;
    }

    public boolean isDealResult() {
        return dealResult;
    }

    public void setDealResult(boolean dealResult) {
        this.dealResult = dealResult;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getParentPageUrl() {
        return parentPageUrl;
    }

    public void setParentPageUrl(String parentPageUrl) {
        this.parentPageUrl = parentPageUrl;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("title = ").append(title).append(Constants.COMMA);
        sb.append("keywords = ").append(keywords).append(Constants.COMMA);
        sb.append("description = ").append(description).append(Constants.COMMA);
        sb.append("author = ").append(author).append(Constants.COMMA);
        sb.append("source = ").append(source).append(Constants.COMMA);
        sb.append("content = ").append(content).append(Constants.COMMA);
        sb.append("url = ").append(url).append(Constants.COMMA);
        sb.append("isPicArticle = ").append(isPicArticle).append(Constants.COMMA);
        sb.append("litpicAddress = ").append(litpicAddress).append(Constants.COMMA);
        sb.append("fileName = ").append(fileName).append(Constants.COMMA);
        sb.append("parentPageUrl = ").append(parentPageUrl);
        return sb.toString();
    }
}
