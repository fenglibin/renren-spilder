package it.renren.spilder.xiaoshuo.dataobject;

public class Books {
    private Integer id;

    private Integer typeId;

    private String name;

    private Boolean recommend;

    private Boolean specialrecommend;

    private String author;

    private String img;

    private String description;

    private Boolean isfinished;

    private String spilderurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Boolean getSpecialrecommend() {
        return specialrecommend;
    }

    public void setSpecialrecommend(Boolean specialrecommend) {
        this.specialrecommend = specialrecommend;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getIsfinished() {
        return isfinished;
    }

    public void setIsfinished(Boolean isfinished) {
        this.isfinished = isfinished;
    }

    public String getSpilderurl() {
        return spilderurl;
    }

    public void setSpilderurl(String spilderurl) {
        this.spilderurl = spilderurl == null ? null : spilderurl.trim();
    }
}