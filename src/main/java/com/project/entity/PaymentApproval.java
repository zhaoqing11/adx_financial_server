package com.project.entity;

import lombok.Data;

@Data
public class PaymentApproval {

    private Integer idPaymentApproval;

    private Integer idPaymentForm;

    private String amount;

    private Integer idUser;

    private String createTime;
}
