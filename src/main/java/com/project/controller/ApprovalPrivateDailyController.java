package com.project.controller;

import com.project.entity.ApprovalPrivateDaily;
import com.project.service.ApprovalPrivateDailyService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: （私账）日报相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/approvalPrivateDaily")
@SuppressWarnings("all")
public class ApprovalPrivateDailyController {

    @Autowired
    ApprovalPrivateDailyService approvalPrivateDailyService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "新增（私账）日报审批记录")
    @PostMapping(value = "/insertSelective")
    public ReturnEntity insertSelective(ApprovalPrivateDaily approvalPrivateDaily, Integer idPrivateDaily) {

        returnEntity = approvalPrivateDailyService.insertSelective(approvalPrivateDaily, idPrivateDaily);
        return returnEntity;

    }
}
