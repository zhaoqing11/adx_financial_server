package com.project.mapper.master;

import com.project.entity.PublicReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublicReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(PublicReport report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<PublicReport> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                    @Param("currentDate") Integer currentDate);
}
