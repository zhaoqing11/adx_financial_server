package com.project.service.impl;

import com.project.entity.User;
import com.project.entity.base.UserEnum;
import com.project.mapper.master.UserMapper;
import com.project.service.UserService;
import com.project.utils.BeanToMapUtil;
import com.project.utils.EncryptionUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.auth.SnowFlakeIdWorker;
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
import java.util.Map;

@Service
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

    private Logger logger = LogManager.getLogger(ProjectVoServiceImpl.class);

    @Autowired
    private ReturnEntity returnEntity;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Value("${project.tokenExpire}")
    Integer tokenExpire;

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
//        redisUtil.set(tokenId, cacheUserInfo, tokenExpire); // 0
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

}
