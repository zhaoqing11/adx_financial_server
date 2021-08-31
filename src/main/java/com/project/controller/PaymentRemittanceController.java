package com.project.controller;

import com.project.entity.PaymentRemittance;
import com.project.service.PaymentRemittanceService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 汇款相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/paymentRemittance")
@SuppressWarnings("all")
public class PaymentRemittanceController {

    @Autowired
    PaymentRemittanceService paymentRemittanceService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "审核创建支出流水记录")
    @PostMapping(value = "/addApprovalPaymentRemittance")
    public ReturnEntity addApprovalPaymentRemittance(PaymentRemittance paymentRemittance, Integer idDaily, Integer idCardType) {

        returnEntity = paymentRemittanceService.addApprovalPaymentRemittance(paymentRemittance, idDaily, idCardType);
        return returnEntity;

    }

    @ApiOperation(value = "新增汇款记录")
    @PostMapping(value = "/addSelective")
    public ReturnEntity addSelective(PaymentRemittance paymentRemittance) {

        returnEntity = paymentRemittanceService.addSelective(paymentRemittance);
        return returnEntity;

    }
}
