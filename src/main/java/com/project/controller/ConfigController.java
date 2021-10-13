package com.project.controller;

import com.project.entity.Config;
import com.project.service.ConfigService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 账户设置相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/config")
@SuppressWarnings("all")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "新增账户")
    @PostMapping(value = "/insertSelective")
    public ReturnEntity insertSelective(Config config) {

        returnEntity = configService.insertSelective(config);
        return returnEntity;

    }

    @ApiOperation(value = "获取账户信息")
    @PostMapping(value = "/selectAll")
    public ReturnEntity selectAll() {

        returnEntity = configService.selectAll();
        return returnEntity;

    }

    @ApiOperation(value = "修改账户信息")
    @PostMapping(value = "/updateConfig")
    public ReturnEntity updateConfig(Config config) {

        returnEntity = configService.updateConfig(config);
        return returnEntity;

    }
}
