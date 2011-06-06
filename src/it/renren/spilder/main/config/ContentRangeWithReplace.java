package it.renren.spilder.main.config;


public class ContentRangeWithReplace extends ContentRange {

    private boolean issRegularExpression;
    private String  from;
    private String  to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isIssRegularExpression() {
        return issRegularExpression;
    }

    public void setIssRegularExpression(boolean issRegularExpression) {
        this.issRegularExpression = issRegularExpression;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
