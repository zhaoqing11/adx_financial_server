package com.project.service;

import com.project.utils.common.base.ReturnEntity;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    void exportToExcel(HttpServletResponse response, int year, int month);

    ReturnEntity selectReportDetailByMonth(int year, int month);

    ReturnEntity deleteSelective(Integer idReport);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                              Integer currentDate);

}
