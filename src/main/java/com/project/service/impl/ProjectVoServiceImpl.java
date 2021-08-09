package com.project.service.impl;

import com.project.service.ProjectVoService;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class ProjectVoServiceImpl implements ProjectVoService {

    private Logger logger = LogManager.getLogger(ProjectVoServiceImpl.class);

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity queryProjectByName(Integer idProject, String projectName) {
        try {
//            ProjectVO projectVO = projectMapper.queryProjectByName(idProject, projectName);
//            returnEntity = ReturnUtil.success(projectVO);
        } catch (Exception e) {
          logger.error("根据项目名称查询项目信息失败，错误消息：--->" + e.getMessage());
          throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
