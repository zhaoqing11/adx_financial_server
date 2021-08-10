package com.project.service;

import com.project.entity.PaymentForm;
import com.project.utils.common.base.ReturnEntity;

public interface PaymentFormService {

    ReturnEntity addSelective(PaymentForm paymentForm);

    ReturnEntity updateSelective(PaymentForm paymentForm);

    ReturnEntity deleteSelective(Integer idPaymentForm);

    ReturnEntity selectByPrimaryKey(Integer idPaymentForm);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                                   PaymentForm paymentForm);

}
