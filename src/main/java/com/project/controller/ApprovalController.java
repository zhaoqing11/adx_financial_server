package com.project.controller;

import com.project.entity.Approval;
import com.project.entity.ApprovalProcessNode;
import com.project.entity.PaymentApproval;
import com.project.service.ApprovalService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：审批相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/approval")
public class ApprovalController {

    @Autowired
    ApprovalService approvalService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "获取审批流程信息")
    @PostMapping(value = "/selectApprovalInfo")
    public ReturnEntity selectApprovalInfo(Integer idApproval) {

        returnEntity = approvalService.selectApprovalInfo(idApproval);
        return returnEntity;

    }

    @ApiOperation(value = "审批请款信息")
    @PostMapping(value = "/verify")
    public ReturnEntity verify(ApprovalProcessNode approvalProcessNode, PaymentApproval paymentApproval) { // ApprovalProcessNode approvalProcessNode

        returnEntity = approvalService.verify(approvalProcessNode, paymentApproval);
        return returnEntity;

    }

    @ApiOperation(value = "修改审批状态")
    @PostMapping(value = "/updateApproval")
    public ReturnEntity updateSelective(Approval approval) {
        returnEntity = approvalService.updateSelective(approval);
        return returnEntity;
    }

    @ApiOperation(value = "根据id查询审批记录")
    @PostMapping(value = "/selectApprovalByIdApproval")
    public ReturnEntity selectByPrimaryKey(Integer idApproval) {

        returnEntity = approvalService.selectByPrimaryKey(idApproval);
        return returnEntity;

    }

}
