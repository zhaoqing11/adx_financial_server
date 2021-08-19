package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.CollectionRecordMapper;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.IncomeFlowRecordMapper;
import com.project.service.CollectionRecordService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class CollectionRecordServiceImpl implements CollectionRecordService {

    static private Logger logger = LogManager.getLogger(CollectionRecordServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private IncomeFlowRecordMapper incomeFlowRecordMapper;

    @Autowired
    private CollectionRecordMapper collectionRecordMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(CollectionRecord collectionRecord) {
        try {
            collectionRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int count = collectionRecordMapper.addSelective(collectionRecord);
            if (count > 0) {
                // 创建支出流水记录
                Integer idCollectionRecord = collectionRecord.getIdCollectionRecord();
                Config config = configMapper.selectConfigInfo(collectionRecord.getIdCardType());
                ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

                BigDecimal remainingSum = new BigDecimal(configVO.getRemainingSum()); // 余额
                BigDecimal amount = new BigDecimal(collectionRecord.getAmount()); // 收款金额

                BigDecimal money = remainingSum.add(amount); // 剩余余额

                IncomeFlowRecord flowRecord = new IncomeFlowRecord();
                flowRecord.setIdCollectionRecord(idCollectionRecord);
                flowRecord.setAmount(collectionRecord.getAmount());
                flowRecord.setRemainingSum(String.valueOf(money));
                flowRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                incomeFlowRecordMapper.addSelective(flowRecord);

                // 修改config文件余额变量
                configVO.setRemainingSum(String.valueOf(money));
                config.setConfig(JSONObject.toJSONString(configVO));

                configMapper.updateConfig(config);

                returnEntity = ReturnUtil.success("新增成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "新增失败");
            }
        } catch (Exception e) {
            logger.error("新增收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateSelective(CollectionRecord collectionRecord) {
        try {
            int count = collectionRecordMapper.updateSelective(collectionRecord);
            if (count > 0) {
                returnEntity = ReturnUtil.success("编辑成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "编辑失败");
            }
        } catch (Exception e) {
            logger.error("修改收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idCollectionRecord) {
        try {
            int count = collectionRecordMapper.deleteSelective(idCollectionRecord);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, CollectionRecord collectionRecord) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            String startTime = collectionRecord.getStartTime();
            String endTime = collectionRecord.getEndTime();

            startTime = Tools.notEmpty(startTime) ? Tools.date2Str(Tools.str2Date(startTime), "yyyy-MM-dd") : startTime;
            endTime = Tools.notEmpty(endTime) ? Tools.date2Str(Tools.str2Date(endTime), "yyyy-MM-dd") : endTime;

            int total = collectionRecordMapper.selectByPageTotal(startTime, endTime, collectionRecord.getIdCardType());
            PageBean<CollectionRecord> pageBean = new PageBean<CollectionRecord>(startIndex, pageSize, total);
            List<CollectionRecord> collectionRecordList = collectionRecordMapper.selectByPage(pageBean.getStartIndex(),
                    pageBean.getPageSize(),collectionRecord);

            pageBean.setList(collectionRecordList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询收款列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
