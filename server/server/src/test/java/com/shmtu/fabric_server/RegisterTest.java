package com.shmtu.fabric_server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shmtu.fabric_server.Controller.UserController;
import com.shmtu.fabric_server.Pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FabricServerApplication.class)
public class RegisterTest {

    @Autowired
    UserController uc;
    // 测试输入内容为空的情况
    @Test
    public void testContentNull() throws JsonProcessingException {
        String res = uc.register(null);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"user is null\"}", res);
        System.out.println(res);
    }

    // 测试用户已存在
    @Test
    public void testUserExist() throws JsonProcessingException {
        User user = new User("1", "xyc", "fsdasdaafads");
        String res = uc.register(user);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"user is exist\"}", res);
        System.out.println(res);
    }

    // 测试用户注册成功
    @Test
    public void testRegisterSuccess() throws JsonProcessingException {
        User user = new User("1234", "xyc1234", "19991208");
        User user2 = new User("12345", "xyc12345", "dasdasdas");
        String res = uc.register(user);
        String res2 = uc.register(user2);
        Assertions.assertEquals("{\"status\":200,\"msg\":\"success\"}", res);
        System.out.println(res);
        System.out.println(res2);
    }
}
