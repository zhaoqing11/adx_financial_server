package com.project.utils;

import com.project.entity.PrivateDaily;
import com.project.entity.PublicDaily;
import com.project.entity.PaymentForm;
import com.project.entity.Report;
import com.project.mapper.master.PrivateDailyMapper;
import com.project.mapper.master.PublicDailyMapper;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.ReportMapper;
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

@Component
@SuppressWarnings("all")
public class Scheduler {

    private Logger logger = LogManager.getLogger(Scheduler.class);

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private PublicDailyMapper dailyMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    // 每天零点生成上日收支明细
    @Scheduled(cron = "0 */1 * * * ?") // 0 0 0 * * ?
    @Transactional
    public void dailyReport() {
        try {
            logger.info("日报定时器执行————————————————————————————————————————————");
            // 获取公账收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(1);
            List<PaymentForm> publicCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(1);
            // 获取私账收支明细
            List<PaymentForm> privatePayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(2);
            List<PaymentForm> privateCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(2);

            createPublicDaily(publicPayFlowRecord, publicCollectionRecord);
            createPrivateDaily(privatePayFlowRecord, privateCollectionRecord);

            logger.info("生成日报成功————————————————————————————————————————————");
        } catch (Exception e) {
            logger.error("生成日报失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    private void createPrivateDaily(List<PaymentForm> payFlow, List<PaymentForm> collectionFlow) {
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
            BigDecimal convertCollectionAmount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0.00") : new BigDecimal(collectionAmount);

            collectionTotal = collectionTotal.add(convertCollectionAmount);
        }

        PrivateDaily privateDaily = new PrivateDaily();
        privateDaily.setPayAmount(String.valueOf(payTotal));
        privateDaily.setServiceCharge(String.valueOf(serviceChargeTotal));
        privateDaily.setCollectionAmount(String.valueOf(collectionTotal));
        privateDaily.setCreateTime(getLastDay());
        privateDailyMapper.insertSelective(privateDaily);
    }

    private void createPublicDaily(List<PaymentForm> payFlow, List<PaymentForm> collectionFlow) {
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
            BigDecimal convertCollectionAmount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0.00") : new BigDecimal(collectionAmount);

            collectionTotal = collectionTotal.add(convertCollectionAmount);
        }

        PublicDaily daily = new PublicDaily();
        daily.setPayAmount(String.valueOf(payTotal));
        daily.setServiceCharge(String.valueOf(serviceChargeTotal));
        daily.setCollectionAmount(String.valueOf(collectionTotal));
        daily.setCreateTime(getLastDay());
        dailyMapper.insertSelective(daily);
    }

    private String getLastDay(){
        Date date = new Date();
        String year = Tools.date2Str(date, "yyyy");
        String month = Tools.date2Str(date, "MM");
        int day = Integer.parseInt(Tools.date2Str(date, "dd")) - 1;
        return year + "-" + month + "-" + day;
    }

    // 生成月报,每月最后一天零点执行
    @Scheduled(cron = "0 0 0 L * ?") //  0 */1 * * * ? （每隔一分钟执行）
    @Transactional
    public void maintenanceReport() {
        try {
            logger.info("月报定时器执行————————————————————————————————————————————");
            Date date = new Date();
            String startTime = DateUtil.getFirstDayOfMonth(date.getMonth() + 1);
            String endTime = DateUtil.getLastDayOfMonth(date.getMonth() + 1);
            List<PaymentForm> payFlowRecordList = paymentFormMapper.queryPayFlowRecordDetail(0, 0, startTime, endTime); // 获取月支出
            List<PaymentForm> incomeFlowRecordList = paymentFormMapper.queryIncomeFlowRecordDetail(0, 0, startTime, endTime); // 获取月收入

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

            Report report = new Report();
            report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
            report.setMonth(date.getMonth() + 1);
            report.setCollectionAmount(String.valueOf(collectionTotal));
            report.setPayAmount(String.valueOf(payTotal));
            report.setServiceCharge(String.valueOf(serviceChargeTotal));
            report.setCreateTime(Tools.date2Str(date, "yyyy-MM-dd HH:mm:ss"));

            reportMapper.addSelective(report);
            logger.info("生成月报成功————————————————————————————————————————————");
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException("定时生成月报错误");
        }
    }

}
