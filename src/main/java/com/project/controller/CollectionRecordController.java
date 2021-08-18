package com.project.controller;

import com.project.entity.CollectionRecord;
import com.project.service.CollectionRecordService;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 收款相关API
 * @author: zhao
 */
@RestController
@RequestMapping(value = "/collectionRecord")
@SuppressWarnings("all")
public class CollectionRecordController {

    @Autowired
    ReturnEntity returnEntity;

    @Autowired
    CollectionRecordService collectionRecordService;

    @ApiOperation(value = "新增收款信息")
    @PostMapping(value = "/addSelective")
    public ReturnEntity addSelective(CollectionRecord collectionRecord) {

        returnEntity = collectionRecordService.addSelective(collectionRecord);
        return returnEntity;

    }

    @ApiOperation(value = "修改收款信息")
    @PostMapping(value = "/updateSelective")
    public ReturnEntity updateSelective(CollectionRecord collectionRecord) {

        returnEntity = collectionRecordService.updateSelective(collectionRecord);
        return returnEntity;

    }

    @ApiOperation(value = "删除收款")
    @PostMapping(value = "/deleteSelective")
    public ReturnEntity deleteSelective(Integer idCollectionRecord) {

        returnEntity = collectionRecordService.deleteSelective(idCollectionRecord);
        return returnEntity;

    }

    @ApiOperation(value = "分页条件查询收款列表")
    @PostMapping(value = "/selectByPage")
    public ReturnEntity selectByPage(Integer pageNum, Integer pageSize,
                              String startTime, String endTime, Integer idCardType) {

        returnEntity = collectionRecordService.selectByPage(pageNum, pageSize, startTime, endTime, idCardType);
        return returnEntity;

    }
}
