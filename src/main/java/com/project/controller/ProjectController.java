package com.project.controller;

import com.project.entity.Project;
import com.project.service.ProjectService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description: 项目相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/project")
@SuppressWarnings("all")
public class ProjectController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    ProjectService projectService;

    @ApiOperation(value = "getMaxSort")
    @GetMapping(value = "/getMaxSort")
    public ReturnEntity getMaxSort() {

        returnEntity = projectService.selectMaxSort();
        return returnEntity;

    }

    @ApiOperation(value = "查询序号是否存在")
    @PostMapping(value = "/selectSortIsExits")
    public ReturnEntity selectSortIsExits(Integer sort) {

        returnEntity = projectService.selectSortIsExits(sort);
        return returnEntity;

    }

    @ApiOperation(value = "获取未提交项目")
    @PostMapping(value = "/selectIsExitsUnCommit")
    public ReturnEntity selectIsExitsUnCommit(Integer idInspectPlan) {

        returnEntity = projectService.selectIsExitsUnCommit(idInspectPlan);
        return returnEntity;

    }

    @ApiOperation(value = "导出数据至pdf")
    @GetMapping(value = "/exportSheet")
    public void exportSheet(HttpServletResponse response, Integer idProject) {

        projectService.exportSheet(response, idProject);

    }

    @ApiOperation(value = "获取幕墙结构列表")
    @GetMapping(value = "/getCurtainStructureList")
    public ReturnEntity getCurtainStructureList() {

        returnEntity = projectService.getCurtainStructureList();
        return returnEntity;

    }

    @ApiOperation(value = "获取玻璃幕墙类型列表")
    @GetMapping(value = "/getProjectTypeList")
    public ReturnEntity getProjectTypeList() {

        returnEntity = projectService.getProjectTypeList();
        return returnEntity;

    }

    @ApiOperation(value = "获取项目列表")
    @GetMapping(value = "/selectAllProjects")
    public ReturnEntity selectAllProjects() {

        returnEntity = projectService.selectAllProjects();
        return returnEntity;

    }

    @ApiOperation(value = "获取项目字典列表")
    @GetMapping(value = "/selectProjectFields")
    public ReturnEntity selectProjectFields() {

        returnEntity = projectService.selectProjectFieldAll();
        return returnEntity;

    }


    @ApiOperation(value = "新增项目")
    @PostMapping(value = "/addProject")
    public ReturnEntity addProject(@RequestBody  List<ProjectFieldValues> projectFieldValuesList, Project project) {

        returnEntity = projectService.insertSelective(projectFieldValuesList, project);
        return returnEntity;

    }

    @ApiOperation(value = "根据id获取项目详情")
    @PostMapping(value = "/selectByIdProject")
    public ReturnEntity selectByIdProject(Integer idProject) {

        returnEntity = projectService.selectByIdProject(idProject);
        return returnEntity;

    }

    @ApiOperation(value = "删除项目")
    @PostMapping(value = "/deleteProject")
    public ReturnEntity deleteProject(Integer idProject) {

        returnEntity = projectService.deleteProject(idProject);
        return returnEntity;

    }

}
