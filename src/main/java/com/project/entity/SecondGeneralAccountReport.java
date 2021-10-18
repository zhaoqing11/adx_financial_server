package com.project.entity;

import lombok.Data;

@Data
public class SecondGeneralAccountReport {

    private Integer idSecondGeneralAccountReport;

    private Integer year;

    private Integer month;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String createTime;

    private String lastRemainingSum;

    private int day;

}
