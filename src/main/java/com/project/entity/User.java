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

    private Integer idDepartment;

    private String roleName; // 角色名称

    private String departmentName; // 所属部门

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
