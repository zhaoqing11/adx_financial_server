package com.project.entity;

import lombok.Data;

@Data
public class PayFlowRecord {

    private Integer idPayFlowRecord;

    private Integer idPaymentRemittance;

    private Integer idFlowType;

    private String amount;

    private String remainingSum;

    private String createTime;

}
