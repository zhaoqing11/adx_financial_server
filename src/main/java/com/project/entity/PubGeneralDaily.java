package com.project.entity;

import lombok.Data;

@Data
public class PubGeneralDaily {

    private Integer idPubGeneralDaily;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private String remainingSum;

    private Integer state;

    private String createTime;

}
