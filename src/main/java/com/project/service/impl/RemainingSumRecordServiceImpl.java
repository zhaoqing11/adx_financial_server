package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.Config;
import com.project.entity.RemainingSumRecord;
import com.project.mapper.master.ConfigMapper;
import com.project.mapper.master.RemainingSumRecordMapper;
import com.project.service.RemainingSumRecordService;
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
public class RemainingSumRecordServiceImpl implements RemainingSumRecordService {

    private static Logger logger = LogManager.getLogger(RemainingSumRecordServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private RemainingSumRecordMapper remainingSumRecordMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(RemainingSumRecord remainingSumRecord) {
        try {
            remainingSumRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int count = remainingSumRecordMapper.addSelective(remainingSumRecord);
            if (count > 0) {
                Config config = configMapper.selectConfigInfo();
                JSONObject jsonObject = JSONObject.parseObject(config.getConfig());
                String value = jsonObject.getString("remainingSum");
                String timeUnit = jsonObject.getString("timeUnit");
                boolean isCyclical = jsonObject.getBooleanValue("isCyclical");

                JSONObject json = new JSONObject();
                json.put("remainingSum", remainingSumRecord.getLastRemainingSum());
                json.put("timeUnit", timeUnit);
                json.put("isCyclical", isCyclical);
                config.setConfig(json.toJSONString());
                configMapper.updateConfig(config);

                returnEntity = ReturnUtil.success("新增成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "新增失败");
            }
        } catch (Exception e) {
            logger.error("新增余额记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
