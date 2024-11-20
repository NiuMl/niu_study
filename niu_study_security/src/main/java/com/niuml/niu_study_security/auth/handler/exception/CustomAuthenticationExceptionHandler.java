package com.niuml.niu_study_security.auth.handler.exception;

import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.utils.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证失败时，会执行这个方法。将失败原因告知客户端
 */
public class CustomAuthenticationExceptionHandler implements
    AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    PrintWriter writer = response.getWriter();
    writer.print(JSON.stringify(Result.fail("${authentication.fail:认证失败}")));
    writer.flush();
    writer.close();
  }
}
