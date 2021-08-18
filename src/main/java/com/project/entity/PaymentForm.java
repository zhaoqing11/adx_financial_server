package com.project.entity;

import lombok.Data;

@Data
public class PaymentForm {

    private Integer idPaymentForm;

    private String code;

    private String reasonApplication;

    private String amount;

    private String paymentName;

    private String paymentAccount;

    private Integer idUser;

    private Integer idPaymentFormState;

    private String createTime;

    private String userName;

    private String approvalAmount;

    private String approvalUser;

    private String remittanceAmount;

    private String serviceCharge;

    private String remittanceUser;

    private String remainingSum;

    private Integer idFlowType;

    private Integer idPayFlowRecord;

    private Integer idIncomeFlowRecord;

    private String collectionAmount;

    private Integer idCardType;

    private boolean state;

}
