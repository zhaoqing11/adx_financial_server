package com.project.service;

import com.project.entity.PaymentApproval;
import com.project.utils.common.base.ReturnEntity;

public interface PaymentApprovalService {

    ReturnEntity addSelective(PaymentApproval paymentApproval);

}
