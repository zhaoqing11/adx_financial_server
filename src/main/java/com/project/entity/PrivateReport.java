package com.project.entity;

import lombok.Data;

@Data
public class PrivateReport {

    private Integer idPrivateReport;

    private Integer year;

    private Integer month;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String createTime;

    private String lastRemainingSum;

    private int day;

}
