package com.project.service;

import com.project.entity.Project;
import com.project.utils.common.base.ReturnEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectService {

    ReturnEntity selectMaxSort();

    ReturnEntity selectSortIsExits(Integer sort);

    ReturnEntity selectIsExitsUnCommit(Integer idInspectPlan);

    ReturnEntity deleteProject(Integer idProject);

    void exportSheet(HttpServletResponse response, Integer idProject);

    ReturnEntity getProjectTypeList();

    ReturnEntity getCurtainStructureList();

    ReturnEntity selectAllProjects();

    ReturnEntity selectProjectFieldAll();

//    ReturnEntity insertSelective(List<ProjectFieldValues> projectFieldValuesList, Project project);

    ReturnEntity selectByIdProject(Integer idProject);
}
