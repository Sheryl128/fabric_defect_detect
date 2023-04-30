package com.shmtu.fabric_server;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class tt {

    @Test
    public void jythonTest() throws IOException {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String urlNameString = "http://127.0.0.1:5000/low2high" + "/" + "xyc4a97291a-d31c-4c1b-bc82-a303048de7961.png" + "/" + "xyc4a97291a-d31c-4c1b-bc82-a303048de7961.png";
            HttpGet get = new HttpGet(urlNameString);
            CloseableHttpResponse response = httpClient.execute(get);
            // 获取所有响应头字段
        } catch(Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
    }
    @Test
    public void ttTest() throws IOException {
        System.out.println(new Date());
    }
}
