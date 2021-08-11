package com.project.controller;

import com.project.entity.PaymentApproval;
import com.project.service.PaymentApprovalService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 审批请款相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/approvalPayment")
@SuppressWarnings("all")
public class PaymentApprovalController {

    @Autowired
    PaymentApprovalService paymentApprovalService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "创建审批请款记录")
    @PostMapping(value = "/addSelective")
    public ReturnEntity addSelective(PaymentApproval paymentApproval) {

        returnEntity = paymentApprovalService.addSelective(paymentApproval);
        return returnEntity;

    }
}
