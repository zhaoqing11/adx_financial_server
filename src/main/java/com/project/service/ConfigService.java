package com.project.service;

import com.project.entity.Config;
import com.project.utils.common.base.ReturnEntity;

public interface ConfigService {

    ReturnEntity selectByIdCardType(Integer idCardType);

    ReturnEntity insertSelective(Config config);

    ReturnEntity selectAll();

    ReturnEntity updateConfig(Config config);

}
