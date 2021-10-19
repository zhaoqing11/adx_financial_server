package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.service.PaymentRemittanceService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
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
    private PublicDailyMapper publicDailyMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private GeneralAccountDailyMapper generalAccountDailyMapper;

    @Autowired
    private SecondGeneralAccountDailyMapper secondGeneralAccountDailyMapper;

    @Autowired
    private PubGeneralDailyMapper pubGeneralDailyMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PayFlowRecordMapper payFlowRecordMapper;

    @Autowired
    private PaymentRemittanceMapper paymentRemittanceMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectRemittanceByIdPamentForm(Integer idPaymentForm) {
        try {
            int count = paymentRemittanceMapper.selectRemittanceByIdPamentForm(idPaymentForm);
            returnEntity = ReturnUtil.success(count);
        } catch (Exception e) {
            logger.error("根据请款id查询汇款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity addApprovalPaymentRemittance(PaymentRemittance paymentRemittance, Integer idDaily, Integer idCardType) {
        try {
            paymentRemittance.setIdCardType(idCardType);
            boolean flag = addRemittance(paymentRemittance);
            if (flag) {
                if (idCardType == CardType.ACCOUNT_TYPE_1) { // 公账
                    PublicDaily publicDaily = publicDailyMapper.selectByPrimaryKey(idDaily);

                    // 获取原本支出数据
                    BigDecimal oldPayAmount = new BigDecimal(publicDaily.getPayAmount());
                    BigDecimal oldServiceChargeAmount = new BigDecimal(publicDaily.getServiceCharge());

                    // 获取新添加支出数据
                    BigDecimal ctPayAmount = new BigDecimal(paymentRemittance.getAmount());
                    BigDecimal ctServiceChargeAmount = new BigDecimal(paymentRemittance.getServiceCharge());

                    // 计算总支出
                    BigDecimal newPayAmount = oldPayAmount.add(ctPayAmount);
                    BigDecimal newServiceChargeAmount = oldServiceChargeAmount.add(ctServiceChargeAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(publicDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.subtract(ctPayAmount).subtract(ctServiceChargeAmount);

                    publicDaily.setPayAmount(String.valueOf(newPayAmount));
                    publicDaily.setServiceCharge(String.valueOf(newServiceChargeAmount));
                    publicDaily.setRemainingSum(String.valueOf(remainingTotal));

                    publicDailyMapper.updateSelective(publicDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_2) { // 私账
                    PrivateDaily privateDaily = privateDailyMapper.selectByPrimaryKey(idDaily);

                    // 获取原本支出数据
                    BigDecimal oldPayAmount = new BigDecimal(privateDaily.getPayAmount());
                    BigDecimal oldServiceChargeAmount = new BigDecimal(privateDaily.getServiceCharge());

                    // 获取新添加支出数据
                    BigDecimal ctPayAmount = new BigDecimal(paymentRemittance.getAmount());
                    BigDecimal ctServiceChargeAmount = new BigDecimal(paymentRemittance.getServiceCharge());

                    // 计算总支出
                    BigDecimal newPayAmount = oldPayAmount.add(ctPayAmount);
                    BigDecimal newServiceChargeAmount = oldServiceChargeAmount.add(ctServiceChargeAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(privateDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.subtract(ctPayAmount).subtract(ctServiceChargeAmount);

                    privateDaily.setPayAmount(String.valueOf(newPayAmount));
                    privateDaily.setServiceCharge(String.valueOf(newServiceChargeAmount));
                    privateDaily.setRemainingSum(String.valueOf(remainingTotal));

                    privateDailyMapper.updateSelective(privateDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_3) { // 普通账户1
                    GeneralAccountDaily generalAccountDaily = generalAccountDailyMapper.selectByPrimaryKey(idDaily);

                    // 获取原本支出数据
                    BigDecimal oldPayAmount = new BigDecimal(generalAccountDaily.getPayAmount());
                    BigDecimal oldServiceChargeAmount = new BigDecimal(generalAccountDaily.getServiceCharge());

                    // 获取新添加支出数据
                    BigDecimal ctPayAmount = new BigDecimal(paymentRemittance.getAmount());
                    BigDecimal ctServiceChargeAmount = new BigDecimal(paymentRemittance.getServiceCharge());

                    // 计算总支出
                    BigDecimal newPayAmount = oldPayAmount.add(ctPayAmount);
                    BigDecimal newServiceChargeAmount = oldServiceChargeAmount.add(ctServiceChargeAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(generalAccountDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.subtract(ctPayAmount).subtract(ctServiceChargeAmount);

                    generalAccountDaily.setPayAmount(String.valueOf(newPayAmount));
                    generalAccountDaily.setServiceCharge(String.valueOf(newServiceChargeAmount));
                    generalAccountDaily.setRemainingSum(String.valueOf(remainingTotal));

                    generalAccountDailyMapper.updateSelective(generalAccountDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_4) { // 普通账户2
                    SecondGeneralAccountDaily generalAccountDaily = secondGeneralAccountDailyMapper.selectByPrimaryKey(idDaily);

                    // 获取原本支出数据
                    BigDecimal oldPayAmount = new BigDecimal(generalAccountDaily.getPayAmount());
                    BigDecimal oldServiceChargeAmount = new BigDecimal(generalAccountDaily.getServiceCharge());

                    // 获取新添加支出数据
                    BigDecimal ctPayAmount = new BigDecimal(paymentRemittance.getAmount());
                    BigDecimal ctServiceChargeAmount = new BigDecimal(paymentRemittance.getServiceCharge());

                    // 计算总支出
                    BigDecimal newPayAmount = oldPayAmount.add(ctPayAmount);
                    BigDecimal newServiceChargeAmount = oldServiceChargeAmount.add(ctServiceChargeAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(generalAccountDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.subtract(ctPayAmount).subtract(ctServiceChargeAmount);

                    generalAccountDaily.setPayAmount(String.valueOf(newPayAmount));
                    generalAccountDaily.setServiceCharge(String.valueOf(newServiceChargeAmount));
                    generalAccountDaily.setRemainingSum(String.valueOf(remainingTotal));

                    secondGeneralAccountDailyMapper.updateSelective(generalAccountDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_5) { // 普通账户3
                    PubGeneralDaily daily = pubGeneralDailyMapper.selectByPrimaryKey(idDaily);

                    // 获取原本支出数据
                    BigDecimal oldPayAmount = new BigDecimal(daily.getPayAmount());
                    BigDecimal oldServiceChargeAmount = new BigDecimal(daily.getServiceCharge());

                    // 获取新添加支出数据
                    BigDecimal ctPayAmount = new BigDecimal(paymentRemittance.getAmount());
                    BigDecimal ctServiceChargeAmount = new BigDecimal(paymentRemittance.getServiceCharge());

                    // 计算总支出
                    BigDecimal newPayAmount = oldPayAmount.add(ctPayAmount);
                    BigDecimal newServiceChargeAmount = oldServiceChargeAmount.add(ctServiceChargeAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(daily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.subtract(ctPayAmount).subtract(ctServiceChargeAmount);

                    daily.setPayAmount(String.valueOf(newPayAmount));
                    daily.setServiceCharge(String.valueOf(newServiceChargeAmount));
                    daily.setRemainingSum(String.valueOf(remainingTotal));

                    pubGeneralDailyMapper.updateSelective(daily);
                }
                returnEntity = ReturnUtil.success("汇款成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "汇款失败");
            }
        } catch (Exception e) {
            logger.error("审核创建支出流水失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity addSelective(PaymentRemittance paymentRemittance) {
        try {
            PaymentForm paymentForm = paymentFormMapper.selectByPrimaryKey(paymentRemittance.getIdPaymentForm());
            paymentRemittance.setIdCardType(paymentForm.getIdCardType());
            boolean flag = addRemittance(paymentRemittance);
            if (flag) {
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


    private boolean addRemittance(PaymentRemittance paymentRemittance) {
        paymentRemittance.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        int count = paymentRemittanceMapper.addSelective(paymentRemittance);
        if (count > 0) {
            // 创建支出流水记录
            Integer idPaymentRemittance = paymentRemittance.getIdPaymentRemittance();
//            if (paymentRemittance.getIdPaymentForm() == null) {
//                idCardType = idCardType;
//            } else {
//                PaymentForm paymentForm = paymentFormMapper.selectByPrimaryKey(paymentRemittance.getIdPaymentForm());
//                idCardType = paymentForm.getIdCardType();
//            }

            // 获取账目类型配置表
            Config config = configMapper.selectConfigInfo(paymentRemittance.getIdCardType());
            ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

            BigDecimal remainingSum = new BigDecimal(configVO.getRemainingSum()); // 余额
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
            configVO.setRemainingSum(String.valueOf(money));
            config.setConfig(JSONObject.toJSONString(configVO));

            configMapper.updateConfig(config);
            return true;
        } else {
            return false;
        }
    }

}
