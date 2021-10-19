package com.project.controller;

import com.project.service.RoleService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 角色相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/role")
@SuppressWarnings("all")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "获取角色权限列表")
    @PostMapping(value = "/getAuthByRole")
    public ReturnEntity getAuthByRole(Integer idRole) {

        returnEntity = roleService.getAuthByRole(idRole);
        return returnEntity;

    }

    @ApiOperation(value = "查询角色列表")
    @PostMapping(value = "/selectAll")
    public ReturnEntity selectAll() {

        returnEntity = roleService.selectAll();
        return returnEntity;

    }

}
