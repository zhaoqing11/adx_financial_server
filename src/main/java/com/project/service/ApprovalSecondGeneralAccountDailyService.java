package com.project.service;

import com.project.entity.ApprovalSecondGeneralAccountDaily;
import com.project.utils.common.base.ReturnEntity;

public interface ApprovalSecondGeneralAccountDailyService {

    ReturnEntity insertSelective(ApprovalSecondGeneralAccountDaily daily, Integer idSecondGeneralAccountDaily);

}
