package com.shmtu.fabric_server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shmtu.fabric_server.Pojo.RespBean;
import com.shmtu.fabric_server.Pojo.User;
import com.shmtu.fabric_server.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FabricServerApplication.class)
public class TestJsonUtil {

    @Test
    public void test2Json() throws JsonProcessingException {
        User user = new User("123456", "testjson", "123456789");
        RespBean respBean = new RespBean(200, "testjson");
        Util util = new Util();
        String json = util.toJson(user);
        String json2 = util.toJson(respBean);
        Assertions.assertEquals("{\"uId\":\"123456\",\"userName\":\"testjson\",\"pwd\":\"123456789\"}", json);
        System.out.println(json);
        System.out.println(json2);
    }
    @Test
    public void testInt2Json() throws JsonProcessingException {
        Integer i  = 3;
        Util util = new Util();
        String json = util.toJson(i);
        Assertions.assertEquals("3", json);
        System.out.println(json);
    }

    @Test
    public void testString2Json() throws JsonProcessingException {
        String str = "hello";
        Util util = new Util();
        String json = util.toJson(str);
        Assertions.assertEquals("\"hello\"", json);
        System.out.println(json);
    }
}
