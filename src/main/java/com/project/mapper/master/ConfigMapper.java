package com.project.mapper.master;

import com.project.entity.Config;

public interface ConfigMapper {

    int updateConfig(Config config);

    Config selectConfigInfo();

}
