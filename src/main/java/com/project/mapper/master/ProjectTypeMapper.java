package com.project.mapper.master;

import java.util.List;

public interface ProjectTypeMapper {

    List<ProjectType> getProjectTypeList();

    List<ProjectType> selectById(Integer[] idProjectTypes); // @Param("idProjectType") Integer idProjectType

}
