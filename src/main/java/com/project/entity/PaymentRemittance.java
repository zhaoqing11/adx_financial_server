package com.project.entity;

import lombok.Data;

@Data
public class PaymentRemittance {

    private Integer idPaymentRemittance;

    private Integer idPaymentForm;

    private Integer idCardType;

    private String amount;

    private String serviceCharge;

    private String remittanceDate;

    private Integer idUser;

    private String createTime;

    private Integer idDepartment;

    private String departmentName;

    private String remark;

}
