package com.project.entity;

import lombok.Data;

@Data
public class Accessory {

    private Integer idAccessory;

    private Integer idUser;

    private String fileName;

    private String ext;

    private Integer size;

    private String guid;

    private boolean del;

    private String filePath;

    private String createTime;

}
