package com.project.service;

import com.project.entity.CollectionRecord;
import com.project.utils.common.base.ReturnEntity;

public interface CollectionRecordService {

    ReturnEntity addSelective(CollectionRecord collectionRecord);

    ReturnEntity updateSelective(CollectionRecord collectionRecord);

    ReturnEntity deleteSelective(Integer idCollectionRecord);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                              String startTime, String endTime);

}
