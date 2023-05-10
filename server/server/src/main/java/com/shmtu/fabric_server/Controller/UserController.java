package com.shmtu.fabric_server.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shmtu.fabric_server.Pojo.RespBean;
import com.shmtu.fabric_server.Pojo.User;
import com.shmtu.fabric_server.Service.UserService;
import com.shmtu.fabric_server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    Util tool = new Util();
    @Autowired
    UserService us;

    @RequestMapping("/login")
    public String login(@RequestBody User user) throws JsonProcessingException {
        RespBean res = new RespBean();
        try {
            User newUser = us.login(user.getUserName());
            if(user.getPwd().equals(newUser.getPwd())){
                res.setStatus(200);
                res.setMsg(newUser.getuId());
                return tool.toJson(res);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        res.setStatus(500);
        res.setMsg("error");
        return tool.toJson(res);
    }

    @RequestMapping("/register")
    public String register(@RequestBody User respUser) throws JsonProcessingException {
        User user = new User(UUID.randomUUID().toString(), respUser.getUserName(), respUser.getPwd());
        System.out.println(user.getuId());
        us.register(user);
        RespBean res = new RespBean(200, "success");
        return tool.toJson(res);
    }
}
