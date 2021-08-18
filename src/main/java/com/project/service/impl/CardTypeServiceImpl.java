package com.project.service.impl;

import com.project.entity.CardType;
import com.project.mapper.master.CardTypeMapper;
import com.project.service.CardTypeService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class CardTypeServiceImpl implements CardTypeService {

    private static Logger logger = LogManager.getLogger(CardTypeServiceImpl.class);

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectAll() {
        try {
            List<CardType> cardTypeList = cardTypeMapper.selectAll();
            returnEntity = ReturnUtil.success(cardTypeList);
        } catch (Exception e) {
            logger.error("获取账号类型失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
