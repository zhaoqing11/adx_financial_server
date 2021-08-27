package com.project.mapper.master;

import com.project.entity.CollectionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionRecordMapper {

    List<CollectionRecord> selectPageDetailByYear(@Param("idCardType") Integer idCardType, @Param("year") int year);

    String selectCollectionTotalByYear(@Param("idCardType") Integer idCardType, @Param("year") int year);

    int addSelective(CollectionRecord collectionRecord);

    int updateSelective(CollectionRecord collectionRecord);

    int deleteSelective(@Param("idCollectionRecord") Integer idCollectionRecord);

    int selectByPageTotal(@Param("startTime") String startTime, @Param("endTime") String endTime,
                          @Param("idCardType") Integer idCardType);

    List<CollectionRecord> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                        @Param("collectionRecord") CollectionRecord collectionRecord);
}
