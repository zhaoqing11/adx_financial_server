package com.project.service;

import com.project.utils.common.base.ReturnEntity;

public interface RoleService {

    ReturnEntity getAuthByRole(Integer idRole);

    ReturnEntity selectAll();

}
