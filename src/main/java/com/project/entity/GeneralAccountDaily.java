package com.project.entity;

import lombok.Data;

@Data
public class GeneralAccountDaily {

    private Integer idGeneralAccountDaily;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String remainingSum;

    private boolean state;

    private String createTime;
}
