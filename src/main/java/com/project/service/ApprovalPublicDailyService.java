package com.project.service;

import com.project.entity.ApprovalPublicDaily;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalPublicDailyService {

    ReturnEntity insertSelective(ApprovalPublicDaily approvalPublicDaily, Integer idPublicDaily);

}
