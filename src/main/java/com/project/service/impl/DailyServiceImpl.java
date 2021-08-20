package com.project.service.impl;

import com.project.entity.PublicDaily;
import com.project.mapper.master.PublicDailyMapper;
import com.project.service.DailyService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@SuppressWarnings("all")
public class DailyServiceImpl implements DailyService {

    private static Logger logger = LogManager.getLogger(DailyServiceImpl.class);

    @Autowired
    private PublicDailyMapper dailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity updateSelective(PublicDaily daily) {
        try {
            int count = dailyMapper.updateSelective(daily);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改日账单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity insertSelective(PublicDaily daily) {
        try {
            int count = dailyMapper.insertSelective(daily);
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
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, PublicDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = dailyMapper.selectByPageTotal(daily);
            PageBean<PublicDaily> pageBean = new PageBean<PublicDaily>(startIndex, pageSize, total);
            List<PublicDaily> dailyList = dailyMapper.selectByPage(startIndex, pageSize, daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
