package com.project.mapper.master;

import com.project.entity.RemainingSumRecord;

public interface RemainingSumRecordMapper {

    RemainingSumRecord queryTodayRemainingSum();

    int addSelective(RemainingSumRecord remainingSumRecord);

}
