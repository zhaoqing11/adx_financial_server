package com.project.mapper.master;

import com.project.entity.PrivateReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivateReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(PrivateReport report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<PrivateReport> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                    @Param("currentDate") Integer currentDate);
}
