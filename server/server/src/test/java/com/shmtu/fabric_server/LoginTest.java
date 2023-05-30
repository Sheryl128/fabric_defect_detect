package com.shmtu.fabric_server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shmtu.fabric_server.Controller.UserController;
import com.shmtu.fabric_server.Pojo.User;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringBootTest(classes = FabricServerApplication.class)
public class LoginTest {

    @Autowired
    UserController uc;
    // 测试传入的值为空的用例
    @Test
    public void testLoginContentNull() throws JsonProcessingException {
        String res = uc.login(null);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"input user is empty\"}", res);
        System.out.println(res);
    }

    // 测试用户不存在
    @Test
    public void testLoginUserNull() throws JsonProcessingException {
        User user = new User("1123", "xyc123", "19991208");
        String res = uc.login(user);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"user is not exist\"}", res);
        System.out.println(res);
    }

    // 测试密码不正确
    @Test
    public void testLoginPwdErr() throws JsonProcessingException {
        User user = new User("1", "xyc", "19991208");
        User user2 = new User("2", "xyc2", "dfasdasasdas");
        String res = uc.login(user);
        String res2 = uc.login(user2);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"password is wrong\"}", res);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"password is wrong\"}", res2);
        System.out.println(res);
        System.out.println(res2);
    }

    // 测试登录成功
    @Test
    public void testLoginSuccess() throws JsonProcessingException {
        User user = new User("1", "xyc", "123");
        User user2 = new User("2", "xyc2", "xycxyc2023");
        String res = uc.login(user);
        String res2 = uc.login(user2);
        Assertions.assertEquals("{\"status\":200,\"msg\":\"1\"}", res);
        Assertions.assertEquals("{\"status\":200,\"msg\":\"2\"}", res2);
        System.out.println(res);
        System.out.println(res2);
    }

}
