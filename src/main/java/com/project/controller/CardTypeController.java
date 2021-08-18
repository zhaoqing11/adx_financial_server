package com.project.controller;

import com.project.service.CardTypeService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 账号类型相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/cardType")
@SuppressWarnings("all")
public class CardTypeController {

    @Autowired
    CardTypeService cardTypeService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "查询账号类型")
    @PostMapping(value = "/selectAll")
    public ReturnEntity selectAll() {

        returnEntity = cardTypeService.selectAll();
        return returnEntity;

    }

}
