package com.project.mapper.master;

import com.project.entity.CollectionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionRecordMapper {

    int addSelective(CollectionRecord collectionRecord);

    int updateSelective(CollectionRecord collectionRecord);

    int deleteSelective(@Param("idCollectionRecord") Integer idCollectionRecord);

    int selectByPageTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CollectionRecord> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                        @Param("startTime") String startTime, @Param("endTime") String endTime);
}
