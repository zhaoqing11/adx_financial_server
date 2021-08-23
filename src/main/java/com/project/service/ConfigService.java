package com.project.service;

import com.project.entity.Config;
import com.project.utils.common.base.ReturnEntity;

public interface ConfigService {

    ReturnEntity selectAll();

    ReturnEntity updateConfig(Config config);

}
