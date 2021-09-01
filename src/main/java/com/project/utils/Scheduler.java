package com.project.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.utils.DateUtil.getLastDay;

@Component
@SuppressWarnings("all")
public class Scheduler {

    private Logger logger = LogManager.getLogger(Scheduler.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private PublicDailyMapper dailyMapper;

    @Autowired
    private PublicReportMapper reportMapper;

    @Autowired
    private PrivateReportMapper privateReportMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    private static final Integer PUBLIC_TYPE = 1; // 公账类型

    private static final Integer PRIVATE_TYPE = 2; // 私账类型

    /**
     * 每天早上八点生成上日收支明细
     */
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void dailyReport() {
        try {
            logger.info("日报定时器执行————————————————————————————————————————————");
            String currentDate = getLastDay("yyyy-MM-dd");
            // 获取公账收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(1, currentDate);
            List<PaymentForm> publicCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(1, currentDate);
            // 获取私账收支明细
            List<PaymentForm> privatePayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(2, currentDate);
            List<PaymentForm> privateCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(2, currentDate);

            MessageVO pubMsg = createDaily(publicPayFlowRecord, publicCollectionRecord, PUBLIC_TYPE);
            MessageVO priMsg = createDaily(privatePayFlowRecord, privateCollectionRecord, PRIVATE_TYPE);

            String telephone = priMsg.getTelephone();

            MessageVO messageVO = new MessageVO();
            messageVO.setCardPub(pubMsg.getCardPub());
            messageVO.setCollectionAmountPub(pubMsg.getCollectionAmountPub());
            messageVO.setPayAmountPub(pubMsg.getPayAmountPub());
            messageVO.setServiceChargePub(pubMsg.getServiceChargePub());
            messageVO.setRemainingSumPub(pubMsg.getRemainingSumPub());
            messageVO.setCardPri(priMsg.getCardPri());
            messageVO.setCollectionAmountPri(priMsg.getCollectionAmountPri());
            messageVO.setPayAmountPri(priMsg.getPayAmountPri());
            messageVO.setServiceChargePri(priMsg.getServiceChargePri());
            messageVO.setRemainingSumPri(priMsg.getRemainingSumPri());
            messageVO.setDate(getLastDay("yyyy年MM月dd日"));

            // 发送短信通知
            try {
                SmsUtil.sendSms(telephone, messageVO);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            logger.info("生成日报成功————————————————————————————————————————————");
        } catch (Exception e) {
            logger.error("生成日报失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 创建日报
     * @param payFlow
     * @param collectionFlow
     * @param type 账目类型
     */
    private MessageVO createDaily(List<PaymentForm> payFlow, List<PaymentForm> collectionFlow, int type) {
        BigDecimal payTotal = new BigDecimal("0.00");
        BigDecimal serviceChargeTotal = new BigDecimal("0.00");
        BigDecimal collectionTotal = new BigDecimal("0.00");

        for (PaymentForm paymentForm : payFlow) {
            String payAmount = paymentForm.getRemittanceAmount();
            String serviceCharge = paymentForm.getServiceCharge();
            BigDecimal convertPayAmount = Tools.isEmpty(payAmount) ? new BigDecimal("0.00") : new BigDecimal(payAmount);
            BigDecimal convertServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

            payTotal = payTotal.add(convertPayAmount);
            serviceChargeTotal = serviceChargeTotal.add(convertServiceCharge);
        }

        for (PaymentForm paymentForm : collectionFlow) {
            String collectionAmount = paymentForm.getCollectionAmount();
            BigDecimal convertCollectionAmount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0.00"): new BigDecimal(collectionAmount);

            collectionTotal = collectionTotal.add(convertCollectionAmount);
        }

        if (type == 1) {
            return insertPublicDaily(payTotal, serviceChargeTotal, collectionTotal);
        } else {
            return insertPrivateDaily(payTotal, serviceChargeTotal, collectionTotal);
        }
    }

    /**
     * 创建私账
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private MessageVO insertPrivateDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(PRIVATE_TYPE);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        PrivateDaily privateDaily = new PrivateDaily();
        privateDaily.setCollectionAmount(String.valueOf(collectionTotal));
        privateDaily.setPayAmount(String.valueOf(payTotal));
        privateDaily.setServiceCharge(String.valueOf(serviceChargeTotal));
        privateDaily.setRemainingSum(configVO.getRemainingSum());
        privateDaily.setCreateTime(getLastDay("yyyy-MM-dd"));
        privateDailyMapper.insertSelective(privateDaily);

        // 设置上一天余额
        RemainingSumRecord record = new RemainingSumRecord();
        record.setIdCardType(2);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);

        MessageVO messageVO = new MessageVO();
        int len = configVO.getCardNum().length();
        String card = configVO.getCardNum().substring(len - 4, len);
        messageVO.setCardPri(card);
        messageVO.setCollectionAmountPri(privateDaily.getCollectionAmount());
        messageVO.setPayAmountPri(privateDaily.getPayAmount());
        messageVO.setServiceChargePri(privateDaily.getServiceCharge());
        messageVO.setRemainingSumPri(privateDaily.getRemainingSum());

        messageVO.setTelephone(configVO.getTelephone());
        return messageVO;
    }

    /**
     * 创建公账
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private MessageVO insertPublicDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(PUBLIC_TYPE);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        PublicDaily daily = new PublicDaily();
        daily.setCollectionAmount(String.valueOf(collectionTotal));
        daily.setPayAmount(String.valueOf(payTotal));
        daily.setServiceCharge(String.valueOf(serviceChargeTotal));
        daily.setRemainingSum(configVO.getRemainingSum());
        daily.setCreateTime(getLastDay("yyyy-MM-dd"));
        dailyMapper.insertSelective(daily);

        // 设置上一天余额
        RemainingSumRecord record = new RemainingSumRecord();
        record.setIdCardType(1);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);

        MessageVO messageVO = new MessageVO();
        int len = configVO.getCardNum().length();
        String card = configVO.getCardNum().substring(len - 4, len);
        messageVO.setCardPub(card);
        messageVO.setCollectionAmountPub(daily.getCollectionAmount());
        messageVO.setPayAmountPub(daily.getPayAmount());
        messageVO.setServiceChargePub(daily.getServiceCharge());
        messageVO.setRemainingSumPub(daily.getRemainingSum());
        return messageVO;
    }

    /**
     * 每月一号晚上十点半执行生成上个月 月报
     */
    @Scheduled(cron = "0 30 22 1 * ?") //（每隔一分钟执行）0 */1 * * * ?     (每月最后一天晚上十点执行)0 0 22 L * ?
    @Transactional
    public void maintenanceReport() {
        try {
            logger.info("月报定时器执行————————————————————————————————————————————");
            Date date = getCurrentDate();
            // 获取上个月第一天和最后一天日期
            String startTime = DateUtil.getFirstDayOfMonth(date.getMonth() + 1);
            String endTime = DateUtil.getLastDayOfMonth(date.getMonth() + 1);
            // 获取月收支列表
            List<PaymentForm> payFlowRecordList = paymentFormMapper.queryPayFlowRecordDetail(0, 0, startTime, endTime);
            List<PaymentForm> incomeFlowRecordList = paymentFormMapper.queryIncomeFlowRecordDetail(0, 0, startTime, endTime);

            // 获取公账列表
            List<PaymentForm> publicPayFlowRecord = payFlowRecordList.stream().filter(s->
                    s.getIdCardType() == 1).collect(Collectors.toList());
            List<PaymentForm> publicIncomeFlowRecordList = incomeFlowRecordList.stream().filter(s->
                    s.getIdCardType() == 1).collect(Collectors.toList());

            // 获取私账列表
            List<PaymentForm> privatePayFlowRecord = payFlowRecordList.stream().filter(s->
                    s.getIdCardType() == 2).collect(Collectors.toList());
            List<PaymentForm> privateIncomeFlowRecordList = incomeFlowRecordList.stream().filter(s->
                    s.getIdCardType() == 2).collect(Collectors.toList());

            createReport(publicPayFlowRecord, publicIncomeFlowRecordList, PUBLIC_TYPE);
            createReport(privatePayFlowRecord, privateIncomeFlowRecordList, PRIVATE_TYPE);

            logger.info("生成月报成功————————————————————————————————————————————");
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException("定时生成月报错误");
        }
    }

    /**
     * 计算月收支明细
     * @param payFlowRecordList
     * @param incomeFlowRecordList
     * @param type
     */
    private void createReport(List<PaymentForm> payFlowRecordList, List<PaymentForm> incomeFlowRecordList, int type) {
        BigDecimal collectionTotal = new BigDecimal("0.00");
        BigDecimal payTotal = new BigDecimal("0.00");
        BigDecimal serviceChargeTotal = new BigDecimal("0.00");

        for (PaymentForm paymentForm : payFlowRecordList) {
            String payAmount = paymentForm.getRemittanceAmount();
            String serviceCharge = paymentForm.getServiceCharge();

            BigDecimal newPayAmount = Tools.isEmpty(payAmount) ? new BigDecimal("0.00") : new BigDecimal(payAmount);
            BigDecimal newServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

            payTotal = payTotal.add(newPayAmount);
            serviceChargeTotal = serviceChargeTotal.add(newServiceCharge);
        }

        for (PaymentForm paymentForm : incomeFlowRecordList) {
            String collectionAmount = paymentForm.getCollectionAmount();
            BigDecimal amount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0.00") : new BigDecimal(collectionAmount);
            collectionTotal = collectionTotal.add(amount);
        }

        if (type == 1) {
            insertPublicReport(collectionTotal, payTotal, serviceChargeTotal);
        } else {
            insertPrivateReport(collectionTotal, payTotal, serviceChargeTotal);
        }
    }

    /**
     * 创建月报（公账）
     * @param collectionTotal
     * @param payTotal
     * @param serviceChargeTotal
     */
    private void insertPublicReport(BigDecimal collectionTotal, BigDecimal payTotal, BigDecimal serviceChargeTotal) {
        Date date = getCurrentDate();
        PublicReport report = new PublicReport();
        report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
        report.setMonth(date.getMonth() + 1);
        report.setCollectionAmount(String.valueOf(collectionTotal));
        report.setPayAmount(String.valueOf(payTotal));
        report.setServiceCharge(String.valueOf(serviceChargeTotal));
        report.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        reportMapper.addSelective(report);
    }

    /**
     * 创建月报（私账）
     * @param collectionTotal
     * @param payTotal
     * @param serviceChargeTotal
     */
    private void insertPrivateReport(BigDecimal collectionTotal, BigDecimal payTotal, BigDecimal serviceChargeTotal) {
        Date date = getCurrentDate();
        PrivateReport report = new PrivateReport();
        report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
        report.setMonth(date.getMonth() + 1);
        report.setCollectionAmount(String.valueOf(collectionTotal));
        report.setPayAmount(String.valueOf(payTotal));
        report.setServiceCharge(String.valueOf(serviceChargeTotal));
        report.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        privateReportMapper.addSelective(report);
    }

    private Date getCurrentDate() {
        String lastDay = DateUtil.getLastDay("yyyy-MM-dd");
        Date date = Tools.str2DateFormat(lastDay, "yyyy-MM-dd");
        return date;
    }
}
