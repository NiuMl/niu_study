package com.niuml.niu_study_security_all_i18n.authentication.handler.exception;

import com.niuml.niu_study_security_all_i18n.common.model.Result;
import com.niuml.niu_study_security_all_i18n.common.model.ResultBuilder;
import com.niuml.niu_study_security_all_i18n.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 认证失败时，会执行这个方法。将失败原因告知客户端
 */
public class CustomAuthenticationExceptionHandler implements
    AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    Result r = ResultBuilder.initResult().msg("${authentication.fail:认证失败}").build();
    ResponseUtil.send(response,r);
  }
}
