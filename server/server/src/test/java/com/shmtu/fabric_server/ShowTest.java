package com.shmtu.fabric_server;

import com.shmtu.fabric_server.Controller.PictureUpload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = FabricServerApplication.class)
public class ShowTest {
    @Autowired
    PictureUpload pu;

    @Test
    public void testHistoryNull() throws IOException, InterruptedException {
        String res = pu.showHistory( null);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"user id is empty\"}", res);
        System.out.println(res);
    }

    @Test
    public void testSearchSuccess() throws IOException, InterruptedException {
        String res = pu.showHistory( "1");
        System.out.println(res);
    }
}
