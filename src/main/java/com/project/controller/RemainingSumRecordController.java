package com.project.controller;

import com.project.entity.RemainingSumRecord;
import com.project.service.RemainingSumRecordService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 余额相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/remainingSum")
@SuppressWarnings("all")
public class RemainingSumRecordController {

    @Autowired
    RemainingSumRecordService remainingSumRecordService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "新增余额记录")
    @PostMapping(value = "/addSelective")
    public ReturnEntity addSelective(RemainingSumRecord remainingSumRecord) {

        returnEntity = remainingSumRecordService.addSelective(remainingSumRecord);
        return returnEntity;

    }

}
