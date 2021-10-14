package com.project.service;

import com.project.entity.GeneralAccountDaily;
import com.project.utils.common.base.ReturnEntity;

public interface GeneralAccountDailyService {

    ReturnEntity selectDailyByPage(Integer startIndex, Integer pageSize, GeneralAccountDaily daily);

}
