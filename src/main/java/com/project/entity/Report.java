package com.project.entity;

import lombok.Data;

@Data
public class Report {

    private Integer idReport;

    private Integer year;

    private Integer month;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String createTime;

    private String lastRemainingSum;

    private int day;

}
