package com.project.service;

import com.project.entity.ApprovalPrivateDaily;
import com.project.entity.ApprovalPublicDaily;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalPrivateDailyService {

    ReturnEntity insertSelective(ApprovalPrivateDaily approvalPrivateDaily, Integer idPrivateDaily);

}
