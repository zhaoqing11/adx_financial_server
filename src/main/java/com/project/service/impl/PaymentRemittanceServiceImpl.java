package com.project.service.impl;

import com.project.entity.PaymentRemittance;
import com.project.mapper.master.PaymentRemittanceMapper;
import com.project.service.PaymentRemittanceService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class PaymentRemittanceServiceImpl implements PaymentRemittanceService {

    private static Logger logger = LogManager.getLogger(PaymentFormServiceImpl.class);


    @Autowired
    private PaymentRemittanceMapper paymentRemittanceMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(PaymentRemittance paymentRemittance) {
        try {
            paymentRemittance.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int count = paymentRemittanceMapper.addSelective(paymentRemittance);
            if (count > 0) {
                returnEntity = ReturnUtil.success("汇款成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "汇款失败");
            }
        } catch (Exception e) {
            logger.error("新增汇款清单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
