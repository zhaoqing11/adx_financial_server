package com.project.mapper.master;

import com.project.entity.User;

public interface UserMapper {

    User selectUserByUserName(String userName);

}
