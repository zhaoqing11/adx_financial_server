package com.project.mapper.master;

import com.project.entity.PubGeneralDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubGeneralDailyMapper {

    int selectIsExitUnApprovalDaily();

    PubGeneralDaily selectByPrimaryKey(@Param("idDaily") Integer idDaily);

    int updateSelective(PubGeneralDaily daily);

    int insertSelective(PubGeneralDaily daily);

    int selectByPageTotal(@Param("daily") PubGeneralDaily daily);

    List<PubGeneralDaily> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                   @Param("daily") PubGeneralDaily daily);

}
