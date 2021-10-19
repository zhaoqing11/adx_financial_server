package com.project.entity;

import lombok.Data;

@Data
public class PubGeneralReport {

    private Integer idPubGeneralReport;

    private Integer year;

    private Integer month;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String createTime;

}
