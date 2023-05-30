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
        if(user == null){
            res.setStatus(500);
            res.setMsg("input user is empty");
            return tool.toJson(res);
        }

        User newUser = us.login(user.getUserName());
        if(newUser == null){
            res.setStatus(500);
            res.setMsg("user is not exist");
            return tool.toJson(res);
        }

        if(user.getPwd().equals(newUser.getPwd())){
            res.setStatus(200);
            res.setMsg(newUser.getuId());
            return tool.toJson(res);
        }else if(!user.getPwd().equals(newUser.getPwd())){
            res.setStatus(500);
            res.setMsg("password is wrong");
            return tool.toJson(res);
        }

        res.setStatus(500);
        res.setMsg("unknown");
        return tool.toJson(res);
    }

    @RequestMapping("/register")
    public String register(@RequestBody User respUser) throws JsonProcessingException {
        RespBean res = new RespBean(200, "success");
        if(respUser == null){
            res.setStatus(500);
            res.setMsg("user is null");
            return tool.toJson(res);
        }
        User user = new User(UUID.randomUUID().toString(), respUser.getUserName(), respUser.getPwd());
        User newUser = us.login(user.getUserName());
        if(newUser != null){
            res.setStatus(500);
            res.setMsg("user is exist");
            return tool.toJson(res);
        }else {
            int flag = us.register(user);
            return tool.toJson(res);
        }
    }
}
