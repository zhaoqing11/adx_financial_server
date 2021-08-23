package com.project.service.impl;

import com.project.entity.ApprovalPublicDaily;
import com.project.entity.PublicDaily;
import com.project.mapper.master.ApprovalPublicDailyMapper;
import com.project.mapper.master.PublicDailyMapper;
import com.project.service.ApprovalPublicDailyService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class ApprovalPublicDailyServiceImpl implements ApprovalPublicDailyService {

    private static Logger logger = LogManager.getLogger(ApprovalPublicDailyServiceImpl.class);

    @Autowired
    private ApprovalPublicDailyMapper approvalPublicDailyMapper;

    @Autowired
    private PublicDailyMapper publicDailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

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
