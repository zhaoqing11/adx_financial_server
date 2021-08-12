package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.Config;
import com.project.entity.PayFlowRecord;
import com.project.entity.PaymentRemittance;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.PayFlowRecordMapper;
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

import java.math.BigDecimal;
import java.util.Date;

@Service
@SuppressWarnings("all")
public class PaymentRemittanceServiceImpl implements PaymentRemittanceService {

    private static Logger logger = LogManager.getLogger(PaymentFormServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PayFlowRecordMapper payFlowRecordMapper;

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
                // 创建支出流水记录
                Integer idPaymentRemittance = paymentRemittance.getIdPaymentRemittance();
                Config config = configMapper.selectConfigInfo();

                JSONObject jsonObject = JSONObject.parseObject(config.getConfig());
                String value = jsonObject.getString("remainingSum");
                String timeUnit = jsonObject.getString("timeUnit");
                boolean isCyclical = jsonObject.getBooleanValue("isCyclical");

                BigDecimal remainingSum = new BigDecimal(value); // 余额
                BigDecimal amount = new BigDecimal(paymentRemittance.getAmount()); // 汇款金额
                BigDecimal serviceCharge = new BigDecimal(paymentRemittance.getServiceCharge()); // 手续费

                BigDecimal money = remainingSum.subtract(amount).subtract(serviceCharge); // 剩余余额

                PayFlowRecord payFlowRecord = new PayFlowRecord();
                payFlowRecord.setIdPaymentRemittance(idPaymentRemittance);
                payFlowRecord.setAmount(paymentRemittance.getAmount());
                payFlowRecord.setRemainingSum(String.valueOf(money));
                payFlowRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                payFlowRecordMapper.addSelective(payFlowRecord);

                // 修改config文件余额变量
                JSONObject json = new JSONObject();
                json.put("remainingSum", String.valueOf(money));
                json.put("timeUnit", timeUnit);
                json.put("isCyclical", isCyclical);
                config.setConfig(json.toJSONString());
                configMapper.updateConfig(config);
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
