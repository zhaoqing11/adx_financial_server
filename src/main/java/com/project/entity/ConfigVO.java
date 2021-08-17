package com.project.entity;

public class ConfigVO {

    private String remainingSum;

    private String cardNum;

    private String timeUnit;

    private boolean isCyclical;

    public String getRemainingSum() {
        return remainingSum;
    }

    public void setRemainingSum(String remainingSum) {
        this.remainingSum = remainingSum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isCyclical() {
        return isCyclical;
    }

    public void setCyclical(boolean cyclical) {
        isCyclical = cyclical;
    }

}
