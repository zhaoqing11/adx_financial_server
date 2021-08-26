package com.project.controller;

import com.project.entity.Department;
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

    @ApiOperation(value = "创建部门")
    @PostMapping(value = "/addDepartment")
    public ReturnEntity insertSelective(Department department){

        returnEntity = departmentService.insertSelective(department);
        return returnEntity;

    }

    @ApiOperation(value = "修改部门")
    @PostMapping(value = "/updateDepartment")
    public ReturnEntity updateSelective(Department department){

        returnEntity = departmentService.updateSelective(department);
        return returnEntity;

    }

    @ApiOperation(value = "删除部门")
    @PostMapping(value = "/deletDepartment")
    public ReturnEntity deleteSelective(Integer idDepartment){

        returnEntity = departmentService.deleteSelective(idDepartment);
        return returnEntity;

    }

    @ApiOperation(value = "查询部门列表")
    @PostMapping(value = "/selectAll")
    public ReturnEntity selectAll() {

        returnEntity = departmentService.selectAll();
        return returnEntity;

    }

}
