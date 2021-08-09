package com.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CollectionRecord {

    private Integer idCollectionRecord;

    private BigDecimal amount;

    private String collectionDate;

    private String collectionAccount;

    private Integer idUser;

    private String createTime;
}
