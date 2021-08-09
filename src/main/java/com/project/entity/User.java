package com.project.entity;

import lombok.Data;

@Data
public class User {

    private Integer idUser;

    private Integer idRole;

    private String realName;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
