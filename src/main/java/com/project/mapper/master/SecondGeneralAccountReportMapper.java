package com.project.mapper.master;

import com.project.entity.SecondGeneralAccountReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondGeneralAccountReportMapper {

    int deleteSelective(@Param("idReport") Integer idReport);

    int addSelective(SecondGeneralAccountReport report);

    int selectByPageTotal(@Param("currentDate") Integer currentDate);

    List<SecondGeneralAccountReport> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                            @Param("currentDate") Integer currentDate);
}
