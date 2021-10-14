package com.project.mapper.master;

import com.project.entity.SecondGeneralAccountDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondGeneralAccountDailyMapper {

    int insertSelective(SecondGeneralAccountDaily daily);

    int selectByPageTotal(@Param("daily") SecondGeneralAccountDaily daily);

    List<SecondGeneralAccountDaily> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                           @Param("daily") SecondGeneralAccountDaily daily);

}
