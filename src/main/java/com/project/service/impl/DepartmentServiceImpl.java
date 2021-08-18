package com.project.service.impl;

import com.project.entity.Department;
import com.project.mapper.master.DepartmentMapper;
import com.project.service.DepartmentService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class DepartmentServiceImpl implements DepartmentService {

    private static Logger logger = LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectAll() {
        try {
            List<Department> departmentList = departmentMapper.selectAll();
            returnEntity = ReturnUtil.success(departmentList);
        } catch (Exception e) {
            logger.error("获取部门列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
