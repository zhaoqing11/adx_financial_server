package com.project.entity;

import lombok.Data;

@Data
public class PrivateDaily {

    private Integer idPrivateDaily;

    private String collectionAmount;

    private String payAmount;

    private String serviceCharge;

    private Integer state;

    private String createTime;

}
