package com.project.entity;

import lombok.Data;

@Data
public class IncomeFlowRecord {

    private Integer idIncomeFlowRecord;

    private Integer idCollectionRecord;

    private Integer idFlowType;

    private String amount;

    private String remainingSum;

    private String createTime;

}
