package com.ces.Village.utils;

import com.alibaba.fastjson2.JSONObject;

/**
 * Json转换工具类
 */
public class JsonConvertUtil {
    /**
     * Java对象转换成JSON字符串
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * JSON字符串转为Java对象
     * @param jsonString
     * @param objClass
     * @return
     * @param <T>
     */
    public static <T> T toJavaObject(String jsonString, Class<T> objClass) {
        return JSONObject.parseObject(jsonString, objClass);
    }
}
