package com.shmtu.fabric_server;

import com.shmtu.fabric_server.Controller.PictureUpload;
import com.shmtu.fabric_server.Pojo.UserHistory;
import com.shmtu.fabric_server.util.Util;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@SpringBootTest(classes = FabricServerApplication.class)
public class TestTestDetect {

    @Autowired
    PictureUpload pu;

    @Test
    public void testFileNameNull() throws IOException, InterruptedException {
        UserHistory uh = new UserHistory("123", "123", new Date(), "123", "123", null);
        String res = pu.generate(new Response(), "1", uh);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"filename is empty\"}", res);
        System.out.println(res);
    }

    @Test
    public void testTestDetectSuccess() throws IOException, InterruptedException {
        UserHistory uh = new UserHistory("123", "123", new Date(), "123", "123", "123");
        String json = new Util().toJson(uh);
        URL url = new URL("http://localhost:8081/picture/testdetect");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(json.getBytes(StandardCharsets.UTF_8));

        ResponseFacade response = new ResponseFacade(new Response());
        connection.connect();
        if (connection.getResponseCode() == 200) {
            //获取返回的数据
            InputStream is = connection.getInputStream();

            if (null != is) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096 * 4096];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }
                byte[] data = os.toByteArray();
                File imageFile = new File("/Users/mete0r/Desktop/testdetect.png");
                //创建输出流
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                //写入数据
                fileOutputStream.write(data);
                is.close();
                os.close();
            }
        }
    }

}
