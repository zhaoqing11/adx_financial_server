package com.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentApproval {

    private Integer idPaymentApproval;

    private Integer idPaymentForm;

    private BigDecimal amount;

    private Integer idUser;

    private String createTime;
}
