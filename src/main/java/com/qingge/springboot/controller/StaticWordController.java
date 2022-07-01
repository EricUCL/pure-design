package com.qingge.springboot.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.ErrorEnums;
import com.qingge.springboot.entity.Food;
import com.qingge.springboot.entity.Message;
import com.qingge.springboot.service.ReportService;
import com.qingge.springboot.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/word")
public class StaticWordController {

    @Autowired
    private ReportService reportService;


    private Message publicMessage;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticWordController.class);

    /*基本信息上传*/
    @PostMapping("/saveWord")
    public String postBasicMessage(@RequestBody Map<String, String> map) {
        Message message = new Message();
        message.setCtopic(map.get("Ctopic"));
        message.setEtopic(map.get("Etopic"));
        message.setName(map.get("name"));
        publicMessage = message;
        return JSONUtil.statusToJson(ErrorEnums.E_200);
    }

    @GetMapping(value = "/updateWord")
    @ResponseBody
    public String updateWord(HttpServletResponse res) {
        String status = "";
        try {
            status = reportService.creatWordByTemplate(res);
        } catch (Exception e) {
            status = JSONUtil.statusToJson(ErrorEnums.E_500);
            e.printStackTrace();
            LOGGER.error("URL of the current request:[{}] - exception:[{}] - response:[{}]",
                    "/word/updateWord",
                    e.toString(),
                    res);
        }
        return status;
    }

    @GetMapping("/generateWord")
    public void export(HttpServletResponse response) throws Exception {
        String fileName = Constants.FILE_OUT_NAME;
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream os = response.getOutputStream();

        /************************生成自定义标签的值****************/
        String name = publicMessage.getName();
        String Ctopic = publicMessage.getCtopic();
        String Etopic = publicMessage.getEtopic();
//        String name = "publicMessage.getName();";
//        String course = "ublicMessage.getCourse();";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("name", name);
            put("Ctopic", Ctopic);
            put("Etopic", Etopic);
            put("date", "2022年 06月");
        }};

        // 模板word文件相对路径
        String wordSrcPath = Constants.FILE_TEMPLATE_PATH;
        //POIUtil.templateWrite(wordSrcPath, os, params);

        //word生成核心代码
        XWPFTemplate template = XWPFTemplate.compile(wordSrcPath).render(params);
        template.write(os);
        os.close();
        template.close();
    }

}
