package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.project.entity.*;
import com.project.mapper.master.ApprovalPublicDailyMapper;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.PublicDailyMapper;
import com.project.service.ApprovalPublicDailyService;
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
public class ApprovalPublicDailyServiceImpl implements ApprovalPublicDailyService {

    private static Logger logger = LogManager.getLogger(ApprovalPublicDailyServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ApprovalPublicDailyMapper approvalPublicDailyMapper;

    @Autowired
    private PublicDailyMapper publicDailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Value("${project.telephone}")
    private String telephone;

    @Value("${project.secondTelephone}")
    private String secondTelephone;

    @Override
    public ReturnEntity insertSelective(ApprovalPublicDaily approvalPublicDaily, Integer idPublicDaily) {
        try {
            approvalPublicDaily.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd"));
            int count = approvalPublicDailyMapper.insertSelective(approvalPublicDaily);
            if (count > 0) {
                PublicDaily publicDaily = new PublicDaily();
                publicDaily.setState(approvalPublicDaily.getIdResultType());
                publicDaily.setIdPublicDaily(idPublicDaily);
                publicDailyMapper.updateSelective(publicDaily);

                // 审批通过，发送短信通知
                if (approvalPublicDaily.getIdResultType() == 1) {
                    try {
                        Config config = configMapper.selectConfigInfo(CardType.ACCOUNT_TYPE_1);
                        ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);
                        PublicDaily pubDaily = publicDailyMapper.selectByPrimaryKey(approvalPublicDaily.getIdPublicDaily());

                        String telephone = configVO.getTelephone();

                        MessageVO messageVO = new MessageVO();
                        int len = configVO.getCardNum().length();
                        String card = configVO.getCardNum().substring(len - 4, len);
                        messageVO.setCard(card);
                        messageVO.setCollectionAmount(pubDaily.getCollectionAmount());
                        messageVO.setPayAmount(pubDaily.getPayAmount());
                        messageVO.setServiceCharge(pubDaily.getServiceCharge());
                        messageVO.setRemainingSum(pubDaily.getRemainingSum());
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
            logger.error("新增（公账）日报审批记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
