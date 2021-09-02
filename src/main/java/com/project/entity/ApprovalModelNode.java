package com.project.entity;

import lombok.Data;

@Data
public class ApprovalModelNode {

    private Integer idApprovalModelNode;

    private String name;

    private Integer idApprovalModel;

    private Integer idRole;

    private Integer nextIdApprovalModelNode;

    private Integer nodeIndex;

    private Integer closeRole;

}
