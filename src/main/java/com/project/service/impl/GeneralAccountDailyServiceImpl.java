package com.project.service.impl;

import com.project.entity.GeneralAccountDaily;
import com.project.mapper.master.GeneralAccountDailyMapper;
import com.project.service.GeneralAccountDailyService;
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
public class GeneralAccountDailyServiceImpl implements GeneralAccountDailyService {

    private static Logger logger = LogManager.getLogger(GeneralAccountDailyServiceImpl.class);

    @Autowired
    private GeneralAccountDailyMapper dailyMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectDailyByPage(Integer startIndex, Integer pageSize, GeneralAccountDaily daily) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = dailyMapper.selectByPageTotal(daily);
            PageBean<GeneralAccountDaily> pageBean = new PageBean<GeneralAccountDaily>(startIndex, pageSize, total);
            List<GeneralAccountDaily> dailyList = dailyMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(), daily);
            pageBean.setList(dailyList);

            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
