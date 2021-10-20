package com.project.service;

import com.project.utils.common.base.ReturnEntity;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    ReturnEntity selectPubGeneralReportByPage(Integer startIndex, Integer pageSize,
                                           Integer currentDate);

    ReturnEntity selectSecondGeneralReportByPage(Integer startIndex, Integer pageSize,
                                           Integer currentDate);

    ReturnEntity selectGeneralReportByPage(Integer startIndex, Integer pageSize,
                                          Integer currentDate);

    void exportToExcel(HttpServletResponse response, int year, int month, Integer idCardType);

    ReturnEntity selectReportDetailByMonth(int year, int month, Integer idCardType);

    ReturnEntity deleteSelective(Integer idReport, Integer idCardType);

    ReturnEntity selectPublicReportByPage(Integer startIndex, Integer pageSize,
                              Integer currentDate);

    ReturnEntity selectPrivateReportByPage(Integer startIndex, Integer pageSize,
                                           Integer currentDate);

}
