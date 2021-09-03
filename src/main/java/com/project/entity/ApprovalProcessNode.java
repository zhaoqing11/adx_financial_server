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

    private String caseName; // 流程名

    private String caseUserName; // 此环节操作人

    private String nextCaseName; // 一下流程名

    private String nextCaseUserName; // 下一环节操作人

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
    }

    public String getNextCaseName() {
        return nextCaseName;
    }

    public void setNextCaseName(String nextCaseName) {
        this.nextCaseName = nextCaseName;
    }

    public String getNextCaseUserName() {
        return nextCaseUserName;
    }

    public void setNextCaseUserName(String nextCaseUserName) {
        this.nextCaseUserName = nextCaseUserName;
    }
}
