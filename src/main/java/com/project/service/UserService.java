package com.project.service;

import com.project.entity.User;
import com.project.utils.common.base.ReturnEntity;

public interface UserService {

    ReturnEntity login(User user);

    ReturnEntity logout(String userId, String token);
}
