package com.project.entity;

import lombok.Data;

@Data
public class ApprovalProcessNode {

    private Integer idApprovalProcessNode;

    private Integer idApproval;

    private Integer submitIdUser;

    private Integer idApprovalCaseNodeUser;

    private String createTime;

    private Integer idCheckResult;

    private String checkCommon;

    private Integer nextIdApprovalCaseNodeUser;

    private Integer approvalIdUser;

}
