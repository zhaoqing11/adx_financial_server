package com.project.controller;

import com.project.entity.*;
import com.project.service.DailyService;
import com.project.service.GeneralAccountDailyService;
import com.project.service.SecondGeneralAccountDailyService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 日账单相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/daily")
@SuppressWarnings("all")
public class DailyController {


    @Autowired
    DailyService dailyService;

    @Autowired
    GeneralAccountDailyService generalAccountDailyService;

    @Autowired
    SecondGeneralAccountDailyService secondGeneralAccountDailyService;

    @Autowired
    ReturnEntity returnEntity;

    @ApiOperation(value = "发送短信")
    @PostMapping(value = "/sendMessage")
    public ReturnEntity sendMessage() {

        returnEntity = dailyService.sendMessage();
        return returnEntity;

    }

    @ApiOperation(value = "查询待办任务")
    @PostMapping(value = "/getDataInfo")
    public ReturnEntity getDataInfo(int idRole) {

        returnEntity = dailyService.getDataInfo(idRole);
        return returnEntity;

    }

    @ApiOperation(value = "根据指定日期获取（公账-账户3）收支明细")
    @PostMapping(value = "/selectPubGeneralDailyByDate")
    public ReturnEntity queryPubGeneralDailyByDate(String date) {

        returnEntity = dailyService.queryPubGeneralDailyByDate(date);
        return returnEntity;

    }

    @ApiOperation(value = "获取每日账单列表（公账-账户4）")
    @PostMapping(value = "/selectPubGeneralDailyByPage")
    public ReturnEntity selectPubGeneralDailyByPage(Integer pageNum, Integer pageSize, PubGeneralDaily daily) {

        returnEntity = dailyService.selectPubGeneralDailyByPage(pageNum, pageSize, daily);
        return returnEntity;

    }

    @ApiOperation(value = "根据指定日期获取收支明细")
    @PostMapping(value = "/selectGeneralDailyByDate")
    public ReturnEntity queryGeneralDailyByDate(String date) {

        returnEntity = dailyService.queryGeneralDailyByDate(date);
        return returnEntity;

    }

    @ApiOperation(value = "根据指定日期获取收支明细")
    @PostMapping(value = "/selectSecondGeneralDailyByDate")
    public ReturnEntity querySecondGeneralDailyByDate(String date) {

        returnEntity = dailyService.querySecondGeneralDailyByDate(date);
        return returnEntity;

    }

    @ApiOperation(value = "获取每日账单列表（普通账户2）")
    @PostMapping(value = "/selectSecondGeneralAccountDailyByPage")
    public ReturnEntity selectSecondGeneralAccountDailyByPage(Integer pageNum, Integer pageSize, SecondGeneralAccountDaily daily){

        returnEntity = secondGeneralAccountDailyService.selectDailyByPage(pageNum, pageSize, daily);
        return returnEntity;

    }

    @ApiOperation(value = "获取每日账单列表（普通账户1）")
    @PostMapping(value = "/selectGeneralAccountDailyByPage")
    public ReturnEntity selectGeneralAccountDailyByPage(Integer pageNum, Integer pageSize, GeneralAccountDaily daily){

        returnEntity = generalAccountDailyService.selectDailyByPage(pageNum, pageSize, daily);
        return returnEntity;

    }


    @ApiOperation(value = "查询待审批账单数量")
    @PostMapping(value = "/selectDailyByState")
    public ReturnEntity selectDailyByState(Integer idRole) {

        returnEntity = dailyService.selectDailyByState(idRole);
        return returnEntity;

    }

    @ApiOperation(value = "查询上一天账单核对信息")
    @PostMapping(value = "/selectIsExitUnApprovalDaily")
    public ReturnEntity selectIsExitUnApprovalDaily(Integer idCardType) {

        returnEntity = dailyService.selectIsExitUnApprovalDaily(idCardType);
        return returnEntity;

    }

    @ApiOperation(value = "根据指定日期获取（私账）收支明细")
    @PostMapping(value = "/selectPrivateDailyByDate")
    public ReturnEntity selectPrivateDailyByDate(String date) {

        returnEntity = dailyService.queryPrivateDailyByDate(date);
        return returnEntity;

    }

    @ApiOperation(value = "根据指定日期获取（公账）收支明细")
    @PostMapping(value = "/selectPublicDailyByDate")
    public ReturnEntity selectPublicDailyByDate(String date) {

        returnEntity = dailyService.queryPublicDailyByDate(date);
        return returnEntity;

    }

    @ApiOperation(value = "修改日账单（公账）")
    @PostMapping(value = "/updatePublicDaily")
    public ReturnEntity updatePublicDaily(PublicDaily daily){

        returnEntity = dailyService.updatePublicDaily(daily);
        return returnEntity;

    }

    @ApiOperation(value = "修改日账单（私账）")
    @PostMapping(value = "/updatePrivateDaily")
    public ReturnEntity updatePrivateDaily(PrivateDaily daily){

        returnEntity = dailyService.updatePrivateDaily(daily);
        return returnEntity;

    }

    @ApiOperation(value = "新增日账单")
    @PostMapping(value = "/insertDaily")
    public ReturnEntity insertSelective(PublicDaily daily){

        returnEntity = dailyService.insertPublicDaily(daily);
        return returnEntity;

    }

    @ApiOperation(value = "获取每日账单列表（公账）")
    @PostMapping(value = "/selectPublicDailyByPage")
    public ReturnEntity selectPublicDailyByPage(Integer pageNum, Integer pageSize, PublicDaily daily){

        returnEntity = dailyService.selectPublicDailyByPage(pageNum, pageSize, daily);
        return returnEntity;

    }

    @ApiOperation(value = "获取每日账单列表（私账）")
    @PostMapping(value = "/selectPrivateDailyByPage")
    public ReturnEntity selectPrivateDailyByPage(Integer pageNum, Integer pageSize, PrivateDaily daily) {

        returnEntity = dailyService.selectPrivateDailyByPage(pageNum, pageSize, daily);
        return returnEntity;

    }

}
