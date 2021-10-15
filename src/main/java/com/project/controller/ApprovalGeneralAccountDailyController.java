package com.project.controller;

import com.project.entity.ApprovalGeneralAccountDaily;
import com.project.entity.ApprovalSecondGeneralAccountDaily;
import com.project.service.ApprovalGeneralAccountDailyService;
import com.project.service.ApprovalSecondGeneralAccountDailyService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: （普通账户）日账相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/approvalGeneralDaily")
@SuppressWarnings("all")
public class ApprovalGeneralAccountDailyController {

    @Autowired
    ApprovalGeneralAccountDailyService dailyService;

    @Autowired
    ApprovalSecondGeneralAccountDailyService secondDailyService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "新增日账审批记录(普通账户2)")
    @PostMapping(value = "/insertSecondGeneralDaily")
    public ReturnEntity insertSelective(ApprovalSecondGeneralAccountDaily accountDaily, Integer idSecondGeneralAccountDaily) {

        returnEntity = secondDailyService.insertSelective(accountDaily, idSecondGeneralAccountDaily);
        return returnEntity;

    }

    @ApiOperation(value = "新增日账审批记录(普通账户1)")
    @PostMapping(value = "/insertGeneralDaily")
    public ReturnEntity insertSelective(ApprovalGeneralAccountDaily accountDaily, Integer idGeneralAccountDaily) {

        returnEntity = dailyService.insertSelective(accountDaily, idGeneralAccountDaily);
        return returnEntity;

    }

}
