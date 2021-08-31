package com.project.mapper.master;

import com.project.entity.PublicDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublicDailyMapper {

    int selectPublicDailyByState(@Param("state") Integer state);

    PublicDaily selectByPrimaryKey(@Param("idDaily") Integer idDaily);

    int selectIsExitUnApprovalDaily();

    int updateSelective(PublicDaily daily);

    int insertSelective(PublicDaily daily);

    int selectByPageTotal(@Param("daily") PublicDaily daily);

    List<PublicDaily> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                   @Param("daily") PublicDaily daily);

}
