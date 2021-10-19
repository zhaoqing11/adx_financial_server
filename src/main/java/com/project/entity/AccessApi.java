package com.project.entity;

import lombok.Data;

@Data
public class AccessApi {

    private Integer idAccessApi;

    private String name;

    private String displayName;

    private Integer level;

    private Integer parentId;

    private String routerPath;

    private Integer seque;

    private String icon;

}
