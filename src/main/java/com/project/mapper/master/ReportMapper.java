package com.project.mapper.master;

import com.project.entity.Report;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(Report report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<Report> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                    @Param("currentDate") Integer currentDate);
}
