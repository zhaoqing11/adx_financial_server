package com.project.mapper.master;

import com.project.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {

    Project selectProjectByIdInspectPlan(@Param("idInspectPlan") Integer idInspectPlan);

    Project selectMaxSort();

    int selectSortIsExits(@Param("sort") Integer sort);

    int updateSelective(Project project);

    int deleteProject(@Param("idProject") Integer idProject);

    int updateOldCodeByIdProject(@Param("oldCode") String oldCode, @Param("idProject") Integer idProject);

    List<Project> selectAll();

    Project selectByIdProject(@Param("idProject") Integer idProject);

    int addProject(Project project);

}
