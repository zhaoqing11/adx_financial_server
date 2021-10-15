package com.project.service;

import com.project.entity.ApprovalGeneralAccountDaily;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalGeneralAccountDailyService {

    ReturnEntity insertSelective(ApprovalGeneralAccountDaily daily, Integer idGeneralAccountDaily);

}
