package com.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentForm {

    private Integer idPaymentForm;

    private String code;

    private String reasonApplication;

    private BigDecimal amount;

    private String paymentName;

    private String paymentAccount;

    private Integer idUser;

    private String createTime;
}
