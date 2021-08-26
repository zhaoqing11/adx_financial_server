package com.project.mapper.master;

import com.project.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User selectUserByUserName(String userName);

    int deleteSelective(@Param("idUser") Integer idUser);

    int updateSelective(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(@Param("idUser") Integer idUser);

    int selectByPageTotal(@Param("user") User user);

    List<User> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                            @Param("user") User user);

}
