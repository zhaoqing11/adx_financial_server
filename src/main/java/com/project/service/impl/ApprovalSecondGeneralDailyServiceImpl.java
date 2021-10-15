package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.project.entity.Config;
import com.project.entity.ConfigVO;
import com.project.entity.MessageVO;
import com.project.entity.SecondGeneralAccountDaily;
import com.project.entity.ApprovalSecondGeneralAccountDaily;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.SecondGeneralAccountDailyMapper;
import com.project.mapper.master.ApprovalSecondGeneralAccountDailyMapper;
import com.project.service.ApprovalSecondGeneralAccountDailyService;
import com.project.utils.DateUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.SmsUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class ApprovalSecondGeneralDailyServiceImpl implements ApprovalSecondGeneralAccountDailyService {

    private static Logger logger = LogManager.getLogger(ApprovalSecondGeneralDailyServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Autowired
    private SecondGeneralAccountDailyMapper generalAccountDailyMapper;

    @Autowired
    private ApprovalSecondGeneralAccountDailyMapper approvalDailyMapper;

    @Override
    public ReturnEntity insertSelective(ApprovalSecondGeneralAccountDaily accountDaily, Integer idSecondGeneralAccountDaily) {
        try {
            accountDaily.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd"));
            int count = approvalDailyMapper.insertSelective(accountDaily);
            if (count > 0) {
                SecondGeneralAccountDaily daily = new SecondGeneralAccountDaily();
                daily.setState(accountDaily.getIdResultType());
                daily.setIdSecondGeneralAccountDaily(idSecondGeneralAccountDaily);
                generalAccountDailyMapper.updateSelective(daily);

                // 审批通过，发送短信通知
                if (accountDaily.getIdResultType() == 1) {
                    try {
                        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_4);
                        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);
                        SecondGeneralAccountDaily generalAccountDaily = generalAccountDailyMapper.
                                selectByPrimaryKey(accountDaily.getIdSecondGeneralAccountDaily());

                        String telephone = configVO.getTelephone();

                        MessageVO messageVO = new MessageVO();
                        int len = configVO.getCardNum().length();
                        String card = configVO.getCardNum().substring(len - 4, len);
                        messageVO.setCard(card);
                        messageVO.setCollectionAmount(generalAccountDaily.getCollectionAmount());
                        messageVO.setPayAmount(generalAccountDaily.getPayAmount());
                        messageVO.setServiceCharge(generalAccountDaily.getServiceCharge());
                        messageVO.setRemainingSum(generalAccountDaily.getRemainingSum());
                        messageVO.setDate(DateUtil.getLastDay("yyyy年MM月dd日"));

                        SmsUtil.sendSms(telephone, messageVO);
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
                returnEntity = ReturnUtil.success("审批成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "审批失败");
            }
        } catch (Exception e) {
            logger.error("（私账）账单审批失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
