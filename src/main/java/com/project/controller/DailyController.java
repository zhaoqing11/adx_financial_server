package com.project.controller;

import com.project.entity.PublicDaily;
import com.project.service.DailyService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 日账单相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/daily")
@SuppressWarnings("all")
public class DailyController {


    @Autowired
    DailyService dailyService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "修改日账单")
    @PostMapping(value = "/updateDaily")
    public ReturnEntity updateSelective(PublicDaily daily){
        returnEntity = dailyService.updateSelective(daily);
        return returnEntity;
    }

    @ApiOperation(value = "新增日账单")
    @PostMapping(value = "/insertDaily")
    public ReturnEntity insertSelective(PublicDaily daily){
        returnEntity = dailyService.insertSelective(daily);
        return returnEntity;
    }

    @ApiOperation(value = "获取每日账单列表")
    @PostMapping(value = "/selectDailyList")
    public ReturnEntity selectByPage(Integer pageNum, Integer pageSize, PublicDaily daily){
        returnEntity = dailyService.selectByPage(pageNum, pageSize, daily);
        return returnEntity;
    }

}
