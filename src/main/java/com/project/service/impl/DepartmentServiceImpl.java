package com.project.service.impl;

import com.project.entity.Department;
import com.project.mapper.master.DepartmentMapper;
import com.project.service.DepartmentService;
import com.project.utils.ReturnUtil;
import com.project.utils.common.base.HttpCode;
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
    public ReturnEntity insertSelective(Department department) {
        try {
            int count = departmentMapper.insertSelective(department);
            if (count > 0) {
                returnEntity = ReturnUtil.success("创建成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "创建失败");
            }
        } catch (Exception e) {
            logger.error("创建部门失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateSelective(Department department) {
        try {
            int count = departmentMapper.updateSelective(department);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改部门失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idDepartment) {
        try {
            int count = departmentMapper.deleteSelective(idDepartment);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除部门失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

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
