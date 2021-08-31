package com.project.mapper.master;

import com.project.entity.PrivateDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivateDailyMapper {

    PrivateDaily selectByPrimaryKey(@Param("idDaily") Integer idDaily);

    int selectIsExitUnApprovalDaily();

    int updateSelective(PrivateDaily daily);

    int insertSelective(PrivateDaily daily);

    int selectByPageTotal(@Param("daily") PrivateDaily daily);

    List<PrivateDaily> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                    @Param("daily") PrivateDaily daily);

}
