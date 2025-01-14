package com.niuml.niu_study_security_all_i18n.authentication.handler.exception;

import com.niuml.niu_study_security_all_i18n.common.model.Result;
import com.niuml.niu_study_security_all_i18n.common.model.ResultBuilder;
import com.niuml.niu_study_security_all_i18n.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证成功(Authentication), 但无权访问时。会执行这个方法
 * 或者SpringSecurity框架捕捉到  AccessDeniedException时，会转出
 */
@Component
public class CustomAuthorizationExceptionHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    Result r = ResultBuilder.initResult().msg("${low.power:无权访问}").build();
    ResponseUtil.send(response,r);
  }
}
