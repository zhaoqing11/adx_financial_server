package com.project.service.impl;

import com.project.entity.AccessApi;
import com.project.entity.Role;
import com.project.mapper.master.AccessApiMapper;
import com.project.mapper.master.RoleMapper;
import com.project.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private static Logger logger = LogManager.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AccessApiMapper accessApiMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity getAuthByRole(Integer idRole) {
        try {
            List<AccessApi> apiList = accessApiMapper.getAuthByRole(idRole);
            returnEntity = ReturnUtil.success(apiList);
        } catch (Exception e) {
            logger.error("获取角色权限列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectAll() {
        try {
            List<Role> roleList = roleMapper.selectAll();
            returnEntity = ReturnUtil.success(roleList);
        } catch (Exception e) {
            logger.error("获取角色列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
