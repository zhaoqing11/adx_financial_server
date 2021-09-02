package com.project.entity;

import lombok.Data;

@Data
public class Approval {

    private Integer idApproval;

    private Integer idUser;

    private Integer idApprovalCase;

    private Integer checkState;

    private boolean isDone;

    private String createTime;

    private String guid;

}
