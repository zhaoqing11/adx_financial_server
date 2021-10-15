package com.project.entity;

import lombok.Data;

@Data
public class SecondGeneralAccountDaily {

    private Integer idSecondGeneralAccountDaily;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String remainingSum;

    private Integer state;

    private String createTime;
}
