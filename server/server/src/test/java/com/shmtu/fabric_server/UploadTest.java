package com.shmtu.fabric_server;

import com.shmtu.fabric_server.Controller.PictureUpload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest(classes = FabricServerApplication.class)
public class UploadTest {
    @Autowired
    PictureUpload pu;

    @Test
    public void testFileNull() throws IOException, InterruptedException {
        String res = pu.upload(null, "1");
        Assertions.assertEquals("{\"status\":500,\"msg\":\"file is empty\"}", res);
        System.out.println(res);
    }

    @Test
    public void testUIDNull() throws IOException, InterruptedException {
        File file = new File("/Users/mete0r/Desktop/sys_test/high/2b68d92afd0d8de51030379245.jpg");
        MultipartFile mf = new MockMultipartFile("f", file.getName(), null, new FileInputStream(file));
        String res = pu.upload(mf, null);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"id is empty\"}", res);
        System.out.println(res);
    }

    @Test
    public void testUploadSuccess() throws IOException, InterruptedException {
        File file = new File("/Users/mete0r/Desktop/sys_test/high/2b68d92afd0d8de51030379245.jpg");
        MultipartFile mf = new MockMultipartFile("f", file.getName(), null, new FileInputStream(file));
        String res = pu.upload(mf, "1");
        System.out.println(res);
    }
}
