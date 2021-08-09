package com.project.controller;

import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽查检查相关API
 *
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/inspectionGroup")
public class InspectionGroupController {

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "获取当前用户待办任务列表")
    @PostMapping(value = "/getInspectionGroupById")
    public ReturnEntity getInspectionGroupById(Integer userId) {

//        returnEntity = inspectionGroupService.selectInspectionGroupById(userId);
        return returnEntity;

    }

    @ApiOperation(value = "获取抽查检查列表")
    @PostMapping(value = "/getInspectionList")
    public ReturnEntity getInspectionList(String projectName) {

//        returnEntity = inspectionGroupService.getInspectionList(projectName);
        return returnEntity;

    }
}
