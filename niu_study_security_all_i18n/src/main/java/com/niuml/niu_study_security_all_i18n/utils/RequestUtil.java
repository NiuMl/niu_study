package com.niuml.niu_study_security_all_i18n.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:11:06
 */
@Log4j2
public class RequestUtil {

    public static Map<String,Object> req2Map(HttpServletRequest request) throws IOException {
        // 提取请求数据
        String requestJsonData = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        log.info("Request Json Data:{}",requestJsonData);
        return JsonUtil.parseToMap(requestJsonData);
    }
}
