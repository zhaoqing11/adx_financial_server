package com.project.service;

import com.project.utils.common.base.ReturnEntity;

public interface ReportService {

    ReturnEntity selectReportDetailByDay(int year, int month);

    ReturnEntity deleteSelective(Integer idReport);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                              Integer currentDate);

}
