package com.project.controller;

import com.project.service.IndexService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @ApiOperation(value = "获取（普通账户2）部门支出明细")
    @PostMapping(value = "/getSecondGeneralFlowRecordDetails")
    ReturnEntity getSecondGeneralFlowRecordDetails(int year) {

        returnEntity = indexService.getSecondGeneralFlowRecordDetails(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（普通账户1）部门支出明细")
    @PostMapping(value = "/getGeneralRecordDetails")
    ReturnEntity getGeneralRecordDetails(int year) {

        returnEntity = indexService.getGeneralRecordDetails(year);
        return returnEntity;

    }


    @ApiOperation(value = "获取（普通账户2）部门支出流水")
    @PostMapping(value = "/getSecondGeneralRecordByDepartment")
    ReturnEntity getSecondGeneralRecordByDepartment(int year){

        returnEntity = indexService.getSecondGeneralRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（普通账户1）部门支出流水")
    @PostMapping(value = "/getGeneralRecordByDepartment")
    ReturnEntity getGeneralRecordByDepartment(int year) {

        returnEntity = indexService.getGeneralRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取年收入支出总数")
    @PostMapping(value = "/getDataInfo")
    ReturnEntity getDataInfo(Integer idCardType, int year) {

        returnEntity = indexService.getDataInfo(idCardType, year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（公账）部门支出流水")
    @PostMapping(value = "/publicFlowRecordByDepartment")
    ReturnEntity publicFlowRecordByDepartment(int year) {

        returnEntity = indexService.publicFlowRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（私账）部门支出流水")
    @PostMapping(value = "/privateFlowRecordByDepartment")
    ReturnEntity privateFlowRecordByDepartment(int year) {

        returnEntity = indexService.privateFlowRecordByDepartment(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（公账）部门支出明细")
    @PostMapping(value = "/getPublicFlowRecordDetails")
    ReturnEntity getPublicFlowRecordDetails(int year) {

        returnEntity = indexService.getPublicFlowRecordDetails(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取（私账）部门支出明细")
    @PostMapping(value = "/getPrivateFlowRecordDetails")
    ReturnEntity getPrivateFlowRecordDetails(int year) {

        returnEntity = indexService.getPrivateFlowRecordDetails(year);
        return returnEntity;

    }

    @ApiOperation(value = "获取指定年分每月收入明细")
    @PostMapping(value = "/getCollectionRecordByMonth")
    ReturnEntity getCollectionRecordByMonth(Integer idCardType, int year) {

        returnEntity = indexService.getCollectionRecordByMonth(idCardType, year);
        return returnEntity;

    }

}
