package com.project.controller;

import com.project.service.IndexService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 统计相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/flowRecordStatistics")
@SuppressWarnings("all")
public class IndexController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    IndexService indexService;

    @ApiOperation(value = "获取年收入支出总数")
    @GetMapping(value = "/getDataInfo")
    ReturnEntity getDataInfo(Integer idCardType, int year) {

        returnEntity = indexService.getDataInfo(idCardType, year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（公账）部门支出流水")
    @GetMapping(value = "/publicFlowRecordByDepartment")
    ReturnEntity publicFlowRecordByDepartment(int year) {

        returnEntity = indexService.publicFlowRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（私账）部门支出流水")
    @GetMapping(value = "/privateFlowRecordByDepartment")
    ReturnEntity privateFlowRecordByDepartment(int year) {

        returnEntity = indexService.privateFlowRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（公账）部门支出明细")
    @GetMapping(value = "/getPublicFlowRecordDetails")
    ReturnEntity getPublicFlowRecordDetails(int year) {

        returnEntity = indexService.getPublicFlowRecordDetails(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（私账）部门支出明细")
    @GetMapping(value = "/getPrivateFlowRecordDetails")
    ReturnEntity getPrivateFlowRecordDetails(int year) {

        returnEntity = indexService.getPrivateFlowRecordDetails(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取指定年分每月收入明细")
    @GetMapping(value = "/getCollectionRecordByMonth")
    ReturnEntity getCollectionRecordByMonth(Integer idCardType, int year) {

        returnEntity = indexService.getCollectionRecordByMonth(idCardType, year);
        return returnEntity;

    }
}
