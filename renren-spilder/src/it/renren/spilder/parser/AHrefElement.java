package it.renren.spilder.parser;

public class AHrefElement {

    private String href;
    private String hrefText;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHrefText() {
        return hrefText;
    }

    public void setHrefText(String hrefText) {
        this.hrefText = hrefText;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof AHrefElement)) {
            return false;
        }
        AHrefElement e = (AHrefElement) object;
        if (e.getHref().equals(this.getHref())) {
            return true;
        }
        return true;
    }

    public int hashCode() {
        return href.hashCode();
    }
}
