package com.project.entity;

/**
 * 发送短信变量实体类
 *
 * @author: zhao
 */
public class MessageVO {

    // 日期
    private String date;

    // 账号（公账）
    private String card;

    // 余额（公账）
    private String remainingSum;

    // 收入（公账）
    private String collectionAmount;

    // 支出（公账）
    private String payAmount;

    // 手续费（公账）
    private String serviceCharge;

    // 接收短信手机号
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getRemainingSum() {
        return remainingSum;
    }

    public void setRemainingSum(String remainingSum) {
        this.remainingSum = remainingSum;
    }

    public String getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

}
