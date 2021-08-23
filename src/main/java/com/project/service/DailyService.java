package com.project.service;

import com.project.entity.PrivateDaily;
import com.project.entity.PublicDaily;
import com.project.utils.common.base.ReturnEntity;

public interface DailyService {

    ReturnEntity queryPrivateDailyByDate(String date);

    ReturnEntity queryPublicDailyByDate(String date);

    ReturnEntity updatePublicDaily(PublicDaily daily);

    ReturnEntity updatePrivateDaily(PrivateDaily daily);

    ReturnEntity insertPublicDaily(PublicDaily daily);

    ReturnEntity selectPublicDailyByPage(Integer startIndex, Integer pageSize, PublicDaily daily);

    ReturnEntity selectPrivateDailyByPage(Integer startIndex, Integer pageSize, PrivateDaily daily);

}
