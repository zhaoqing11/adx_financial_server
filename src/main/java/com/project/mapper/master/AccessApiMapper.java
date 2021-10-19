package com.project.mapper.master;

import com.project.entity.AccessApi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccessApiMapper {

    List<AccessApi> getAuthByRole(@Param("idRole") Integer idRole);

}
