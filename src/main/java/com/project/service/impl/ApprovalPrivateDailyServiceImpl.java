package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.project.entity.*;
import com.project.mapper.master.ApprovalPrivateDailyMapper;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.PrivateDailyMapper;
import com.project.service.ApprovalPrivateDailyService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class ApprovalPrivateDailyServiceImpl implements ApprovalPrivateDailyService {

    private static Logger logger = LogManager.getLogger(ApprovalPrivateDailyServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ApprovalPrivateDailyMapper approvalPrivateDailyMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Value("${project.telephone}")
    private String telephone;

    @Value("${project.secondTelephone}")
    private String secondTelephone;

    @Override
    public ReturnEntity insertSelective(ApprovalPrivateDaily approvalPrivateDaily, Integer idPrivateDaily) {
        try {
            approvalPrivateDaily.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd"));
            int count = approvalPrivateDailyMapper.insertSelective(approvalPrivateDaily);
            if (count > 0) {
                PrivateDaily privateDaily = new PrivateDaily();
                privateDaily.setState(approvalPrivateDaily.getIdResultType());
                privateDaily.setIdPrivateDaily(idPrivateDaily);
                privateDailyMapper.updateSelective(privateDaily);

                // 审批通过，发送短信通知
                if (approvalPrivateDaily.getIdResultType() == 1) {
                    try {
                        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_2);
                        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);
                        PrivateDaily priDaily = privateDailyMapper.selectByPrimaryKey(approvalPrivateDaily.getIdPrivateDaily());

                        String telephone = configVO.getTelephone();

                        MessageVO messageVO = new MessageVO();
                        int len = configVO.getCardNum().length();
                        String card = configVO.getCardNum().substring(len - 4, len);
                        messageVO.setCard(card);
                        messageVO.setCollectionAmount(priDaily.getCollectionAmount());
                        messageVO.setPayAmount(priDaily.getPayAmount());
                        messageVO.setServiceCharge(priDaily.getServiceCharge());
                        messageVO.setRemainingSum(priDaily.getRemainingSum());
                        messageVO.setDate(DateUtil.getLastDay("yyyy年MM月dd日"));

                        SmsUtil.sendSms(telephone, messageVO);
                        SmsUtil.sendSms(secondTelephone, messageVO);
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
                returnEntity = ReturnUtil.success("审批成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "审批失败");
            }
        } catch (Exception e) {
            logger.error("新增（私账）日报审批记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
