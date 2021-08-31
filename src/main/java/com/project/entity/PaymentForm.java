package com.project.entity;

import lombok.Data;

@Data
public class PaymentForm {

    private Integer idPaymentForm;

    private String code;

    private Integer idCardType;

    private String reasonApplication;

    private String amount;

    private String paymentName;

    private String paymentAccount;

    private Integer idUser;

    private Integer idPaymentFormState;

    private boolean state;

    private String files;

    private String createTime;

    private String userName;

    private String approvalAmount;

    private String approvalUser;

    private Integer idPaymentRemittance;

    private String remittanceAmount;

    private String serviceCharge;

    private String remittanceUser;

    private String remittanceDate;

    private String remainingSum;

    private Integer idFlowType;

    private Integer idPayFlowRecord;

    private Integer idIncomeFlowRecord;

    private Integer idCollectionRecord;

    private String collectionAmount;

    private String collectionDate;

    private String remark;

    private Integer idDepartment;

    private String departmentName;

}
