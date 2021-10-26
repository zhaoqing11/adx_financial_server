package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.service.DailyService;
import com.project.utils.DateUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.SmsUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


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
    private GeneralAccountDailyMapper generalAccountDailyMapper;

    @Autowired
    private SecondGeneralAccountDailyMapper secondGeneralAccountDailyMapper;

    @Autowired
    private PubGeneralDailyMapper pubGeneralDailyMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Value("${project.telephone}")
    private String telephone;

    @Value("${project.secondTelephone}")
    private String secondTelephone;

    @Override
    public ReturnEntity sendMessage() {
        try {
            if (publicDailyMapper.selectIsExitUnApprovalDaily() > 0) {
                return ReturnUtil.validError(HttpCode.CODE_500, "数据尚未审核通过，请审核后操作");
            }
            if (privateDailyMapper.selectIsExitUnApprovalDaily() > 0) {
                return ReturnUtil.validError(HttpCode.CODE_500, "数据尚未审核通过，请审核后操作");
            }
            if (generalAccountDailyMapper.selectIsExitUnApprovalDaily() > 0) {
                return ReturnUtil.validError(HttpCode.CODE_500, "数据尚未审核通过，请审核后操作");
            }
            if (pubGeneralDailyMapper.selectIsExitUnApprovalDaily() > 0) {
                return ReturnUtil.validError(HttpCode.CODE_500, "数据尚未审核通过，请审核后操作");
            }

            MessageVO pubMsg = selectByAccountType(CardType.ACCOUNT_TYPE_1);
            MessageVO priMsg = selectByAccountType(CardType.ACCOUNT_TYPE_2);
            MessageVO generalMsg = selectByAccountType(CardType.ACCOUNT_TYPE_3);
            MessageVO pubGeneralMsg = selectByAccountType(CardType.ACCOUNT_TYPE_5);

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pubMsg", pubMsg);
            map.put("priMsg", priMsg);
            map.put("generalMsg", generalMsg);
            map.put("pubGeneralMsg", pubGeneralMsg);

            SmsUtil.sendSms(telephone, map);
            SmsUtil.sendSms(secondTelephone, map);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return returnEntity;
    }

    private MessageVO selectByAccountType(Integer type) {
        String date = Tools.date2Str(new Date(), "yyyy-MM-dd");
        Config config = configMapper.selectConfigInfo(type);
        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

        MessageVO messageVO = new MessageVO();
        int len = configVO.getCardNum().length();
        String card = configVO.getCardNum().substring(len - 4, len);
        messageVO.setCard(card);
        messageVO.setDate(DateUtil.getLastDay("yyyy年MM月dd日"));

        switch (type) {
            case 1:
                PublicDaily pubDaily = publicDailyMapper.selectDailyByDate();

                messageVO.setCollectionAmount(pubDaily.getCollectionAmount());
                messageVO.setPayAmount(pubDaily.getPayAmount());
                messageVO.setServiceCharge(pubDaily.getServiceCharge());
                messageVO.setRemainingSum(pubDaily.getRemainingSum());
                break;
            case 2:
                PrivateDaily priDaily = privateDailyMapper.selectDailyByDate();

                messageVO.setCollectionAmount(priDaily.getCollectionAmount());
                messageVO.setPayAmount(priDaily.getPayAmount());
                messageVO.setServiceCharge(priDaily.getServiceCharge());
                messageVO.setRemainingSum(priDaily.getRemainingSum());
                break;
            case 3:
                GeneralAccountDaily generalDaily = generalAccountDailyMapper.selectDailyDate();

                messageVO.setCollectionAmount(generalDaily.getCollectionAmount());
                messageVO.setPayAmount(generalDaily.getPayAmount());
                messageVO.setServiceCharge(generalDaily.getServiceCharge());
                messageVO.setRemainingSum(generalDaily.getRemainingSum());
                break;
            case 5:
                PubGeneralDaily pubGeneralDaily = pubGeneralDailyMapper.selectDailyDate();

                messageVO.setCollectionAmount(pubGeneralDaily.getCollectionAmount());
                messageVO.setPayAmount(pubGeneralDaily.getPayAmount());
                messageVO.setServiceCharge(pubGeneralDaily.getServiceCharge());
                messageVO.setRemainingSum(pubGeneralDaily.getRemainingSum());
                break;
        }
        return messageVO;
    }

    @Override
    public ReturnEntity getDataInfo(int idRole) {
        try {
            int state = 0; // 查询状态不等于1的待审核数量
            switch (idRole) {
                case 2:
                    state = 0;
                    break;
                case 3:
                    state = 2;
                    break;
            }
            int pubNum = publicDailyMapper.selectPublicDailyByState(state);
            int priNum = privateDailyMapper.selectPrivateDailyByState(state);
            int generalNum = generalAccountDailyMapper.selectGeneralDailyUnApproval(state);
            int pubGeneralNum = pubGeneralDailyMapper.selectPubGeneralDailyUnApproval(state);

            int approvalCount = paymentFormMapper.queryApprovalPaymentCount();
            int remittanceCount = paymentFormMapper.queryPaymentRemittanceCount();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pubNum", pubNum);
            map.put("priNum", priNum);
            map.put("generalNum", generalNum);
            map.put("pubGeneralNum", pubGeneralNum);

            map.put("approvalCount", approvalCount);
            map.put("remittanceCount", remittanceCount);

            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("查询待办任务失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryPubGeneralDailyByDate(String date) {
        try {
            // 获取普通账户收支列表
            List<PaymentForm> pubPayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.ACCOUNT_TYPE_5, date);
            List<PaymentForm> pubCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.ACCOUNT_TYPE_5, date);

            List<RemainingSumVO> remainingSumVOS = formatDaily(pubPayFlowRecord, pubCollectionFlowRecord);
            returnEntity = ReturnUtil.success(remainingSumVOS);
        } catch (Exception e) {
            logger.error("根据指定日期获取账单明细列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectPubGeneralDailyByPage(Integer startIndex, Integer pageSize, PubGeneralDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = pubGeneralDailyMapper.selectByPageTotal(daily);
            PageBean<PubGeneralDaily> pageBean = new PageBean<PubGeneralDaily>(startIndex, pageSize, total);
            List<PubGeneralDaily> dailyList = pubGeneralDailyMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(), daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity querySecondGeneralDailyByDate(String date) {
        try {
            // 获取普通账户收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.ACCOUNT_TYPE_4, date);
            List<PaymentForm> publicCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.ACCOUNT_TYPE_4, date);

            List<RemainingSumVO> remainingSumVOS = formatDaily(publicPayFlowRecord, publicCollectionFlowRecord);
            returnEntity = ReturnUtil.success(remainingSumVOS);
        } catch (Exception e) {
            logger.error("根据指定日期获取账单明细列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity queryGeneralDailyByDate(String date) {
        try {
            // 获取普通账户收支列表
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.ACCOUNT_TYPE_3, date);
            List<PaymentForm> publicCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.ACCOUNT_TYPE_3, date);

            List<RemainingSumVO> remainingSumVOS = formatDaily(publicPayFlowRecord, publicCollectionFlowRecord);
            returnEntity = ReturnUtil.success(remainingSumVOS);
        } catch (Exception e) {
            logger.error("根据指定日期获取账单明细列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

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
            if (idCardType == CardType.ACCOUNT_TYPE_1) {
                count = publicDailyMapper.selectIsExitUnApprovalDaily();
            } else if (idCardType == CardType.ACCOUNT_TYPE_2) {
                count = privateDailyMapper.selectIsExitUnApprovalDaily();
            } else if (idCardType == CardType.ACCOUNT_TYPE_3) {
                count = generalAccountDailyMapper.selectIsExitUnApprovalDaily();
            } else if (idCardType == CardType.ACCOUNT_TYPE_4) {
                count = secondGeneralAccountDailyMapper.selectIsExitUnApprovalDaily();
            } else if (idCardType == CardType.ACCOUNT_TYPE_5) {
                count = pubGeneralDailyMapper.selectIsExitUnApprovalDaily();
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
            List<PaymentForm> privatePayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.ACCOUNT_TYPE_2, date);
            List<PaymentForm> privateCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.ACCOUNT_TYPE_2, date);

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
            List<PaymentForm> publicPayFlowRecord = paymentFormMapper.selectPaymentByIdCardType(CardType.ACCOUNT_TYPE_1, date);
            List<PaymentForm> publicCollectionFlowRecord = paymentFormMapper.selectCollectionByIdCardType(CardType.ACCOUNT_TYPE_1, date);

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
