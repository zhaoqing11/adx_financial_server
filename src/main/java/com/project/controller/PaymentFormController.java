package com.project.controller;

import com.project.entity.PaymentForm;
import com.project.service.PaymentFormService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 请款单相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/paymentForm")
@SuppressWarnings("all")
public class PaymentFormController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    PaymentFormService paymentFormService;

    @ApiOperation(value = "获取收支流水列表")
    @PostMapping(value = "/queryFlowRecordDetail")
    public ReturnEntity queryFlowRecordDetail(String startTime, String endTime) {

        returnEntity = paymentFormService.queryFlowRecordDetail(startTime, endTime);
        return returnEntity;

    }

    @ApiOperation(value = "获取待审批请款数")
    @PostMapping(value = "/getDataInfo")
    public ReturnEntity getDataInfo() {

        returnEntity = paymentFormService.getDataInfo();
        return returnEntity;

    }

    @ApiOperation(value = "分页条件查询全部请款列表")
    @PostMapping(value = "/queryAllPaymentForm")
    public ReturnEntity queryAllPaymentForm(Integer pageNum, Integer pageSize, PaymentForm paymentForm) {

        returnEntity = paymentFormService.queryAllPaymentForm(pageNum, pageSize, paymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "分页条件查询审批列表")
    @PostMapping(value = "/selectApprovalPaymentFormByPage")
    public ReturnEntity selectApprovalPaymentFormByPage(Integer pageNum, Integer pageSize,
                                                        PaymentForm paymentForm) {

        returnEntity = paymentFormService.selectApprovalPaymentFormByPage(pageNum, pageSize,
                paymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "创建请款单")
    @PostMapping(value = "/addSelective")
    public ReturnEntity addSelective(PaymentForm paymentForm) {

        returnEntity = paymentFormService.addSelective(paymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "根据id修改请款单")
    @PostMapping(value = "/updateSelective")
    public ReturnEntity updateSelective(PaymentForm paymentForm) {

        returnEntity = paymentFormService.updateSelective(paymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "根据id删除请款单")
    @PostMapping(value = "/deleteSelective")
    public ReturnEntity deleteSelective(Integer idPaymentForm) {

        returnEntity = paymentFormService.deleteSelective(idPaymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "根据id查询请款单")
    @PostMapping(value = "/selectByPrimaryKey")
    public ReturnEntity selectByPrimaryKey(Integer idPaymentForm) {

        returnEntity = paymentFormService.selectByPrimaryKey(idPaymentForm);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询请款单列表")
    @PostMapping(value = "/selectByPage")
    public ReturnEntity selectByPage(Integer pageNum, Integer pageSize,
                              PaymentForm paymentForm) {

        returnEntity = paymentFormService.selectByPage(pageNum, pageSize, paymentForm);
        return returnEntity;

    }
}
