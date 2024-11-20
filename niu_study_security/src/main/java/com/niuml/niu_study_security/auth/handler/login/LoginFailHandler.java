package com.niuml.niu_study_security.auth.handler.login;

import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import com.niuml.niu_study_security.common.utils.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * AbstractAuthenticationProcessingFilter抛出AuthenticationException异常后，会跑到这里来
 */
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    String errorMessage = exception.getMessage();
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    PrintWriter writer = response.getWriter();
    Result responseData = ResultBuilder.aResult()
        .data(null)
        .code("login.fail")
        .msg(errorMessage)
        .build();
    writer.print(JSON.stringify(responseData));
    writer.flush();
    writer.close();
  }
}
