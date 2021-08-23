package com.project.service.impl;

import com.project.entity.ApprovalPrivateDaily;
import com.project.entity.PrivateDaily;
import com.project.mapper.master.ApprovalPrivateDailyMapper;
import com.project.mapper.master.PrivateDailyMapper;
import com.project.service.ApprovalPrivateDailyService;
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
public class ApprovalPrivateDailyServiceImpl implements ApprovalPrivateDailyService {

    private static Logger logger = LogManager.getLogger(ApprovalPrivateDailyServiceImpl.class);

    @Autowired
    private ApprovalPrivateDailyMapper approvalPrivateDailyMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

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
