package com.project.service;

import com.project.entity.SecondGeneralAccountDaily;
import com.project.utils.common.base.ReturnEntity;

public interface SecondGeneralAccountDailyService {

    ReturnEntity selectDailyByPage(Integer startIndex, Integer pageSize, SecondGeneralAccountDaily daily);

}
