package com.project.mapper.master;

import com.project.entity.IncomeFlowRecord;

import java.util.List;

public interface IncomeFlowRecordMapper {

    int addSelective(IncomeFlowRecord incomeFlowRecord);

    List<IncomeFlowRecord> selectAllFlowRecord();

}
