package com.project.mapper.master;

import com.project.entity.GeneralAccountReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralAccountReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(GeneralAccountReport report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<GeneralAccountReport> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                    @Param("currentDate") Integer currentDate);
}
