package com.shmtu.fabric_server.Mapper;

import com.shmtu.fabric_server.Pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User login(@Param("uId") String uId);
    int register(User user);
}
