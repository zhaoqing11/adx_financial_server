package com.project.service.impl;

import com.project.entity.PaymentForm;
import com.project.entity.RemainingSumRecord;
import com.project.entity.RemainingSumVO;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.RemainingSumRecordMapper;
import com.project.service.PaymentFormService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("all")
public class PaymentFormServiceImpl implements PaymentFormService {

    private static Logger logger = LogManager.getLogger(PaymentFormServiceImpl.class);

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity queryFlowRecordDetail(Integer pageNum, Integer pageSize, String startTime, String endTime) {
        try {
            List<RemainingSumVO> arrayList = new ArrayList<RemainingSumVO>();

            pageNum = pageNum == null ? 0 : pageNum;
            pageSize = pageSize == null ? 0 : pageSize;

            startTime = !Tools.isEmpty(startTime) ? Tools.date2Str(Tools.str2Date(startTime), "yyyy-MM-dd") : startTime;
            endTime = !Tools.isEmpty(endTime) ? Tools.date2Str(Tools.str2Date(endTime), "yyyy-MM-dd") : endTime;

            int payTotal = paymentFormMapper.queryPayFlowRecordDetailTotal(startTime, endTime);
            PageBean<PaymentForm> pageBean = new PageBean<PaymentForm>(pageNum, pageSize, payTotal);

            // 获取支出流水列表
            List<PaymentForm> payFlowRecord = paymentFormMapper.queryPayFlowRecordDetail(pageBean.getStartIndex(),
                    pageBean.getPageSize(), startTime,endTime);
            payFlowRecord.forEach(item -> {
                RemainingSumVO remainingSumVO = new RemainingSumVO();
                remainingSumVO.setReasonApplication(item.getReasonApplication());
                remainingSumVO.setAmount(item.getAmount());
                remainingSumVO.setPaymentName(item.getPaymentName());
                remainingSumVO.setPaymentAccount(item.getPaymentAccount());
                remainingSumVO.setCode(item.getCode());
                remainingSumVO.setApprovalAmount(item.getApprovalAmount());
                remainingSumVO.setRemittanceAmount(item.getRemittanceAmount());
                remainingSumVO.setServiceCharge(item.getServiceCharge());
                remainingSumVO.setRemainingSum(item.getRemainingSum());
                remainingSumVO.setIdFlowType(item.getIdFlowType());
                remainingSumVO.setIdPayFlowRecord(item.getIdPayFlowRecord());
                remainingSumVO.setCreateTime(item.getCreateTime());
                arrayList.add(remainingSumVO);
            });

            int incomeTotal = paymentFormMapper.queryIncomeFlowRecordDetailTotal(startTime, endTime);
            PageBean<PaymentForm> pageBean2 = new PageBean<PaymentForm>(pageNum, pageSize, incomeTotal);

            // 获取收入流水列表
            List<PaymentForm> incomeFlowRecord = paymentFormMapper.queryIncomeFlowRecordDetail(pageBean2.getStartIndex(),
                    pageBean2.getPageSize(), startTime, endTime);
            incomeFlowRecord.forEach(item -> {
                RemainingSumVO remainingSumVO = new RemainingSumVO();
                remainingSumVO.setCollectionAmount(item.getCollectionAmount());
                remainingSumVO.setRemainingSum(item.getRemainingSum());
                remainingSumVO.setIdFlowType(item.getIdFlowType());
                remainingSumVO.setIdIncomeFlowRecord(item.getIdIncomeFlowRecord());
                remainingSumVO.setCreateTime(item.getCreateTime());
                arrayList.add(remainingSumVO);
            });

            pageBean.setList(arrayList);
            pageBean.setTotalPage(payTotal + incomeTotal);
            pageBean.setTotalRecord(arrayList.size());

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("获取收支流水列表失败，错误消息：---:" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getDataInfo() {
        try {
            int approvalCount = paymentFormMapper.queryApprovalPaymentCount();
            int remittanceCount = paymentFormMapper.queryPaymentRemittanceCount();
            RemainingSumRecord sumRecord = remainingSumRecordMapper.queryTodayRemainingSum();
            String remainingSum = sumRecord != null ? sumRecord.getLastRemainingSum() : null;

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("approvalCount", approvalCount);
            map.put("remittanceCount", remittanceCount);
            map.put("remainingSum", remainingSum);

            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("获取待审批请款数失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryAllPaymentForm(Integer startIndex, Integer pageSize, PaymentForm paymentForm) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = paymentFormMapper.queryPaymentFormTotal(paymentForm);
            PageBean<PaymentForm> pageBean = new PageBean<PaymentForm>(startIndex, pageSize, total);
            List<PaymentForm> paymentFormList = paymentFormMapper.queryAllPaymentForm(pageBean.getStartIndex(),
                    pageBean.getPageSize(), paymentForm);

            pageBean.setList(paymentFormList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询待全部请款列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectApprovalPaymentFormByPage(Integer startIndex, Integer pageSize, PaymentForm paymentForm) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = paymentFormMapper.selectApprovalPaymentFormTotal(paymentForm);
            PageBean<PaymentForm> pageBean = new PageBean<PaymentForm>(startIndex, pageSize, total);
            List<PaymentForm> paymentFormList = paymentFormMapper.selectApprovalPaymentFormByPage(pageBean.getStartIndex(),
                    pageBean.getPageSize(), paymentForm);

            pageBean.setList(paymentFormList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询待审批请款列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity addSelective(PaymentForm paymentForm) {
        try {
            if (paymentForm != null) {
                String newCode = "";
                // 获取当日最大编号数
                String code = paymentFormMapper.queryMaxCode(new Date());
                if (Tools.notEmpty(code)) { // 编号不为空接上一个产生编号数+1
                    int num = Integer.parseInt(code.substring(10, 13));
                    newCode = code.substring(0, 10) + formatNum(String.valueOf(num + 1));

                } else { // 为空创建当日首个编号
                    String date = Tools.date2Str(new Date(), "yy-MM-dd");
                    String[] dataArrays = date.split("-");
                    String year = dataArrays[0];
                    String month = dataArrays[1];
                    String day = dataArrays[2];
                    newCode = "A" + year + "-" + month + "-" + day + "-" + "001";
                }
                paymentForm.setCode(newCode);
                paymentForm.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

                int count = paymentFormMapper.addSelective(paymentForm);
                if (count > 0) {
                    returnEntity = ReturnUtil.success("创建成功");
                } else {
                    returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "创建失败");
                }
            } else {
                throw new ServiceException("获取参数错误！");
            }

        } catch (Exception e) {
            logger.error("创建请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    private String formatNum(String code) {
        switch (code.length()) {
            case 1:
                code = "00" + code;
                break;
            case 2:
                code = "0" + code;
                break;
        }
        return code;
    }

    @Override
    public ReturnEntity updateSelective(PaymentForm paymentForm) {
        try {
            int count = paymentFormMapper.updateSelective(paymentForm);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idPaymentForm) {
        try {
            int count = paymentFormMapper.deleteSelective(idPaymentForm);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPrimaryKey(Integer idPaymentForm) {
        try {
            PaymentForm paymentForm = paymentFormMapper.selectByPrimaryKey(idPaymentForm);
            returnEntity = ReturnUtil.success(paymentForm);
        } catch (Exception e) {
            logger.error("根据id查询请款单信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, PaymentForm paymentForm) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            Integer idUser = paymentForm.getIdUser();
            if (idUser == null) {
                throw new ServiceException("参数错误！");
            }

            int total = paymentFormMapper.selectByPageTotal(paymentForm, idUser);
            PageBean<PaymentForm> pageBean = new PageBean<PaymentForm>(startIndex, pageSize, total);
            List<PaymentForm> paymentFormList = paymentFormMapper.selectByPage(pageBean.getStartIndex(),
                    pageBean.getPageSize(), paymentForm, idUser);

            pageBean.setList(paymentFormList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询请款单列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
