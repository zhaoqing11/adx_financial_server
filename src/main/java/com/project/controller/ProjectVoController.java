package com.project.controller;

import com.project.service.ProjectVoService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @description: 监管平台项目相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/ybim_project")
@SuppressWarnings("all")
public class ProjectVoController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    ProjectVoService projectService;

    @ApiOperation(value = "根据项目名获取项目详情")
    @PostMapping(value = "/selectProjectByName")
    public ReturnEntity selectProjectByName(Integer idProject, String projectName) {

        returnEntity = projectService.queryProjectByName(idProject, projectName);
        return returnEntity;

    }

}
