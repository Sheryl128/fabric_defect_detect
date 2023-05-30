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
import org.python.jline.console.history.History;
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
    public String upload(@RequestParam("pic") MultipartFile file, @PathVariable("uId") String uid) throws IOException, InterruptedException {
        if(file == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("file is empty");
            return tool.toJson(res);
        }
        if(uid == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("id is empty");
            return tool.toJson(res);
        }
        System.out.println("=====success=====");
        String fileName = file.getOriginalFilename();
        String newFileName = uid + UUID.randomUUID() + fileName;
        String inputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/" + newFileName;
        String outputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/" + newFileName;
        System.out.println(inputPath);
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
        UserHistory uh = new UserHistory(hId, uid, new Date(), newFileName, newFileName, fileName);
        hs.setHistory(uh);
        return tool.toJson(new RespBean(200, "success"));
    }

    @RequestMapping("/download/{hid}")
    public String download(HttpServletResponse response, @PathVariable String hid) throws IOException, InterruptedException {
        if(hid == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("history id is empty");
            return tool.toJson(res);
        }
        // 根据hid反查数据库获取地址
        UserHistory h = new UserHistory();
        h = hs.getHistoryByHid(hid);
        if(h == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("file is not exist");
            return tool.toJson(res);
        }
        String outPath = h.getOutPath();
        String fileName = h.getFileName();
        // 地址拼接
        String outputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/" + outPath;

        try {
            // 上传
            FileInputStream fileInputStream = new FileInputStream(outputPath);
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[4096 * 4096];
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
        return null;
    }

    @RequestMapping("/show/{uid}")
    public String showHistory(@PathVariable String uid) throws IOException, InterruptedException {
        if(uid == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("user id is empty");
            return tool.toJson(res);
        }
        List<UserHistory> list = hs.getHistory(uid);
        return tool.toJson(list);
    }

    @RequestMapping("/generate/{uid}")
    public String generate(HttpServletResponse response, @PathVariable String uid, @RequestBody UserHistory uh) throws IOException, InterruptedException {
        if(uh.getFileName() == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("filename is empty");
            return tool.toJson(res);
        }

        String inputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/" + uh.getFileName() + ".png";

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String urlNameString = "http://127.0.0.1:5000/gen" + "/" + uh.getFileName();
            HttpGet get = new HttpGet(urlNameString);
            CloseableHttpResponse r = httpClient.execute(get);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(inputPath);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");

            int len = 0;
            byte[] bytes = new byte[4096 * 4096];
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
        return null;
    }

    @RequestMapping("/testdetect")
    public String testDetect(HttpServletResponse response, @RequestBody UserHistory uh) throws IOException, InterruptedException {
        if(uh.getFileName() == null){
            RespBean res = new RespBean();
            res.setStatus(500);
            res.setMsg("filename is empty");
            return tool.toJson(res);
        }

        String inputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/" + uh.getFileName() + ".png";
        String outputPath = "/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/" + uh.getFileName() + ".png";
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String urlNameString = "http://127.0.0.1:5000/low2high" + "/" + uh.getFileName() + ".png" + "/" + uh.getFileName() + ".png";
            HttpGet get = new HttpGet(urlNameString);
            CloseableHttpResponse r = httpClient.execute(get);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(outputPath);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");

            int len = 0;
            byte[] bytes = new byte[4096 * 4096];
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
        File f1 = new File(inputPath);
        f1.delete();
        File f2 = new File(outputPath);
        f2.delete();
        return null;
    }
}
