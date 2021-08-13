package com.project.controller;

import com.project.service.ReportService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 月报相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/report")
@SuppressWarnings("all")
public class ReportController { // http://localhost:8989/dev-api/report/selectReportDetailByDay?year=2021&month=8&AccessToken=3443d0b4ad3c555db071ac72e2c398c0be34714ffc056355f51cfcf2b2d9a2a218a681cd1bf6f434

    @Autowired
    ReportService reportService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "获取指定月份流水明细")
    @GetMapping(value = "/selectReportDetailByDay")
    public ReturnEntity selectReportDetailByDay(int year, int month) {

        returnEntity = reportService.selectReportDetailByDay(year, month);
        return returnEntity;

    }

    @ApiOperation(value = "删除月报")
    @PostMapping(value = "/deleteSelective")
    public ReturnEntity deleteSelective(Integer idReport) {

        returnEntity = reportService.deleteSelective(idReport);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询月报")
    @PostMapping(value = "/selectByPage")
    public ReturnEntity selectByPage(Integer pageNum, Integer pageSize,
                                     Integer currentDate) {

        returnEntity = reportService.selectByPage(pageNum, pageSize, currentDate);
        return returnEntity;

    }
}
