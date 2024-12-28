package com.niuml.niu_study_security.auth.handler.exception;

import com.niuml.niu_study_security.common.exception.BaseException;
import com.niuml.niu_study_security.common.exception.InterAuthenticationException;
import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import com.niuml.niu_study_security.common.utils.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 */
@Log4j2
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {



    private void sendMsg(String result,HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        writer.write(JSON.stringify(result));
        writer.flush();
        writer.close();
    }

    /**
     * 这还没到controller层，所以RestControllerAdvice是无效的
     */
    private void handlerException(Exception e,HttpServletResponse response) {
        if (e instanceof BaseException be ) {
            Result result = ResultBuilder.aResult()
                    .msg(be.getMessage())
                    .code(be.getCode())
                    .build();
            response.setStatus(be.getHttpStatus().value());
            sendMsg(JSON.stringify(result),response);
        }else if (e instanceof AuthenticationException | e instanceof AccessDeniedException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            sendMsg(JSON.stringify(Result.fail(e.getMessage())),response);
        } else if (e instanceof ExpiredJwtException eje) {
            Result result = ResultBuilder.aResult()
                    .msg("token 失效")
                    .code("token.expired")
                    .build();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            sendMsg(JSON.stringify(result),response);
        } else if (e instanceof MalformedJwtException) {
            Result result = ResultBuilder.aResult()
                    .msg("解析token失败")
                    .code("token.invalid")
                    .build();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            sendMsg(JSON.stringify(result),response);
        } else if (e instanceof InterAuthenticationException) {
            Result result = ResultBuilder.aResult()
                    .msg("接口级别鉴权受限")
                    .code("inter.invalid")
                    .build();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            sendMsg(JSON.stringify(result),response);
        } else{
            // 未知异常
            Result result = ResultBuilder.aResult()
                    .msg("System Error")
                    .code("system.error")
                    .build();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            sendMsg(JSON.stringify(result),response);
        }
        // 自定义异常
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerException(e,response);
        }
    }
}
