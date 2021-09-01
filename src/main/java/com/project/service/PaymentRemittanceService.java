package com.project.service;

import com.project.entity.PaymentRemittance;
import com.project.utils.common.base.ReturnEntity;

public interface PaymentRemittanceService {

    ReturnEntity selectRemittanceByIdPamentForm(Integer idPaymentForm);

    ReturnEntity addApprovalPaymentRemittance(PaymentRemittance paymentRemittance, Integer idDaily, Integer idCardType);

    ReturnEntity addSelective(PaymentRemittance paymentRemittance);

}
