package com.project.service;

import com.project.entity.Approval;
import com.project.entity.ApprovalProcessNode;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalService {

    ReturnEntity selectApprovalInfo(Integer idApproval);

    ReturnEntity verify(ApprovalProcessNode approvalProcessNode);

    ReturnEntity updateSelective(Approval approval);

    ReturnEntity selectByPrimaryKey(Integer idApproval);

    int approvalComm(Integer idDepartment, Integer userId, Integer idApproval, Integer idApprovalModel, String checkCommon);

}
