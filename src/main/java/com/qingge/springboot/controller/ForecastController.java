package com.qingge.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qingge.springboot.entity.Food;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

@RestController
//@RequestMapping("/forecast")
public class ForecastController {


    @PostMapping("/forecast")
    public Food forecast(@RequestBody Map<String, String> params) throws InterruptedException {

        //String productionArea = params.get("productionArea");
        String selectProv = params.get("selectProv");
        Food food = new Food();
        if (selectProv.equals("西藏自治区")) {
            food.setQualified("不合格");
        } else food.setQualified("合格");
        Thread.sleep(1000);
        return food;
    }

    private Object remoteCall(String content){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        String str = jsonObject.toJSONString();
        // 访问服务进程的套接字
        Socket socket = null;
        //List<Question> questions = new ArrayList<>();

        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket("localhost",5891);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while((tmp=br.readLine())!=null)
                sb.append(tmp).append('\n');
            // 解析结果
            JSONArray res = JSON.parseArray(sb.toString());

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {if(socket!=null) socket.close();} catch (IOException e) {}
        }
        return null;
    }
}
