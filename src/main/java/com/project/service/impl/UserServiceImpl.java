package com.project.service.impl;

import com.project.entity.User;
import com.project.entity.base.UserEnum;
import com.project.mapper.master.UserMapper;
import com.project.service.UserService;
import com.project.utils.BeanToMapUtil;
import com.project.utils.EncryptionUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.auth.SnowFlakeIdWorker;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import com.project.utils.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private ReturnEntity returnEntity;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Value("${project.tokenExpire}")
    Integer tokenExpire;

    @Override
    public ReturnEntity updatePassword(Integer idUser, String oldPassword, String newPassword) {
        try {
            User user = userMapper.selectByPrimaryKey(idUser);
            if (!EncryptionUtil.match(oldPassword, user.getPassword())) {
                return ReturnUtil.validError(HttpCode.CODE_500, "原密码输入错误");
            }
            user.setPassword(EncryptionUtil.encrypt(newPassword));
            int count = userMapper.updateSelective(user);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改用户密码失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity login(User usr) {
        try {
            User user = userMapper.selectUserByUserName(usr.getUserName());
            if (user == null || null == user.getPassword()) {
                // 抛出用户不存在异常
                return ReturnUtil.validError(HttpCode.CODE_500, "用户不存在！");
            }
            if (user.getEnable() == false) {
                // 抛出用户已被禁用异常
                return ReturnUtil.validError(HttpCode.CODE_500, "用户已被禁用，请联系管理员");
            }

            // 若密码不匹配
            if (!EncryptionUtil.match(usr.getPassword(), user.getPassword())) {
                // 抛出密码错误异常
                return ReturnUtil.validError(HttpCode.CODE_500, "请输入正确的账号或密码");
            }
            returnEntity = assembleResponseBody(user);
        } catch (Exception e) {
            logger.error("登录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    private ReturnEntity assembleResponseBody(User user) {
        if (user.getEnable() != true) {
            return ReturnUtil.validError(HttpCode.CODE_403, "对不起您的账号已被禁用，请联系平台管理员！");
        }
        Map<String, Object> cacheUserInfo = new HashMap<String, Object>();

        cacheUserInfo.put(UserEnum.CACHE_USERACCOUNT, user.getUserName());
        cacheUserInfo.put(UserEnum.CACHE_USERDISABLE, user.getEnable());
        cacheUserInfo.put(UserEnum.CACHE_USERID, user.getIdUser());
        SnowFlakeIdWorker idWorker = new SnowFlakeIdWorker(1, 1);
        String tokenId = Long.toString(idWorker.nextId());
        tokenId = EncryptionUtil.encrypt(tokenId);
        redisUtil.set(tokenId, cacheUserInfo, tokenExpire); // 0
        // 返回前端 密码置空
        user.setPassword(null);
        Map<String, Object> userMap = BeanToMapUtil.beanToMap(user);
        userMap.put("accessToken", tokenId);
        // 返回登录的用户信息
        return ReturnUtil.success(userMap);
    }

    @Override
    public ReturnEntity logout(String userId, String token) {
        redisUtil.del(token);
        redisUtil.del(userId);
        return ReturnUtil.success();
    }

    @Override
    public ReturnEntity deleteSelective(Integer idUser) {
        try {
            int count = userMapper.deleteSelective(idUser);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateSelective(User user) {
        try {
            int count = userMapper.updateSelective(user);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改用户失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity insertSelective(User user) {
        try {
            if (Tools.notEmpty(user.getPassword())) {
                user.setPassword(EncryptionUtil.encrypt(user.getPassword()));
            }
            int count = userMapper.insertSelective(user);
            if (count > 0) {
                returnEntity = ReturnUtil.success("创建成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "创建失败");
            }
        } catch (Exception e) {
            logger.error("创建用户失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPrimaryKey(Integer idUser) {
        try {
            User user = userMapper.selectByPrimaryKey(idUser);
            returnEntity = ReturnUtil.success(user);
        } catch (Exception e) {
            logger.error("根据用户id查询用户失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, User user) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = userMapper.selectByPageTotal(user);
            PageBean<User> pageBean = new PageBean<User>(startIndex, pageSize, total);
            List<User> userList = userMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(),
                    user);
            pageBean.setList(userList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询用户列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
