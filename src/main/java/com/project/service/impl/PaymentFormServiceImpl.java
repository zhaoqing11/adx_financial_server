package com.project.service.impl;

import com.project.entity.PaymentForm;
import com.project.entity.RemainingSumRecord;
import com.project.entity.RemainingSumVO;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.PaymentRemittanceMapper;
import com.project.mapper.master.RemainingSumRecordMapper;
import com.project.mapper.master.UserMapper;
import com.project.service.ApprovalService;
import com.project.service.PaymentFormService;
import com.project.utils.DateUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
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
    private UserMapper userMapper;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private PaymentRemittanceMapper paymentRemittanceMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectByStateCount() {
        try {
            int approvaled = paymentFormMapper.selectByStateCount(3);
            int unApproval = paymentFormMapper.selectByStateCount(2);
            int unRemittance = paymentFormMapper.queryPaymentRemittanceCount();
            int remittanceCount = paymentRemittanceMapper.selectRemittanceCount();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("approvaled", approvaled);
            map.put("unApproval", unApproval);
            map.put("unRemittance", unRemittance);
            map.put("remittanceCount", remittanceCount);
            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("获取审批数据失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByState(Integer idUser) {
        try {
            PaymentForm paymentForm = paymentFormMapper.selectByState(idUser, 1);
            returnEntity = ReturnUtil.success(paymentForm);
        } catch (Exception e) {
            logger.error("根据用户id获取草稿项目失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryLastDayFlowRecord(Integer idCardType) {
        try {
            String date = DateUtil.getLastDay("yyyy-MM-dd");
            List<PaymentForm> flowRecordList = paymentFormMapper.queryLastDayFlowRecord(idCardType, date);
            List<PaymentForm> collectionRcordList = paymentFormMapper.queryLastDayCollectionRecord(idCardType, date);

            List<RemainingSumVO> remainingSumVOS = new ArrayList<RemainingSumVO>();
            for (PaymentForm paymentForm : flowRecordList) {
                RemainingSumVO remainingSumVO = new RemainingSumVO();
                remainingSumVO.setCode(paymentForm.getCode());
                remainingSumVO.setReasonApplication(paymentForm.getReasonApplication());
                remainingSumVO.setAmount(paymentForm.getAmount());
                remainingSumVO.setPaymentName(paymentForm.getPaymentName());
                remainingSumVO.setPaymentAccount(paymentForm.getPaymentAccount());
                remainingSumVO.setApprovalAmount(paymentForm.getApprovalAmount());
                remainingSumVO.setRemittanceAmount(paymentForm.getRemittanceAmount());
                remainingSumVO.setServiceCharge(paymentForm.getServiceCharge());
                remainingSumVO.setRemittanceDate(paymentForm.getRemittanceDate());
                remainingSumVOS.add(remainingSumVO);
            }

            for (PaymentForm paymentForm : collectionRcordList) {
                RemainingSumVO remainingSumVO = new RemainingSumVO();
                remainingSumVO.setCode(paymentForm.getCode());
                remainingSumVO.setReasonApplication(paymentForm.getReasonApplication());
                remainingSumVO.setAmount(paymentForm.getAmount());
                remainingSumVO.setPaymentName(paymentForm.getPaymentName());
                remainingSumVO.setPaymentAccount(paymentForm.getPaymentAccount());
                remainingSumVO.setApprovalAmount(paymentForm.getApprovalAmount());
                remainingSumVO.setRemittanceAmount(paymentForm.getRemittanceAmount());
                remainingSumVO.setServiceCharge(paymentForm.getServiceCharge());
                remainingSumVO.setRemittanceDate(paymentForm.getRemittanceDate());

                remainingSumVO.setCollectionAmount(paymentForm.getCollectionAmount());
                remainingSumVO.setCollectionDate(paymentForm.getCollectionDate());
                remainingSumVOS.add(remainingSumVO);
            }
            Map<String, Object> map = new HashMap<String, Object>();
        } catch (Exception e) {
            logger.error("获取上日收支明细失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryFlowRecordDetail(Integer pageNum, Integer pageSize, String startTime, String endTime) {
        try {
            List<RemainingSumVO> remainingSumVOList = new ArrayList<RemainingSumVO>();

            pageNum = pageNum == null ? 0 : pageNum;
            pageSize = pageSize == null ? 0 : pageSize;
            final int currentPage = pageSize;

            startTime = !Tools.isEmpty(startTime) ? Tools.date2Str(Tools.str2Date(startTime), "yyyy-MM-dd") : startTime;
            endTime = !Tools.isEmpty(endTime) ? Tools.date2Str(Tools.str2Date(endTime), "yyyy-MM-dd") : endTime;

            // 获取支出流水列表
            List<PaymentForm> payFlowRecord = paymentFormMapper.queryPayFlowRecordDetail(0, 0, startTime, endTime);
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
                remainingSumVO.setIdCardType(item.getIdCardType());
                remainingSumVO.setIdConfig(item.getIdConfig());
                remainingSumVO.setRemark(item.getRemark());
                remainingSumVO.setCreateTime(item.getCreateTime());
                remainingSumVOList.add(remainingSumVO);
            });

            // 获取收入流水列表
            List<PaymentForm> incomeFlowRecord = paymentFormMapper.queryIncomeFlowRecordDetail(0, 0, startTime, endTime);
            incomeFlowRecord.forEach(item -> {
                RemainingSumVO remainingSumVO = new RemainingSumVO();
                remainingSumVO.setCollectionAmount(item.getCollectionAmount());
                remainingSumVO.setRemainingSum(item.getRemainingSum());
                remainingSumVO.setIdFlowType(item.getIdFlowType());
                remainingSumVO.setIdIncomeFlowRecord(item.getIdIncomeFlowRecord());
                remainingSumVO.setIdCardType(item.getIdCardType());
                remainingSumVO.setIdConfig(item.getIdConfig());
                remainingSumVO.setRemark(item.getRemark());
                remainingSumVO.setCreateTime(item.getCreateTime());
                remainingSumVOList.add(remainingSumVO);
            });

            List<RemainingSumVO> publicRemainSumList = new ArrayList<RemainingSumVO>();
            List<RemainingSumVO> privateRemainSumList = new ArrayList<RemainingSumVO>();
            List<RemainingSumVO> firstGeneralAccount = new ArrayList<RemainingSumVO>();
            List<RemainingSumVO> secondGeneralAccount = new ArrayList<RemainingSumVO>();
            List<RemainingSumVO> thirdGeneralAccount = new ArrayList<RemainingSumVO>();

            // 过滤公账私账
            for (RemainingSumVO remainingSumVO : remainingSumVOList) {
                Integer idConfig = remainingSumVO.getIdConfig();
                if (idConfig == CardType.ACCOUNT_TYPE_1) {
                    publicRemainSumList.add(remainingSumVO);
                } else if (idConfig == CardType.ACCOUNT_TYPE_2) {
                    privateRemainSumList.add(remainingSumVO);
                } else if (idConfig == CardType.ACCOUNT_TYPE_3) {
                    firstGeneralAccount.add(remainingSumVO);
                } else if (idConfig == CardType.ACCOUNT_TYPE_4) {
                    secondGeneralAccount.add(remainingSumVO);
                } else if (idConfig == CardType.ACCOUNT_TYPE_5) {
                    thirdGeneralAccount.add(remainingSumVO);
                }
            }

            Collections.sort(publicRemainSumList, Comparator.comparing(RemainingSumVO::getCreateTime).reversed()); //这里使用了JDK8的方法传递特性
            Collections.sort(privateRemainSumList, Comparator.comparing(RemainingSumVO::getCreateTime).reversed());
            Collections.sort(firstGeneralAccount, Comparator.comparing(RemainingSumVO::getCreateTime).reversed());
            Collections.sort(secondGeneralAccount, Comparator.comparing(RemainingSumVO::getCreateTime).reversed());
            Collections.sort(thirdGeneralAccount, Comparator.comparing(RemainingSumVO::getCreateTime).reversed());


            Map<String, Object> publicMp = new HashMap<String, Object>();
            publicMp.put("datas", publicRemainSumList);
            publicMp.put("totalPage", publicRemainSumList.size());

            Map<String, Object> privateMp = new HashMap<String, Object>();
            privateMp.put("datas", privateRemainSumList);
            privateMp.put("totalPage", privateRemainSumList.size());

            Map<String, Object> generalAccountMp = new HashMap<String, Object>();
            generalAccountMp.put("datas", firstGeneralAccount);
            generalAccountMp.put("totalPage", firstGeneralAccount.size());

            Map<String, Object> secondGeneralAccountMp = new HashMap<String, Object>();
            secondGeneralAccountMp.put("datas", secondGeneralAccount);
            secondGeneralAccountMp.put("totalPage", secondGeneralAccount.size());

            Map<String, Object> thirdGeneralAccountMp = new HashMap<String, Object>();
            thirdGeneralAccountMp.put("datas", thirdGeneralAccount);
            thirdGeneralAccountMp.put("totalPage", thirdGeneralAccount.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("publicRemainSumList", publicMp);
            map.put("privateRemainSumList", privateMp);
            map.put("generalAccountMpOne", generalAccountMp);
            map.put("generalAccountMpTwo", secondGeneralAccountMp);
            map.put("thirdGeneralAccountMp", thirdGeneralAccountMp);
            returnEntity = ReturnUtil.success(map);
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
            RemainingSumRecord publicSumRecord = remainingSumRecordMapper.queryTodayRemainingSum(CardType.ACCOUNT_TYPE_1);
            RemainingSumRecord privateSumRecord = remainingSumRecordMapper.queryTodayRemainingSum(CardType.ACCOUNT_TYPE_2);

            String publicRemainingSum = publicSumRecord != null ? publicSumRecord.getLastRemainingSum() : null;
            String privateRemainingSum = privateSumRecord != null ? privateSumRecord.getLastRemainingSum() : null;

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("approvalCount", approvalCount);
            map.put("remittanceCount", remittanceCount);
            map.put("publicRemainingSum", publicRemainingSum);
            map.put("privateRemainingSum", privateRemainingSum);

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
            Integer idApproval = paymentForm.getIdApproval();
            Integer idUser = paymentForm.getIdUser();
            Integer idDepartment = userMapper.selectByPrimaryKey(idUser).getIdDepartment();

            if (paymentForm.getIdPaymentForm() != null) {
                if (paymentForm.getState() == 2) {
                    idApproval = approvalService.approvalComm(idDepartment, idUser, idApproval, 1, "提交付款申请");
                    paymentForm.setIdApproval(idApproval);
                }
//                paymentForm.setState(2); // 待审核
                paymentFormMapper.updateSelective(paymentForm);
                returnEntity = ReturnUtil.success("编辑成功");
            } else {
                String newCode = getTodayMaxCode();
                paymentForm.setCode(newCode);
                paymentForm.setCreateTime(Tools.date2Str(new Date()));
                int count = paymentFormMapper.addSelective(paymentForm);
                if (count > 0) {
                    if (paymentForm.getState() == 2) {
                        idApproval = approvalService.approvalComm(idDepartment, idUser, idApproval, 1, "提交付款申请");
                        paymentForm.setIdApproval(idApproval);
                        paymentFormMapper.updateSelective(paymentForm);
                    }
                    returnEntity = ReturnUtil.success("创建成功");
                } else {
                    returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "创建失败");
                }
            }
        } catch (Exception e) {
            logger.error("创建请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    /**
     * 获取当日最大编号数
     *
     * @return
     */
    private String getTodayMaxCode() {
        String newCode = "";
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
        return newCode;
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
