package com.project.mapper.master;

import com.project.entity.RemainingSumRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RemainingSumRecordMapper {

    List<RemainingSumRecord> queryRemainingSumByMonth(@Param("currentDate") String currentDate);

    RemainingSumRecord queryTodayRemainingSum(@Param("idCardType") Integer idCardType);

    int addSelective(RemainingSumRecord remainingSumRecord);

}
