package com.project.service;

import com.project.utils.common.base.ReturnEntity;

public interface IndexService {

    ReturnEntity getDataInfo(Integer idCardType, int year);

    ReturnEntity publicFlowRecordByDepartment(int year);

    ReturnEntity privateFlowRecordByDepartment(int year);

    ReturnEntity getPublicFlowRecordDetails(int year);

    ReturnEntity getPrivateFlowRecordDetails(int year);

    ReturnEntity getCollectionRecordByMonth(Integer idCardType, int year);

}
