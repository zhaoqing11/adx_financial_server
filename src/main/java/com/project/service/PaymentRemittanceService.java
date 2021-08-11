package com.project.service;

import com.project.entity.PaymentRemittance;
import com.project.utils.common.base.ReturnEntity;

public interface PaymentRemittanceService {

    ReturnEntity addSelective(PaymentRemittance paymentRemittance);

}
