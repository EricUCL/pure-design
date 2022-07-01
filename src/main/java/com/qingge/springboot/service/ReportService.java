package com.qingge.springboot.service;


import com.qingge.springboot.entity.Message;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {
    String creatWordByTemplate(HttpServletResponse res) throws Exception;
    void getMessage(Message message);
}
