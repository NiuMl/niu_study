package com.niuml.niu_study_security.auth.handler.resourceapi.openapi3;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OpenApi3AuthenticationFilter extends OncePerRequestFilter {


  public OpenApi3AuthenticationFilter() {
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    System.out.println("这里是默认过滤链...");
    // 随便给个默认身份
    Authentication authentication = new TestingAuthenticationToken("username", "password", "ROLE_USER");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 放行
    filterChain.doFilter(request, response);
  }
}
