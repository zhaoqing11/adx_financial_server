package com.project.service;

import com.project.utils.common.base.ReturnEntity;

public interface IndexService {

    ReturnEntity getPubGeneralRecordDetails(int year);

    ReturnEntity getPubGeneralRecordByDepartment(int year);

    ReturnEntity getSecondGeneralFlowRecordDetails(int year);

    ReturnEntity getGeneralRecordDetails(int year);

    ReturnEntity getSecondGeneralRecordByDepartment(int year);

    ReturnEntity getGeneralRecordByDepartment(int year);

    ReturnEntity getDataInfo(Integer idCardType, int year);

    ReturnEntity publicFlowRecordByDepartment(int year);

    ReturnEntity privateFlowRecordByDepartment(int year);

    ReturnEntity getPublicFlowRecordDetails(int year);

    ReturnEntity getPrivateFlowRecordDetails(int year);

    ReturnEntity getCollectionRecordByMonth(Integer idCardType, int year);

}
