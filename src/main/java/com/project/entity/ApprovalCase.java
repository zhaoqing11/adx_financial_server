package com.project.entity;

import lombok.Data;

@Data
public class ApprovalCase {

    private Integer idApprovalCase;

    private Integer idApprovalModel;

    private Integer idDepartment;

    private String name;

    private boolean active;

}
