package com.project.controller;

import com.project.entity.Approval;
import com.project.entity.ApprovalProcessNode;
import com.project.service.ApprovalService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "")
@RestController
@Validated
@RequestMapping(value = "/approval")
public class ApprovalController {

    @Autowired
    ApprovalService approvalService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "")
    @PostMapping(value = "/verify")
    public ReturnEntity verify(ApprovalProcessNode approvalProcessNode) {

        returnEntity = approvalService.verify(approvalProcessNode);
        return returnEntity;

    }

    @ApiOperation(value = "")
    @PostMapping(value = "/updateApproval")
    public ReturnEntity updateSelective(Approval approval) {
        returnEntity = approvalService.updateSelective(approval);
        return returnEntity;
    }

    @ApiOperation(value = "")
    @PostMapping(value = "/selectApprovalBtIdApproval")
    public ReturnEntity selectByPrimaryKey(Integer idApproval) {

        returnEntity = approvalService.selectByPrimaryKey(idApproval);
        return returnEntity;

    }

}
