package com.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.entity.Config;
import com.project.entity.ConfigVO;
import com.project.mapper.master.ConfigMapper;
import com.project.service.ConfigService;
import com.project.utils.DecimalFormatUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@SuppressWarnings("all")
public class ConfigServiceImpl implements ConfigService {

    private static Logger logger = LogManager.getLogger(ConfigServiceImpl.class);

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Value("${project.telephone}")
    private String telephone;

    @Override
    public ReturnEntity selectAll() {
        try {
            List<Config> configList = configMapper.selectAll();
            returnEntity = ReturnUtil.success(configList);
        } catch (Exception e) {
            logger.error("获取账户信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateConfig(Config config) {
        try {
            ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);
            BigDecimal remainingSum = new BigDecimal(configVO.getRemainingSum());
            configVO.setTelephone(telephone);
            configVO.setRemainingSum(DecimalFormatUtil.formatString2(remainingSum, null));
            config.setConfig(JSON.toJSONString(configVO));
            int count = configMapper.updateConfig(config);
            if (count > 0) {
                returnEntity = ReturnUtil.success("编辑成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "编辑失败");
            }
        } catch (Exception e) {
            logger.error("修改账户信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
