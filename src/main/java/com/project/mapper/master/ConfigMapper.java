package com.project.mapper.master;

import com.project.entity.Config;
import org.apache.ibatis.annotations.Param;

public interface ConfigMapper {

    int updateConfig(Config config);

    Config selectConfigInfo(@Param("idConfig") Integer idConfig);

}
