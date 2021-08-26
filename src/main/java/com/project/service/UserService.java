package com.project.service;

import com.project.entity.User;
import com.project.utils.common.base.ReturnEntity;

public interface UserService {

    ReturnEntity updatePassword(Integer idUser, String oldPassword, String newPassword);

    ReturnEntity login(User user);

    ReturnEntity logout(String userId, String token);

    ReturnEntity deleteSelective(Integer idUser);

    ReturnEntity updateSelective(User user);

    ReturnEntity insertSelective(User user);

    ReturnEntity selectByPrimaryKey(Integer idUser);;

    ReturnEntity selectByPage(Integer startIndex, Integer pageSize,
                            User user);
}
