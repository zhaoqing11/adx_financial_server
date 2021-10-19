package com.project.service;

import com.project.entity.ApprovalPubGeneralDaily;
import com.project.entity.ApprovalPublicDaily;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalPublicDailyService {

    ReturnEntity approvalPubGeneral(ApprovalPubGeneralDaily approvalPublicDaily, Integer idPubGeneralDaily);

    ReturnEntity insertSelective(ApprovalPublicDaily approvalPublicDaily, Integer idPublicDaily);

}
