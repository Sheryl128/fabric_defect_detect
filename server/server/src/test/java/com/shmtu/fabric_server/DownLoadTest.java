package com.shmtu.fabric_server;

import com.shmtu.fabric_server.Controller.PictureUpload;
import com.shmtu.fabric_server.Pojo.RespBean;
import com.shmtu.fabric_server.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest(classes = FabricServerApplication.class)
public class DownLoadTest {
    @Autowired
    PictureUpload pu;

    @Test
    public void testHidNull() throws IOException, InterruptedException {
        String res = pu.download(new Response(), null);
        Assertions.assertEquals("{\"status\":500,\"msg\":\"history id is empty\"}", res);
        System.out.println(res);
    }

    @Test
    public void testDownloadSuccess() throws IOException, InterruptedException {
        String hid = "6568b36f-40d2-4ac0-a03a-646307a35c43";
        URL url = new URL("http://localhost:8081/picture/download/" + hid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                File imageFile = new File("/Users/mete0r/Desktop/test.jpg");
                //创建输出流
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                //写入数据
                fileOutputStream.write(data);
                is.close();
                os.close();
            }
        }
    }


    @Test
    public void testDownloadManyPic() throws IOException, InterruptedException {
        for(int i = 0; i < 10000; i++){
            String hid = "6568b36f-40d2-4ac0-a03a-646307a35c43";
            URL url = new URL("http://localhost:8081/picture/download/" + hid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            ResponseFacade response = new ResponseFacade(new Response());
            connection.connect();
            StringBuffer result = new StringBuffer();
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
                    is.close();
                    os.close();
                }
            }
        }
    }
}
