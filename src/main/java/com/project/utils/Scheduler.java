package com.project.utils;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.utils.common.base.enums.CardType;
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
    private PubGeneralDailyMapper pubGeneralDailyMapper;

    @Autowired
    private SecondGeneralAccountDailyMapper secondGeneralAccountDailyMapper;

    @Autowired
    private GeneralAccountDailyMapper generalAccountDailyMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private PublicDailyMapper dailyMapper;

    @Autowired
    private PublicReportMapper reportMapper;

    @Autowired
    private PrivateReportMapper privateReportMapper;

    @Autowired
    private GeneralAccountReportMapper generalAccountReportMapper;

    @Autowired
    private SecondGeneralAccountReportMapper secondGeneralAccountReportMapper;

    @Autowired
    private PubGeneralReportMapper pubGeneralReportMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    /**
     * 每天早上八点生成上一天收支明细
     */
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void dailyReport() {
        try {
            logger.info("日报定时器执行————————————————————————————————————————————");
            String currentDate = getLastDay("yyyy-MM-dd");
            // 获取公账收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(CardType.ACCOUNT_TYPE_1, currentDate);
            List<PaymentForm> publicCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(CardType.ACCOUNT_TYPE_1, currentDate);
            // 获取私账收支明细
            List<PaymentForm> privatePayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(CardType.ACCOUNT_TYPE_2, currentDate);
            List<PaymentForm> privateCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(CardType.ACCOUNT_TYPE_2, currentDate);

            // 获取普通账户收支明细
            List<PaymentForm> generalPayFlowRecord = paymentFormMapper.queryLastDayFlowRecord(CardType.ACCOUNT_TYPE_3, currentDate);
            List<PaymentForm> generalCollectionRecord = paymentFormMapper.queryLastDayCollectionRecord(CardType.ACCOUNT_TYPE_3, currentDate);

            List<PaymentForm> generalPayFlowRecord3 = paymentFormMapper.queryLastDayFlowRecord(CardType.ACCOUNT_TYPE_5, currentDate);
            List<PaymentForm> generalCollectionRecord3 = paymentFormMapper.queryLastDayCollectionRecord(CardType.ACCOUNT_TYPE_5, currentDate);

              // Todo: 因业务需求变更，暂时停止浦发账户1151
//            List<PaymentForm> generalPayFlowRecord2 = paymentFormMapper.queryLastDayFlowRecord(CardType.ACCOUNT_TYPE_4, currentDate);
//            List<PaymentForm> generalCollectionRecord2 = paymentFormMapper.queryLastDayCollectionRecord(CardType.ACCOUNT_TYPE_4, currentDate);
//            createDaily(generalPayFlowRecord2, generalCollectionRecord2, CardType.ACCOUNT_TYPE_4);

            createDaily(publicPayFlowRecord, publicCollectionRecord, CardType.ACCOUNT_TYPE_1);
            createDaily(privatePayFlowRecord, privateCollectionRecord, CardType.ACCOUNT_TYPE_2);
            createDaily(generalPayFlowRecord, generalCollectionRecord, CardType.ACCOUNT_TYPE_3);
            createDaily(generalPayFlowRecord3, generalCollectionRecord3, CardType.ACCOUNT_TYPE_5);

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
    private void createDaily(List<PaymentForm> payFlow, List<PaymentForm> collectionFlow, int type) {
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

        switch (type) {
            case 1:
                insertPublicDaily(payTotal, serviceChargeTotal, collectionTotal);
                break;
            case 2:
                insertPrivateDaily(payTotal, serviceChargeTotal, collectionTotal);
                break;
            case 3:
                insertGeneralAccountDaily(payTotal, serviceChargeTotal, collectionTotal);
                break;
            case 4:
                insertSecondGeneralAccountDaily(payTotal, serviceChargeTotal, collectionTotal);
                break;
            case 5:
                insertPubGeneralDaily(payTotal, serviceChargeTotal, collectionTotal);
                break;
        }
    }

    /**
     * 创建（公账）账户3
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private void insertPubGeneralDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_5);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        PubGeneralDaily daily = new PubGeneralDaily();
        daily.setCollectionAmount(String.valueOf(collectionTotal));
        daily.setPayAmount(String.valueOf(payTotal));
        daily.setServiceCharge(String.valueOf(serviceChargeTotal));
        daily.setRemainingSum(configVO.getRemainingSum());
        daily.setCreateTime(getLastDay("yyyy-MM-dd"));
        pubGeneralDailyMapper.insertSelective(daily);

        // 设置上一天余额
        RemainingSumRecord record = new RemainingSumRecord();
        record.setIdCardType(CardType.ACCOUNT_TYPE_4);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);
    }

    /**
     * 创建普通账户2
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private void insertSecondGeneralAccountDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_4);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        SecondGeneralAccountDaily generalAccountDaily = new SecondGeneralAccountDaily();
        generalAccountDaily.setCollectionAmount(String.valueOf(collectionTotal));
        generalAccountDaily.setPayAmount(String.valueOf(payTotal));
        generalAccountDaily.setServiceCharge(String.valueOf(serviceChargeTotal));
        generalAccountDaily.setRemainingSum(configVO.getRemainingSum());
        generalAccountDaily.setCreateTime(getLastDay("yyyy-MM-dd"));
        secondGeneralAccountDailyMapper.insertSelective(generalAccountDaily);

        // 设置上一天余额
        RemainingSumRecord record = new RemainingSumRecord();
        record.setIdCardType(CardType.ACCOUNT_TYPE_4);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);
    }

    /**
     * 创建普通账户1
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private void insertGeneralAccountDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_3);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        GeneralAccountDaily generalAccountDaily = new GeneralAccountDaily();
        generalAccountDaily.setCollectionAmount(String.valueOf(collectionTotal));
        generalAccountDaily.setPayAmount(String.valueOf(payTotal));
        generalAccountDaily.setServiceCharge(String.valueOf(serviceChargeTotal));
        generalAccountDaily.setRemainingSum(configVO.getRemainingSum());
        generalAccountDaily.setCreateTime(getLastDay("yyyy-MM-dd"));
        generalAccountDailyMapper.insertSelective(generalAccountDaily);

        // 设置上一天余额
        RemainingSumRecord record = new RemainingSumRecord();
        record.setIdCardType(CardType.ACCOUNT_TYPE_3);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);
    }

    /**
     * 创建私账
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private void insertPrivateDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_2);
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
        record.setIdCardType(CardType.ACCOUNT_TYPE_2);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);

    }

    /**
     * 创建公账
     * @param payTotal
     * @param serviceChargeTotal
     * @param collectionTotal
     */
    private void insertPublicDaily(BigDecimal payTotal, BigDecimal serviceChargeTotal, BigDecimal collectionTotal) {
        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_1);
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
        record.setIdCardType(CardType.ACCOUNT_TYPE_1);
        record.setLastRemainingSum(configVO.getRemainingSum());
        record.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        remainingSumRecordMapper.addSelective(record);

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
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_1).collect(Collectors.toList());
            List<PaymentForm> publicIncomeFlowRecordList = incomeFlowRecordList.stream().filter(s->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_1).collect(Collectors.toList());

            // 获取私账列表
            List<PaymentForm> privatePayFlowRecord = payFlowRecordList.stream().filter(s->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_2).collect(Collectors.toList());
            List<PaymentForm> privateIncomeFlowRecordList = incomeFlowRecordList.stream().filter(s->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_2).collect(Collectors.toList());

            // 获取公账账户1
            List<PaymentForm> firstGeneralAccountPayRecord =payFlowRecordList.stream().filter(s ->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_3).collect(Collectors.toList());
            List<PaymentForm> firstGeneralAccountIncomeRecord = incomeFlowRecordList.stream().filter(s ->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_3).collect(Collectors.toList());

            // 获取公账账户2 Todo: 因业务需求变更，暂时停止浦发账户1151
//            List<PaymentForm> secondGeneralAccountPayRecord =payFlowRecordList.stream().filter(s ->
//                    s.getIdCardType() == CardType.ACCOUNT_TYPE_4).collect(Collectors.toList());
//            List<PaymentForm> secondGeneralAccountIncomeRecord = incomeFlowRecordList.stream().filter(s ->
//                    s.getIdCardType() == CardType.ACCOUNT_TYPE_4).collect(Collectors.toList());

            // 获取公账账户3
            List<PaymentForm> pubGeneralAccountPayRecord =payFlowRecordList.stream().filter(s ->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_5).collect(Collectors.toList());
            List<PaymentForm> pubGeneralAccountIncomeRecord = incomeFlowRecordList.stream().filter(s ->
                    s.getIdCardType() == CardType.ACCOUNT_TYPE_5).collect(Collectors.toList());

            createReport(publicPayFlowRecord, publicIncomeFlowRecordList, CardType.ACCOUNT_TYPE_1);
            createReport(privatePayFlowRecord, privateIncomeFlowRecordList, CardType.ACCOUNT_TYPE_2);
            createReport(firstGeneralAccountPayRecord, firstGeneralAccountIncomeRecord, CardType.ACCOUNT_TYPE_3);
//            createReport(secondGeneralAccountPayRecord, secondGeneralAccountIncomeRecord, CardType.ACCOUNT_TYPE_4);
            createReport(pubGeneralAccountPayRecord, pubGeneralAccountIncomeRecord, CardType.ACCOUNT_TYPE_5);

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

        if (type == CardType.ACCOUNT_TYPE_1) {
            insertPublicReport(collectionTotal, payTotal, serviceChargeTotal);
        } else if (type == CardType.ACCOUNT_TYPE_2) {
            insertPrivateReport(collectionTotal, payTotal, serviceChargeTotal);
        } else if (type == CardType.ACCOUNT_TYPE_3) {
            insertGeneralAccountReport(collectionTotal, payTotal, serviceChargeTotal);
        } else if (type == CardType.ACCOUNT_TYPE_4) {
            insertSecondGeneralAccountReport(collectionTotal, payTotal, serviceChargeTotal);
        } else if (type == CardType.ACCOUNT_TYPE_5) {
            insertPubGeneralAccountReport(collectionTotal, payTotal, serviceChargeTotal);
        }
    }

    /**
     * 创建月报（公账账户3）
     * @param collectionTotal
     * @param payTotal
     * @param serviceChargeTotal
     */
    private void insertPubGeneralAccountReport(BigDecimal collectionTotal, BigDecimal payTotal, BigDecimal serviceChargeTotal) {
        Date date = getCurrentDate();
        PubGeneralReport report = new PubGeneralReport();
        report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
        report.setMonth(date.getMonth() + 1);
        report.setCollectionAmount(String.valueOf(collectionTotal));
        report.setPayAmount(String.valueOf(payTotal));
        report.setServiceCharge(String.valueOf(serviceChargeTotal));
        report.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pubGeneralReportMapper.addSelective(report);
    }

    /**
     * 创建月报（公账账户2）
     * @param collectionTotal
     * @param payTotal
     * @param serviceChargeTotal
     */
    private void insertSecondGeneralAccountReport(BigDecimal collectionTotal, BigDecimal payTotal, BigDecimal serviceChargeTotal) {
        Date date = getCurrentDate();
        SecondGeneralAccountReport report = new SecondGeneralAccountReport();
        report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
        report.setMonth(date.getMonth() + 1);
        report.setCollectionAmount(String.valueOf(collectionTotal));
        report.setPayAmount(String.valueOf(payTotal));
        report.setServiceCharge(String.valueOf(serviceChargeTotal));
        report.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        secondGeneralAccountReportMapper.addSelective(report);
    }

    /**
     * 创建月报（公账账户1）
     * @param collectionTotal
     * @param payTotal
     * @param serviceChargeTotal
     */
    private void insertGeneralAccountReport(BigDecimal collectionTotal, BigDecimal payTotal, BigDecimal serviceChargeTotal) {
        Date date = getCurrentDate();
        GeneralAccountReport report = new GeneralAccountReport();
        report.setYear(Integer.parseInt(Tools.date2Str(date, "yyyy")));
        report.setMonth(date.getMonth() + 1);
        report.setCollectionAmount(String.valueOf(collectionTotal));
        report.setPayAmount(String.valueOf(payTotal));
        report.setServiceCharge(String.valueOf(serviceChargeTotal));
        report.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        generalAccountReportMapper.addSelective(report);
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
