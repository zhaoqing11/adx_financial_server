package com.project.service.impl;

import com.project.entity.*;
import com.project.mapper.master.CollectionRecordMapper;
import com.project.mapper.master.DepartmentMapper;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.PaymentRemittanceMapper;
import com.project.service.IndexService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class IndexServiceImpl implements IndexService {

    private static Logger logger = LogManager.getLogger(IndexServiceImpl.class);

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PaymentRemittanceMapper paymentRemittanceMapper;

    @Autowired
    private CollectionRecordMapper collectionRecordMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private ReturnEntity returnEntity;

    private static final Integer MONTH_NUM = 12;

    @Override
    public ReturnEntity getSecondGeneralFlowRecordDetails(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentForm> paymentFormList = paymentFormMapper.selectPayFlowRecordDetails(CardType.ACCOUNT_TYPE_4, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (Department item : departmentList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                // 筛选对应部门数据
                List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                map.put("data", filterMap(filterList));
                mapList.add(map);
            }

            // 添加其他数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("department", "其他");

            List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                    null == s.getIdDepartment()).collect(Collectors.toList());
            map.put("data", filterMap(filterList));
            mapList.add(map);

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（普通账户2）获取指定年份各部门每月支出列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getGeneralRecordDetails(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentForm> paymentFormList = paymentFormMapper.selectPayFlowRecordDetails(CardType.ACCOUNT_TYPE_3, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (Department item : departmentList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                // 筛选对应部门数据
                List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                map.put("data", filterMap(filterList));
                mapList.add(map);
            }

            // 添加其他数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("department", "其他");

            List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                    null == s.getIdDepartment()).collect(Collectors.toList());
            map.put("data", filterMap(filterList));
            mapList.add(map);

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（普通账户1）获取指定年份各部门每月支出列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getSecondGeneralRecordByDepartment(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentRemittance> paymentRemittanceList = paymentRemittanceMapper.selectPaymentRemittanceByDepartment(CardType.ACCOUNT_TYPE_4, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            departmentList.forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                List<PaymentRemittance> filterRemittance = paymentRemittanceList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                String amount = filterRemittance.size() > 0 ? filterRemittance.get(0).getAmount() : "";
                String serviceCharge = filterRemittance.size() > 0 ? filterRemittance.get(0).getServiceCharge() : "";

                BigDecimal ctAmount = Tools.isEmpty(amount) ? new BigDecimal("0.00") : new BigDecimal(amount);
                BigDecimal ctServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

                BigDecimal total = ctAmount.add(ctServiceCharge);
                map.put("num", total);

                mapList.add(map);
            });

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（普通账户2）按年份获取各部门支出数据失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getGeneralRecordByDepartment(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentRemittance> paymentRemittanceList = paymentRemittanceMapper.selectPaymentRemittanceByDepartment(CardType.ACCOUNT_TYPE_3, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            departmentList.forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                List<PaymentRemittance> filterRemittance = paymentRemittanceList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                String amount = filterRemittance.size() > 0 ? filterRemittance.get(0).getAmount() : "";
                String serviceCharge = filterRemittance.size() > 0 ? filterRemittance.get(0).getServiceCharge() : "";

                BigDecimal ctAmount = Tools.isEmpty(amount) ? new BigDecimal("0.00") : new BigDecimal(amount);
                BigDecimal ctServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

                BigDecimal total = ctAmount.add(ctServiceCharge);
                map.put("num", total);

                mapList.add(map);
            });

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（普通账户1）按年份获取各部门支出数据失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getDataInfo(Integer idCardType, int year) {
        try {
            PaymentRemittance paymentRemittance = paymentRemittanceMapper.selectRemittanceTotalByYear(idCardType, year);
            String collectionAmount = collectionRecordMapper.selectCollectionTotalByYear(idCardType, year);

            Map<String, Object> map = new HashMap<String, Object>();

            String payAmount = paymentRemittance == null ? "0.00" : paymentRemittance.getAmount();
            String serviceCharge = paymentRemittance == null ? "0.00" : paymentRemittance.getServiceCharge();
            String cAmount = collectionAmount == null ? "0.00" : collectionAmount;
            map.put("payAmount", payAmount);
            map.put("serviceCharge", serviceCharge);
            map.put("collectionAmount", cAmount);

            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("获取年收入收支总数失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity publicFlowRecordByDepartment(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentRemittance> paymentRemittanceList = paymentRemittanceMapper.selectPaymentRemittanceByDepartment(CardType.ACCOUNT_TYPE_1, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            departmentList.forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                List<PaymentRemittance> filterRemittance = paymentRemittanceList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                String amount = filterRemittance.size() > 0 ? filterRemittance.get(0).getAmount() : "";
                String serviceCharge = filterRemittance.size() > 0 ? filterRemittance.get(0).getServiceCharge() : "";

                BigDecimal ctAmount = Tools.isEmpty(amount) ? new BigDecimal("0.00") : new BigDecimal(amount);
                BigDecimal ctServiceCharge = Tools.isEmpty(serviceCharge) ? new BigDecimal("0.00") : new BigDecimal(serviceCharge);

                BigDecimal total = ctAmount.add(ctServiceCharge);
                map.put("num", total);

                mapList.add(map);
            });

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（公账）按年份获取各部门支出数据失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity privateFlowRecordByDepartment(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentRemittance> paymentRemittanceList = paymentRemittanceMapper.selectPaymentRemittanceByDepartment(CardType.ACCOUNT_TYPE_2, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            departmentList.forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                List<PaymentRemittance> filterRemittance = paymentRemittanceList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                String amount = filterRemittance.size() > 0 ? filterRemittance.get(0).getAmount() : "0.00";
                String serviceCharge = filterRemittance.size() > 0 ? filterRemittance.get(0).getServiceCharge() : "0.00";

                BigDecimal total = new BigDecimal(amount).add(new BigDecimal(serviceCharge));
                map.put("num", total);

                mapList.add(map);
            });

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（私账）按年份获取各部门支出数据失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getPublicFlowRecordDetails(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentForm> paymentFormList = paymentFormMapper.selectPayFlowRecordDetails(CardType.ACCOUNT_TYPE_1, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (Department item : departmentList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                // 筛选对应部门数据
                List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                map.put("data", filterMap(filterList));
                mapList.add(map);
            }

            // 添加其他数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("department", "其他");

            List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                    null == s.getIdDepartment()).collect(Collectors.toList());
            map.put("data", filterMap(filterList));
            mapList.add(map);

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（公账）获取指定年份各部门每月支出列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    private List<PayFlowRecordVO> filterMap(List<PaymentForm> filterList) {
        List<PayFlowRecordVO> recordVOList = new ArrayList<PayFlowRecordVO>();
        for (int i = 0; i < MONTH_NUM; i++) {
            PayFlowRecordVO recordVO = new PayFlowRecordVO();
            String month = addZaro(String.valueOf(i + 1));
            recordVO.setMonth(i + 1);

            List<PaymentForm> filterMonth = filterList.stream().filter(s->
                    formatValue(s.getRemittanceDate()).equals(month)).collect(Collectors.toList());

            BigDecimal amountTotal = new BigDecimal("0.00"); // 月总支出
            BigDecimal serviceCharegeTotal = new BigDecimal("0.00"); // 月总手续费

            for (PaymentForm paymentForm : filterMonth) {
                String amount = paymentForm.getRemittanceAmount();
                String serviceCharge = paymentForm.getServiceCharge();

                amountTotal = amountTotal.add(new BigDecimal(amount));
                serviceCharegeTotal = serviceCharegeTotal.add(new BigDecimal(serviceCharge));
            }
            recordVO.setAmount(String.valueOf(amountTotal));
            recordVO.setServiceCharge(String.valueOf(serviceCharegeTotal));

            recordVOList.add(recordVO);
        }
        return recordVOList;
    }

    private String formatValue(String value) {
        Date date = Tools.str2DateFormat(value, "yyyy-MM-dd");
        String month = Tools.date2Str(date, "MM");
        return month;
    }

    private String addZaro(String value) {
        if (value.length() < 2) {
            value = "0" + value;
        }
        return value;
    }

    @Override
    public ReturnEntity getPrivateFlowRecordDetails(int year) {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            List<PaymentForm> paymentFormList = paymentFormMapper.selectPayFlowRecordDetails(CardType.ACCOUNT_TYPE_2, year);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            departmentList.forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("department", item.getDepartmentName());

                // 筛选对应部门数据
                List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                        item.getIdDepartment() == s.getIdDepartment()).collect(Collectors.toList());
                map.put("data", filterMap(filterList));
                mapList.add(map);
            });

            // 添加其他数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("department", "其他");

            List<PaymentForm> filterList = paymentFormList.stream().filter(s ->
                    null == s.getIdDepartment()).collect(Collectors.toList());
            map.put("data", filterMap(filterList));
            mapList.add(map);

            returnEntity = ReturnUtil.success(mapList);
        } catch (Exception e) {
            logger.error("（私账）获取指定年份各部门每月支出列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getCollectionRecordByMonth(Integer idCardType, int year) {
        try {
            List<CollectionRecord> collectionRecordList = collectionRecordMapper.selectPageDetailByYear(idCardType, year);

            List<ColFlowRecordVO> recordVOList = new ArrayList<ColFlowRecordVO>();
            for(int i = 0; i < MONTH_NUM; i++) {
                ColFlowRecordVO recordVO = new ColFlowRecordVO();
                String month = addZaro(String.valueOf( i + 1));
                recordVO.setMonth(i + 1);

                // 筛选当前月份数据
                List<CollectionRecord> filterRecord = collectionRecordList.stream().filter(s ->
                        formatValue(s.getCollectionDate()).equals(month)).collect(Collectors.toList());

                BigDecimal total = new BigDecimal("0.00");
                for (CollectionRecord collectionRecord : filterRecord) {
                    String amount = collectionRecord.getAmount();
                    BigDecimal ctAmount = Tools.isEmpty(amount) ? new BigDecimal("0.00") : new BigDecimal(amount);
                    total = total.add(ctAmount);
                }
                recordVO.setAmount(String.valueOf(total));

                recordVOList.add(recordVO);
            }
            returnEntity = ReturnUtil.success(recordVOList);
        } catch (Exception e) {
            logger.error("根据指定年月或者每月收入流水明细失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
