package com.project.service.impl;

import com.project.entity.SecondGeneralAccountDaily;
import com.project.mapper.master.SecondGeneralAccountDailyMapper;
import com.project.service.SecondGeneralAccountDailyService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class SecondGeneralAccountDailyServiceImpl implements SecondGeneralAccountDailyService {

    private static Logger logger = LogManager.getLogger(SecondGeneralAccountDailyServiceImpl.class);

    @Autowired
    private SecondGeneralAccountDailyMapper dailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectDailyByPage(Integer startIndex, Integer pageSize, SecondGeneralAccountDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = dailyMapper.selectByPageTotal(daily);
            PageBean<SecondGeneralAccountDaily> pageBean = new PageBean<SecondGeneralAccountDaily>(startIndex, pageSize, total);
            List<SecondGeneralAccountDaily> dailyList = dailyMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(), daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
