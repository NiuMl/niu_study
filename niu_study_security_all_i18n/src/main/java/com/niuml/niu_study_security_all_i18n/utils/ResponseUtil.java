package com.niuml.niu_study_security_all_i18n.utils;

import com.niuml.niu_study_security_all_i18n.common.model.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/***
 * @author niumengliang
 * Date:2024/12/10
 * Time:15:00
 * 通过使用HttpServletResponse对象，将数据返回给客户端
 * 为什么要用这个，是因为有些操作还没有到springmvc里面，有些异常拦截是拦截不到的，使用的是过滤器
 */
@Log4j2
public class ResponseUtil {

    private static final String CONTENT_TYPE = "application/json;charset=utf-8";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public static void send(HttpServletResponse response, String msg) {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            response.getWriter().write(msg);
        } catch (IOException e) {
            log.info("ResponseUtil send error: " + e.getMessage());
        }
    }
    public static void send(HttpServletResponse response, Result result) {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            response.getWriter().write(JsonUtil.toJson(result));
        } catch (IOException e) {
            log.info("ResponseUtil send error: " + e.getMessage());
        }
    }
}
