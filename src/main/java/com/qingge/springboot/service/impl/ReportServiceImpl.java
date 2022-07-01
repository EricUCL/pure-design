package com.qingge.springboot.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.ErrorEnums;
import com.qingge.springboot.entity.Message;
import com.qingge.springboot.service.ReportService;
import com.qingge.springboot.utils.JSONUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Service("testUserService")
public class ReportServiceImpl implements ReportService {

    private Message publicMessage;

    @Override
    public String creatWordByTemplate(HttpServletResponse res) throws Exception {
        String fileName = Constants.FILE_OUT_NAME;
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream os = res.getOutputStream();

        /************************生成自定义标签的值****************/
        String name = publicMessage.getName();
//        String name = "publicMessage.getName();";
//        String course = "ublicMessage.getCourse();";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("name", name);
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

        return JSONUtil.statusToJson(ErrorEnums.E_200);
    }

    public void getMessage(Message message) {
        publicMessage = message;
    }

}
