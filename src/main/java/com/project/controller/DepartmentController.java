package com.project.controller;

import com.project.service.DepartmentService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 部门相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/department")
@SuppressWarnings("all")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "查询部门列表")
    @PostMapping(value = "/selectAll")
    public ReturnEntity selectAll() {

        returnEntity = departmentService.selectAll();
        return returnEntity;

    }

}
