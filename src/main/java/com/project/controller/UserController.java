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
