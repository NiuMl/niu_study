package com.niuml.niu_study_security_all_i18n.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/***
 * @author niumengliang
 * Date:2024/12/10
 * Time:15:02
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> parseToMap(String json) {
        return parse(json, HashMap.class);
    }

    public static <T> T parse(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, TypeFactory.defaultInstance().constructType(targetType));
        } catch (IOException e) {
            throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + json, e);
        }
    }
    public static <T> T parse(String json, TypeReference<T> typeReference) {
        if (json != null && !json.isEmpty()) {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
    public static <T> T parse(String json, Type targetType) {
        try {
            return objectMapper.readValue(json, TypeFactory.defaultInstance().constructType(targetType));
        } catch (IOException e) {
            throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + json, e);
        }
    }
}
