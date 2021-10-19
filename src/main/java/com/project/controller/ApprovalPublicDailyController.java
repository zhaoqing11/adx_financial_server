package com.project.controller;

import com.project.entity.ApprovalPubGeneralDaily;
import com.project.entity.ApprovalPublicDaily;
import com.project.service.ApprovalPublicDailyService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: （公账）日报相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/approvalPublicDaily")
@SuppressWarnings("all")
public class ApprovalPublicDailyController {

    @Autowired
    ApprovalPublicDailyService approvalPublicDailyService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "审批（公账-账户3）日报审批记录")
    @PostMapping(value = "/approvalPubGeneral")
    public ReturnEntity approvalPubGeneral(ApprovalPubGeneralDaily approvalPublicDaily, Integer idPubGeneralDaily) {

        returnEntity = approvalPublicDailyService.approvalPubGeneral(approvalPublicDaily, idPubGeneralDaily);
        return returnEntity;

    }

    @ApiOperation(value = "新增（公账）日报审批记录")
    @PostMapping(value = "/insertSelective")
    public ReturnEntity insertSelective(ApprovalPublicDaily approvalPublicDaily, Integer idPublicDaily) {

        returnEntity = approvalPublicDailyService.insertSelective(approvalPublicDaily, idPublicDaily);
        return returnEntity;

    }
}
