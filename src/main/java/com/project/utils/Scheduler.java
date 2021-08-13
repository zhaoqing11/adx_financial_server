package com.project.utils;

import com.project.entity.PaymentForm;
import com.project.entity.Report;
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
    private ReportMapper reportMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    // 生成月报,每月最后一天零点执行
    @Scheduled(cron = "0 0 0 L * ?") //  0 */1 * * * ? （每隔一分钟执行）
    @Transactional
    public void maintenanceReport() {
        try {
            logger.info("月报定时器执行————————————————————————————————————————————");
            Date date = new Date();
            String startTime = DateUtil.getFirstDayOfMonth(date.getMonth() + 1);
            String endTime = DateUtil.getLastDayOfMonth(date.getMonth() + 1);
            List<PaymentForm> payFlowRecordList = paymentFormMapper.queryPayFlowRecordDetails(startTime, endTime); // 获取月支出
            List<PaymentForm> incomeFlowRecordList = paymentFormMapper.queryIncomeFlowRecordDetails(startTime, endTime); // 获取月收入

            BigDecimal collectionTotal = new BigDecimal("0.00");
            BigDecimal payTotal = new BigDecimal("0.00");
            BigDecimal serviceChargeTotal = new BigDecimal("0.00");

            for (PaymentForm paymentForm : payFlowRecordList) {
                String payAmount = paymentForm.getRemittanceAmount();
                String serviceCharge = paymentForm.getServiceCharge();

                BigDecimal newPayAmount = Tools.isEmpty(payAmount) ? new BigDecimal("0") : new BigDecimal(payAmount);
                BigDecimal newServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0") : new BigDecimal(serviceCharge);

                payTotal = payTotal.add(newPayAmount);
                serviceChargeTotal = serviceChargeTotal.add(newServiceCharge);
            }

            for (PaymentForm paymentForm : incomeFlowRecordList) {
                String collectionAmount = paymentForm.getCollectionAmount();
                BigDecimal amount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0") : new BigDecimal(collectionAmount);
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
