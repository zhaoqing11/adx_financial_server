package com.project.entity;

import lombok.Data;

@Data
public class PaymentRemittance {

    private Integer idPaymentRemittance;

    private Integer idPaymentForm;

    private String amount;

    private String serviceCharge;

    private Integer idUser;

    private String createTime;
}
