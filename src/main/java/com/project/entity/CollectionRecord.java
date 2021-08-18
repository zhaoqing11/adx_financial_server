package com.project.entity;

import lombok.Data;

@Data
public class CollectionRecord {

    private Integer idCollectionRecord;

    private String amount;

    private String collectionDate;

//    private String collectionAccount;

    private Integer idUser;

    private String createTime;

    private String userName;

    private Integer idCardType;

}
