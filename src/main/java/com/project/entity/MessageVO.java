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
    private String cardPub;

    // 余额（公账）
    private String remainingSumPub;

    // 收入（公账）
    private String collectionAmountPub;

    // 支出（公账）
    private String payAmountPub;

    // 手续费（公账）
    private String serviceChargePub;

    // 账号（私账）
    private String cardPri;

    // 余额（私账）
    private String remainingSumPri;

    // 收入（私账）
    private String collectionAmountPri;

    // 支出（私账）
    private String payAmountPri;

    // 手续费（私账）
    private String serviceChargePri;

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

    public String getCardPub() {
        return cardPub;
    }

    public void setCardPub(String cardPub) {
        this.cardPub = cardPub;
    }

    public String getRemainingSumPub() {
        return remainingSumPub;
    }

    public void setRemainingSumPub(String remainingSumPub) {
        this.remainingSumPub = remainingSumPub;
    }

    public String getCollectionAmountPub() {
        return collectionAmountPub;
    }

    public void setCollectionAmountPub(String collectionAmountPub) {
        this.collectionAmountPub = collectionAmountPub;
    }

    public String getPayAmountPub() {
        return payAmountPub;
    }

    public void setPayAmountPub(String payAmountPub) {
        this.payAmountPub = payAmountPub;
    }

    public String getServiceChargePub() {
        return serviceChargePub;
    }

    public void setServiceChargePub(String serviceChargePub) {
        this.serviceChargePub = serviceChargePub;
    }

    public String getCardPri() {
        return cardPri;
    }

    public void setCardPri(String cardPri) {
        this.cardPri = cardPri;
    }

    public String getRemainingSumPri() {
        return remainingSumPri;
    }

    public void setRemainingSumPri(String remainingSumPri) {
        this.remainingSumPri = remainingSumPri;
    }

    public String getCollectionAmountPri() {
        return collectionAmountPri;
    }

    public void setCollectionAmountPri(String collectionAmountPri) {
        this.collectionAmountPri = collectionAmountPri;
    }

    public String getPayAmountPri() {
        return payAmountPri;
    }

    public void setPayAmountPri(String payAmountPri) {
        this.payAmountPri = payAmountPri;
    }

    public String getServiceChargePri() {
        return serviceChargePri;
    }

    public void setServiceChargePri(String serviceChargePri) {
        this.serviceChargePri = serviceChargePri;
    }
}
