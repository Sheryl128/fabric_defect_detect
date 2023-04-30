package com.shmtu.fabric_server.Service;

import com.shmtu.fabric_server.Mapper.UserMapper;
import com.shmtu.fabric_server.Pojo.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper um;

    public User login(String uId){
        return um.login(uId);
    }

    public int register(User user){
        return um.register(user);
    }
}
