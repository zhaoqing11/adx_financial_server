package com.project.controller;

import com.project.service.ReportService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 月报相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/report")
@SuppressWarnings("all")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "导出收支明细")
    @GetMapping(value = "/exportToExcel")
    public void exportToExcel(HttpServletResponse response, int year, int month, Integer idCardType) {

        reportService.exportToExcel(response, year, month, idCardType);

    }

    @ApiOperation(value = "获取指定月份流水明细")
    @PostMapping(value = "/selectReportDetailByDay")
    public ReturnEntity selectReportDetailByDay(int year, int month, Integer idCardType) {

        returnEntity = reportService.selectReportDetailByMonth(year, month, idCardType);
        return returnEntity;

    }

    @ApiOperation(value = "删除月报")
    @PostMapping(value = "/deleteSelective")
    public ReturnEntity deleteSelective(Integer idReport) {

        returnEntity = reportService.deleteSelective(idReport);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询（公账）月报")
    @PostMapping(value = "/selectPublicReportByPage")
    public ReturnEntity selectPublicReportByPage(Integer pageNum, Integer pageSize,
                                     Integer currentDate) {

        returnEntity = reportService.selectPublicReportByPage(pageNum, pageSize, currentDate);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询（私账）月报")
    @PostMapping(value = "/selectPrivateReportByPage")
    public ReturnEntity selectPrivateReportByPage(Integer pageNum, Integer pageSize,
                                     Integer currentDate) {

        returnEntity = reportService.selectPrivateReportByPage(pageNum, pageSize, currentDate);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询普通账户2月报")
    @PostMapping(value = "/selectSecondGeneralReportByPage")
    ReturnEntity selectSecondGeneralReportByPage(Integer pageNum, Integer pageSize,
                                                 Integer currentDate) {

        returnEntity = reportService.selectSecondGeneralReportByPage(pageNum, pageSize, currentDate);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询普通账户1月报")
    @PostMapping(value = "/selectGeneralReportByPage")
    ReturnEntity selectGeneralReportByPage(Integer pageNum, Integer pageSize,
                                           Integer currentDate) {

        returnEntity = reportService.selectGeneralReportByPage(pageNum, pageSize, currentDate);
        return returnEntity;

    }

}
