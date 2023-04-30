package com.shmtu.fabric_server.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shmtu.fabric_server.Pojo.RespBean;
import com.shmtu.fabric_server.Pojo.User;
import com.shmtu.fabric_server.Pojo.UserHistory;
import com.shmtu.fabric_server.Service.HistoryService;
import com.shmtu.fabric_server.util.Util;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.util.RequestUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RestController
@RequestMapping("/picture")
public class PictureUpload {

    Util tool = new Util();

    @Autowired
    HistoryService hs;

    @RequestMapping("/upload/{uId}")
    @ResponseBody
    public String upload(@RequestParam("pic") MultipartFile file, @PathVariable("uId") String username) throws IOException, InterruptedException {
        String fileName = file.getOriginalFilename();
        String newFileName = username + UUID.randomUUID() + fileName;
        String inputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/" + newFileName;
        String outputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/" + newFileName;

        File dest = new File(inputPath);
        file.transferTo(dest);
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String urlNameString = "http://127.0.0.1:5000/low2high" + "/" + newFileName + "/" + newFileName;
            HttpGet get = new HttpGet(urlNameString);
            CloseableHttpResponse response = httpClient.execute(get);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 这里需要更新数据库
        String hId = UUID.randomUUID().toString();
        UserHistory uh = new UserHistory(hId, username, new Date(), newFileName, newFileName, fileName);
        hs.setHistory(uh);
        return tool.toJson(new RespBean(200, "success"));
    }

    @RequestMapping("/download/{hid}")
    public String download(HttpServletResponse response, @PathVariable String hid) throws IOException, InterruptedException {
        // 根据hid反查数据库获取地址
        String outPath = hs.getHistoryByHid(hid).getOutPath();
        // 地址拼接
        String outputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/" + outPath;

        try {
            // 上传
            FileInputStream fileInputStream = new FileInputStream(outputPath);
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");

            int len = 0;
            byte[] bytes = new byte[3000 * 3000];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return tool.toJson(new RespBean(200, "success"));
    }

    @RequestMapping("/show/{uid}")
    public String showHistory(@PathVariable String uid) throws IOException, InterruptedException {
        List<UserHistory> list = hs.getHistory(uid);
        return tool.toJson(list);
    }
}
