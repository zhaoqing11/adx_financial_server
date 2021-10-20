package com.project.mapper.master;

import com.project.entity.PubGeneralReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubGeneralReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(PubGeneralReport report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<PubGeneralReport> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                            @Param("currentDate") Integer currentDate);

}
