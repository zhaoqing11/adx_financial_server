package com.project.mapper.master;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectFieldValuesMapper {

    int insertSelective(ProjectFieldValues projectFieldValues);

    int updateSelective(ProjectFieldValues projectFieldValues);

    List<ProjectFieldValues> selectIsExitsUnCommit(@Param("idInspectionPlan") Integer idInspectionPlan);

    int deleteProjectValues(@Param("idProject") Integer idProject);

    List<ProjectFieldValues> selectByIdProject(@Param("idProject") Integer idProject);

    int addProjectFieldValues(@Param("projectFieldValuesList") List<ProjectFieldValues> projectFieldValuesList);

}
