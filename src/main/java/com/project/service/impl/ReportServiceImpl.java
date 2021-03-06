package com.project.service.impl;

import com.project.entity.PaymentForm;
import com.project.entity.RemainingSumRecord;
import com.project.entity.Report;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.RemainingSumRecordMapper;
import com.project.mapper.master.ReportMapper;
import com.project.service.ReportService;
import com.project.utils.DateUtil;
import com.project.utils.DecimalFormatUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import com.project.utils.excel.ExcelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class ReportServiceImpl implements ReportService {

    private static Logger logger = LogManager.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReturnEntity returnEntity;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    @Override
    public void exportToExcel(HttpServletResponse response, int year, int month) {
        try {
            Date date = Tools.str2DateFormat(year + "-" + month, "yyyy-MM");
            String fileName = Tools.date2Str(date, "yyyyMM");

            List<Map<String, Object>> mapList = getReportDetailByMonth(year, month);

            HSSFWorkbook wb = ExcelUtil.exportAnalysisData(mapList, fileName);
            ExcelUtil.downExcel(response, wb, fileName + "???????????????");
        } catch (Exception e) {
            logger.error("??????" + month + "???????????????????????????????????????--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ReturnEntity selectReportDetailByMonth(int year, int month) {
        try {
            List<Map<String, Object>> mapList = getReportDetailByMonth(year, month);
            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("??????????????????????????????????????????--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    /**
     * ??????????????????????????????
     *
     * @param year
     * @param month
     * @return
     */
    private List<Map<String, Object>> getReportDetailByMonth(int year, int month) {
        String ctDate = year + "-" + month + "-" + 1;
        int day = DateUtil.getMaxDayOfMonth(Tools.str2DateFormat(ctDate, "yyyy-MM-dd")); // ??????????????????
        String startTime = DateUtil.getFirstDayOfMonth(month); // ?????????????????????
        String endTime = DateUtil.getLastDayOfMonth(month); // ????????????????????????
        String currentDate = Tools.date2Str(Tools.str2Date(startTime), "yyyyMM");

        List<PaymentForm> payFlowList = paymentFormMapper.queryPayFlowRecordDetail(0, 0, startTime, endTime); // ??????
        List<PaymentForm> incomeFlowList = paymentFormMapper.queryIncomeFlowRecordDetail(0, 0, startTime, endTime); // ??????
        List<RemainingSumRecord> remainingSumRecordList = remainingSumRecordMapper.queryRemainingSumByMonth(currentDate); // ??????????????????

        // ??????????????????
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < day; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("day", i + 1);

            Report report = new Report();
            Date dateNum = Tools.str2DateFormat(year + "-" + month + "-" + (i + 1), "yyyy-MM-dd");
            String date = Tools.date2Str(dateNum, "yyyy-MM-dd");

            // ??????????????????????????????
            List<RemainingSumRecord> remainingSumList = remainingSumRecordList.stream().filter(s ->
                    Tools.date2Str(Tools.str2Date(s.getCreateTime()), "yyyy-MM-dd").equals(date))
                    .collect(Collectors.toList());
            String remainingSum = remainingSumList.size() > 0 ? remainingSumList.get(0).getLastRemainingSum() : "0.00";
            report.setLastRemainingSum(remainingSum);

            BigDecimal payTotal = new BigDecimal("0.00");
            BigDecimal serviceChargeTotal = new BigDecimal("0.00");
            BigDecimal collectionTotal = new BigDecimal("0.00");

            // ??????????????????????????????
            List<PaymentForm> newPayFlowList = payFlowList.stream().filter(s ->
                    Tools.date2Str(Tools.str2Date(s.getCreateTime()), "yyyy-MM-dd").equals(date))
                    .collect(Collectors.toList());
            for (PaymentForm paymentForm : newPayFlowList) {
                String payAmount = paymentForm.getRemittanceAmount();
                String serviceCharge = paymentForm.getServiceCharge();

                BigDecimal newPayAmount = Tools.isEmpty(payAmount) ? new BigDecimal("0.00") : new BigDecimal(payAmount);
                BigDecimal newServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

                payTotal = payTotal.add(newPayAmount);
                serviceChargeTotal = serviceChargeTotal.add(newServiceCharge);
            }

            // ??????????????????????????????
            List<PaymentForm> newIncomeFlowList = incomeFlowList.stream().filter(s ->
                    Tools.date2Str(Tools.str2Date(s.getCreateTime()), "yyyy-MM-dd").equals(date))
                    .collect(Collectors.toList());
            for (PaymentForm paymentForm : newIncomeFlowList) {
                String collectionAmount = paymentForm.getCollectionAmount();

                BigDecimal newCollectionAmount = Tools.isEmpty(collectionAmount) ? new BigDecimal("0.00") : new BigDecimal(collectionAmount);

                collectionTotal = collectionTotal.add(newCollectionAmount);
            }

            report.setPayAmount(String.valueOf(payTotal));
            report.setCollectionAmount(String.valueOf(collectionTotal));
            report.setServiceCharge(String.valueOf(serviceChargeTotal));
            map.put("report", report);
            mapList.add(map);
        }

        Map<String, Object> map = totalCalculation(mapList);
        mapList.add(map);

        return mapList;
    }

    /**
     * ????????????
     *
     * @param dataList
     * @return
     */
    public Map<String, Object> totalCalculation(List<Map<String, Object>> dataList) {
        BigDecimal collectionAmount = new BigDecimal("0.00");
        BigDecimal payAmount = new BigDecimal("0.00");
        BigDecimal serviceCharge = new BigDecimal("0.00");

        Map<String, Object> mp = new HashMap<String, Object>();
        for (Map<String, Object> map : dataList) {
            Report report = (Report) map.get("report");

            BigDecimal newCollectionAmount = Tools.isEmpty(report.getCollectionAmount()) ? new BigDecimal("0.00") : new BigDecimal(report.getCollectionAmount());
            BigDecimal newPayAmount = Tools.isEmpty(report.getPayAmount()) ? new BigDecimal("0.00") : new BigDecimal(report.getPayAmount());
            BigDecimal newServiceCharge = Tools.isEmpty(report.getServiceCharge()) ? new BigDecimal("0.00") : new BigDecimal(report.getServiceCharge());

            collectionAmount = collectionAmount.add(newCollectionAmount);
            payAmount = payAmount.add(newPayAmount);
            serviceCharge = serviceCharge.add(newServiceCharge);

            // ?????????0???????????????????????????????????????
            String formatLastRemainingSum = report.getLastRemainingSum().matches("^0.*$") ? report.getLastRemainingSum() : DecimalFormatUtil.formatString(new BigDecimal(report.getLastRemainingSum()), null);
            String formatPayAmount = report.getPayAmount().matches("^0.*$") ? report.getPayAmount() : DecimalFormatUtil.formatString(new BigDecimal(report.getPayAmount()), null);
            String formatCAmount = report.getCollectionAmount().matches("^0.*$") ? report.getCollectionAmount() : DecimalFormatUtil.formatString(new BigDecimal(report.getCollectionAmount()), null);
            String formatSCharge = report.getServiceCharge().matches("^0.*$") ? report.getServiceCharge() : DecimalFormatUtil.formatString(new BigDecimal(report.getServiceCharge()), null);

            report.setLastRemainingSum(formatLastRemainingSum);
            report.setPayAmount(formatPayAmount);
            report.setCollectionAmount(formatCAmount);
            report.setServiceCharge(formatSCharge);
        }

        Report report = new Report();
        report.setCollectionAmount(DecimalFormatUtil.formatString(collectionAmount, null));
        report.setPayAmount(DecimalFormatUtil.formatString(payAmount, null));
        report.setServiceCharge(DecimalFormatUtil.formatString(serviceCharge, null));

        mp.put("report", report);
        return mp;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idReport) {
        try {
            int count = reportMapper.deleteSelective(idReport);
            if (count > 0) {
                returnEntity = ReturnUtil.success("????????????");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "????????????");
            }
        } catch (Exception e) {
            logger.error("????????????????????????????????????--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, Integer currentDate) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = reportMapper.selectByPageTotal(currentDate);
            PageBean<Report> pageBean = new PageBean<Report>(startIndex, pageSize, total);
            List<Report> reportList = reportMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(),
                    currentDate);

            for (Report report : reportList) {
                String collectionAmount = report.getCollectionAmount();
                String payAmount = report.getPayAmount();
                String serviceCharge = report.getServiceCharge();

                collectionAmount = collectionAmount.matches("^0.*$") ? collectionAmount : DecimalFormatUtil.formatString(new BigDecimal(collectionAmount), null);
                payAmount = payAmount.matches("^0.*$") ? payAmount : DecimalFormatUtil.formatString(new BigDecimal(payAmount), null);
                serviceCharge = serviceCharge.matches("^0.*$") ? serviceCharge : DecimalFormatUtil.formatString(new BigDecimal(serviceCharge), null);

                report.setCollectionAmount(collectionAmount);
                report.setPayAmount(payAmount);
                report.setServiceCharge(serviceCharge);
            }

            pageBean.setList(reportList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("??????????????????????????????????????????--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
