package com.project.service.impl;

import com.project.entity.PaymentApproval;
import com.project.entity.PaymentForm;
import com.project.mapper.master.PaymentApprovalMapper;
import com.project.mapper.master.PaymentFormMapper;
import com.project.service.PaymentApprovalService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class PaymentApprovalServiceImpl implements PaymentApprovalService {

    private static Logger logger = LogManager.getLogger(PaymentFormServiceImpl.class);

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private PaymentApprovalMapper paymentApprovalMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(PaymentApproval paymentApproval) {
        try {
            paymentApproval.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int count = paymentApprovalMapper.addSelective(paymentApproval);
            if (count > 0) {
                PaymentForm paymentForm = new PaymentForm();
                paymentForm.setIdPaymentForm(paymentApproval.getIdPaymentForm());
                paymentForm.setIdPaymentFormState(2); // 状态置为“已审批”
                paymentFormMapper.updateSelective(paymentForm);
            }
            returnEntity = ReturnUtil.success("审批成功");
        } catch (Exception e) {
            logger.error("审批请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
