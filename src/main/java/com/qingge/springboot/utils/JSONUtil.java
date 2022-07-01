package com.qingge.springboot.utils;

import com.alibaba.fastjson.JSONObject;
import com.qingge.springboot.common.ErrorEnums;

public class JSONUtil {
    public static String statusToJson(ErrorEnums errorEnum){
        JSONObject result = new JSONObject();
        result.put("code", errorEnum.getCode());
        result.put("msg", errorEnum.getMsg());

        return result.toJSONString();
    }
}
