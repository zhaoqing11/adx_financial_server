package com.project.entity;

public class ConfigVO {

    private String cardNum;

    private String remainingSum;

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

    public boolean isCyclical() {
        return isCyclical;
    }

    public void setCyclical(boolean cyclical) {
        isCyclical = cyclical;
    }

}
