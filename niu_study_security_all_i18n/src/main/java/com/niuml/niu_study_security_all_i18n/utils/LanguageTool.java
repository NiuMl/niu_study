package com.niuml.niu_study_security_all_i18n.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;
import java.util.Objects;

public class LanguageTool {

    /**
     * 获取用户请求的语言，在http请求中，会在Header中的Accept-Language指定语言
     * 如果不指定，则返货服务器的机器语言
     */
    public static Locale getRequestLanguage() {
        Locale language = Locale.getDefault(); // 默认获取机器语言
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String acceptLanguage = request.getHeader("Accept-Language");
            if (acceptLanguage != null) {
                language = Locale.forLanguageTag(acceptLanguage);
            }
            return language;
        } catch (Exception e) {
            return language;
        }
    }

}
