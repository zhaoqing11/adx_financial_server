package com.project.service;

import com.project.entity.PublicDaily;
import com.project.utils.common.base.ReturnEntity;

public interface DailyService {

    ReturnEntity updateSelective(PublicDaily daily);

    ReturnEntity insertSelective(PublicDaily daily);

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize, PublicDaily daily);

}
