package com.project.service;

import com.project.utils.common.base.ReturnEntity;

public interface ProjectVoService {

    ReturnEntity queryProjectByName(Integer idProject, String projectName);

}
