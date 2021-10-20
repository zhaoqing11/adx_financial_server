package com.project.mapper.master;

import com.project.entity.GeneralAccountDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralAccountDailyMapper {

    int selectIsExitUnApprovalDaily();

    int insertSelective(GeneralAccountDaily daily);

    int updateSelective(GeneralAccountDaily daily);

    GeneralAccountDaily selectByPrimaryKey(@Param("idGeneralAccountDaily") Integer idGeneralAccountDaily);

    int selectByPageTotal(@Param("daily") GeneralAccountDaily daily);

    List<GeneralAccountDaily> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                   @Param("daily") GeneralAccountDaily daily);

}
