package com.project.mapper.master;

import com.project.entity.PayFlowRecord;

import java.util.List;

public interface PayFlowRecordMapper {

    int addSelective(PayFlowRecord payFlowRecord);

    List<PayFlowRecord> selectAllFlowRecord();

}
