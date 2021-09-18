package com.project.service.impl;

import com.project.entity.PaymentForm;
import com.project.entity.PrivateDaily;
import com.project.entity.PublicDaily;
import com.project.entity.RemainingSumVO;
import com.project.mapper.master.PaymentFormMapper;
import com.project.mapper.master.PrivateDailyMapper;
import com.project.mapper.master.PublicDailyMapper;
import com.project.service.DailyService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@SuppressWarnings("all")
public class DailyServiceImpl implements DailyService {

    private static Logger logger = LogManager.getLogger(DailyServiceImpl.class);

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private PublicDailyMapper publicDailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectDailyByState(Integer idRole) {
        try {
            int countPub = 0;
            int countPri = 0;
            int approvalTotalPri = 0;
            int approvalTotalPub = 0;
            switch (idRole) {
                case 2: // 审批人
                    countPri = privateDailyMapper.selectPrivateDailyByState(0);
                    countPub = publicDailyMapper.selectPublicDailyByState(0);

                    approvalTotalPri = privateDailyMapper.selectPrivateDailyByState(1); // 已审核数量 私账
                    approvalTotalPub = publicDailyMapper.selectPublicDailyByState(1); // 已审核数量 公账
                    break;
                case 3: // 汇款人
                    countPri = privateDailyMapper.selectPrivateDailyByState(2);
                    countPub = publicDailyMapper.selectPublicDailyByState(2);
                    break;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("countPub", countPub);
            map.put("countPri", countPri);
            map.put("approvalTotalPri", approvalTotalPri);
            map.put("approvalTotalPub", approvalTotalPub);

            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectIsExitUnApprovalDaily(Integer idCardType) {
        try {
            int count = 0;
            if (idCardType == CardType.PUBLICTYPE) {
                count = publicDailyMapper.selectIsExitUnApprovalDaily();
            } else {
                count = privateDailyMapper.selectIsExitUnApprovalDaily();
            }
            returnEntity = ReturnUtil.success(count);
        } catch (Exception e) {
            logger.error("查询上一天账单核对信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryPrivateDailyByDate(String date) {
        try {
            // 获取私账收支列表
            List<PaymentForm> privatePayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.PRIVATETYPE, date);
            List<PaymentForm> privateCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.PRIVATETYPE, date);

            List<RemainingSumVO> remainingSumVOS = formatDaily(privatePayFlowRecord, privateCollectionFlowRecord);
            returnEntity = ReturnUtil.success(remainingSumVOS);
        } catch (Exception e) {
            logger.error("根据指定日期获取(私账)账单明细列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryPublicDailyByDate(String date) {
        try {
            // 获取公账收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.PUBLICTYPE, date);
            List<PaymentForm> publicCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.PUBLICTYPE, date);

            List<RemainingSumVO> remainingSumVOS = formatDaily(publicPayFlowRecord, publicCollectionFlowRecord);
            returnEntity = ReturnUtil.success(remainingSumVOS);
        } catch (Exception e) {
            logger.error("根据指定日期获取(公账)账单明细列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    private List<RemainingSumVO> formatDaily(List<PaymentForm> payFlowRecord, List<PaymentForm> collectionFlowRcord) {
        List<RemainingSumVO> remainingSumVOS = new ArrayList<RemainingSumVO>();
        for (PaymentForm item : payFlowRecord) {
            RemainingSumVO remainingSumVO = new RemainingSumVO();
            remainingSumVO.setCode(item.getCode());
            remainingSumVO.setReasonApplication(item.getReasonApplication());
            remainingSumVO.setAmount(item.getAmount());
            remainingSumVO.setPaymentName(item.getPaymentName());
            remainingSumVO.setPaymentAccount(item.getPaymentAccount());
            remainingSumVO.setApprovalAmount(item.getApprovalAmount());
            remainingSumVO.setRemittanceAmount(item.getRemittanceAmount());
            remainingSumVO.setServiceCharge(item.getServiceCharge());
            remainingSumVO.setRemainingSum(item.getRemainingSum());
            remainingSumVO.setCreateTime(item.getCreateTime());
            remainingSumVO.setIdPayFlowRecord(item.getIdPayFlowRecord());
            remainingSumVO.setIdPaymentRemittance(item.getIdPaymentRemittance());
            remainingSumVO.setIdPaymentForm(item.getIdPaymentForm());
            remainingSumVO.setRemark(item.getRemark());
            remainingSumVOS.add(remainingSumVO);
        }

        for (PaymentForm item : collectionFlowRcord) {
            RemainingSumVO remainingSumVO = new RemainingSumVO();
            remainingSumVO.setCollectionAmount(item.getCollectionAmount());
            remainingSumVO.setRemainingSum(item.getRemainingSum());
            remainingSumVO.setCreateTime(item.getCreateTime());
            remainingSumVO.setIdIncomeFlowRecord(item.getIdIncomeFlowRecord());
            remainingSumVO.setIdCollectionRecord(item.getIdCollectionRecord());
            remainingSumVO.setRemark(item.getRemark());
            remainingSumVOS.add(remainingSumVO);
        }
        return remainingSumVOS;
    }

    @Override
    public ReturnEntity updatePublicDaily(PublicDaily daily) {
        try {
            int count = publicDailyMapper.updateSelective(daily);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改（公账）日账单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updatePrivateDaily(PrivateDaily daily) {
        try {
            int count = privateDailyMapper.updateSelective(daily);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改（私账）日账单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity insertPublicDaily(PublicDaily daily) {
        try {
            int count = publicDailyMapper.insertSelective(daily);
            if (count > 0) {
                returnEntity = ReturnUtil.success("新增成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "新增失败");
            }
        } catch (Exception e) {
            logger.error("新增日账单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectPublicDailyByPage(Integer startIndex, Integer pageSize, PublicDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = publicDailyMapper.selectByPageTotal(daily);
            PageBean<PublicDaily> pageBean = new PageBean<PublicDaily>(startIndex, pageSize, total);
            List<PublicDaily> dailyList = publicDailyMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(), daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectPrivateDailyByPage(Integer startIndex, Integer pageSize, PrivateDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = privateDailyMapper.selectByPageTotal(daily);
            PageBean<PublicDaily> pageBean = new PageBean<PublicDaily>(startIndex, pageSize, total);
            List<PrivateDaily> dailyList = privateDailyMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(), daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
