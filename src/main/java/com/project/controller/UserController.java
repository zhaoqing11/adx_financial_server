package com.project.controller;

import com.project.entity.User;
import com.project.service.UserService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 用户相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/user")
@SuppressWarnings("all")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "修改用户密码")
    @PostMapping(value = "/updatePassword")
    public ReturnEntity updatePassword(Integer idUser, String oldPassword, String newPassword) {

        returnEntity = userService.updatePassword(idUser, oldPassword, newPassword);
        return returnEntity;

    }

    @ApiOperation(value = "分页（条件）查询用户列表")
    @PostMapping(value = "/selectByPage")
    public ReturnEntity selectByPage(Integer pageNum, Integer pageSize, User user) {

        returnEntity = userService.selectByPage(pageNum, pageSize, user);
        return returnEntity;

    }

    @ApiOperation(value = "根据用户id查询用户信息")
    @PostMapping(value = "/selectUserById")
    public ReturnEntity selectByPrimaryKey(Integer idUser) {

        returnEntity = userService.selectByPrimaryKey(idUser);
        return returnEntity;

    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "/deleteUser")
    public ReturnEntity deleteSelective(Integer idUser) {

        returnEntity = userService.deleteSelective(idUser);
        return returnEntity;

    }

    @ApiOperation(value = "修改用户")
    @PostMapping(value = "/updateUser")
    public ReturnEntity updateSelective(User user) {

        returnEntity = userService.updateSelective(user);
        return returnEntity;

    }

    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/addUser")
    public ReturnEntity insertSelective(User user) {

        returnEntity = userService.insertSelective(user);
        return returnEntity;

    }

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public ReturnEntity login(User user) {

        returnEntity = userService.login(user);
        return returnEntity;

    }

    @ApiOperation(value = "登出")
    @PostMapping(value = "/logout")
    public ReturnEntity logout(String userId, String token) {

        returnEntity = userService.logout(userId, token);

        return returnEntity;
    }
}
