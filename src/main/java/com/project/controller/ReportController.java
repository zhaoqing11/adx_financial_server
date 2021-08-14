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
    public void exportToExcel(HttpServletResponse response, int year, int month) {

        reportService.exportToExcel(response, year, month);

    }

    @ApiOperation(value = "获取指定月份流水明细")
    @PostMapping(value = "/selectReportDetailByDay")
    public ReturnEntity selectReportDetailByDay(int year, int month) {

        returnEntity = reportService.selectReportDetailByMonth(year, month);
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
