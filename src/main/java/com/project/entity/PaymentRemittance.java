package com.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRemittance {

    private Integer idPaymentRemittance;

    private Integer idPaymentForm;

    private BigDecimal amount;

    private BigDecimal serviceCharge;

    private Integer idUser;

    private String createTime;
}
