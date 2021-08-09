package com.project.mapper.master;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectFieldMapper {

    List<ProjectField> selectProjectFieldById(@Param("idProject") Integer idProject);

    int insertSeleative(ProjectField projectField);

    int addProjectFields(@Param("projectField") List<ProjectField> projectField);

    List<ProjectField> selectAll();

}
