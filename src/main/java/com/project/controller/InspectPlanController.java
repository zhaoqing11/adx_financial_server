package com.project.controller;

import com.project.service.InspectPlanService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽查计划项目API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/inspectPlan")
@SuppressWarnings("All")
public class InspectPlanController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    InspectPlanService inspectPlanService;

    @ApiOperation("创建抽查检查计划")
    @PostMapping(value = "/insertInspectPlan")
    public ReturnEntity insertSelective(InspectPlan inspectPlan) {

        returnEntity = inspectPlanService.insertSelective(inspectPlan);
        return returnEntity;

    }

}
