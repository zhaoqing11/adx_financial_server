package com.project.service;

import com.project.entity.PaymentForm;
import com.project.utils.common.base.ReturnEntity;

public interface PaymentFormService {

    ReturnEntity selectByStateCount();

    ReturnEntity selectByState(Integer idUser);

    ReturnEntity queryLastDayFlowRecord(Integer idCardType);

    ReturnEntity queryFlowRecordDetail(Integer pageNum, Integer pageSize, String startTime, String endTime);

    ReturnEntity getDataInfo();

    ReturnEntity queryAllPaymentForm(Integer startIndex, Integer pageSize,
                                          PaymentForm paymentForm);

    ReturnEntity selectApprovalPaymentFormByPage(Integer startIndex, Integer pageSize,
                                                      PaymentForm paymentForm);

    ReturnEntity addSelective(PaymentForm paymentForm);

    ReturnEntity updateSelective(PaymentForm paymentForm);

    ReturnEntity deleteSelective(Integer idPaymentForm);

    ReturnEntity selectByPrimaryKey(Integer idPaymentForm);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                                   PaymentForm paymentForm);

}
