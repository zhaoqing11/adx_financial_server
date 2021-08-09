package com.project.mapper.master;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InspectPlanMapper {

    List<InspectPlan> selectInspectPlanByUserId(@Param("idUser") Integer idUser);

    List<InspectPlan> selectInspectPlanList();

    InspectPlan selectInspectPlanById(@Param("idUser") Integer idUser);

    int insertSelective(InspectPlan inspectPlan);

    int updateSelective(InspectPlan inspectPlan);

    int deletSelective(@Param("idInspectPlan") Integer idInspectPlan);

}
