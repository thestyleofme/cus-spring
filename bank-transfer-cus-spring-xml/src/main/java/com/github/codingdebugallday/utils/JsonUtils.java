package com.github.codingdebugallday.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * JSON工具类（使用的是jackson实现的）
 * </p>
 *
 * @author isaac 2020/9/5 2:46
 * @since 1.0.0
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     *
     * @param data Object
     * @return String
     */
    public static String object2Json(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return T
     */
    public static <T> T json2Pojo(String jsonData, Class<T> beanType) throws JsonProcessingException {
        return MAPPER.readValue(jsonData, beanType);
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param jsonData jsonData
     * @param beanType Class<T>
     * @return T
     */
    public static <T> List<T> json2List(String jsonData, Class<T> beanType) throws JsonProcessingException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        return MAPPER.readValue(jsonData, javaType);
    }

}
