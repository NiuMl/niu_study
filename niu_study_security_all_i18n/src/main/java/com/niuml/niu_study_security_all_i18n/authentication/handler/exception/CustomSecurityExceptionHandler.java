package com.niuml.niu_study_security_all_i18n.authentication.handler.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.niuml.niu_study_security_all_i18n.common.exceptions.BaseException;
import com.niuml.niu_study_security_all_i18n.common.model.Result;
import com.niuml.niu_study_security_all_i18n.common.model.ResultBuilder;
import com.niuml.niu_study_security_all_i18n.utils.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.niuml.niu_study_security_all_i18n.common.codes.ExceptionCode.*;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 */
@Log4j2
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {



    /**
     * 这还没到controller层，所以RestControllerAdvice是无效的
     */
    private void handlerException(Exception e,HttpServletResponse response) {
        if (e instanceof BaseException be ) {
            Result result = ResultBuilder.initResult()
                    .msg(be.getMessage())
                    .code(be.getCode())
                    .build();
            response.setStatus(200);
            ResponseUtil.send(response,result);
        }else if (e instanceof AuthenticationException | e instanceof AccessDeniedException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ResponseUtil.send(response,Result.fail(e.getMessage()));
        } else if (e instanceof TokenExpiredException eje) {
            Result result = ResultBuilder.initResult()
                    .msg("${token.lose:token已过期}")
                    .code(TOKEN_LOSE)
                    .build();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            ResponseUtil.send(response,result);
        } else if (e instanceof JWTDecodeException) {
            Result result = ResultBuilder.initResult()
                    .msg("${token.analysis.error:解析token失败}")
                    .code(TOKEN_ANALYSIS_ERROR)
                    .build();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            ResponseUtil.send(response,result);
//        }
//        else if (e instanceof InterAuthenticationException) {
//            Result result = ResultBuilder.initResult()
//                    .msg("接口级别鉴权受限")
//                    .code("inter.invalid")
//                    .build();
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            sendMsg(JSON.stringify(result),response);
        } else{
            e.printStackTrace();
            // 未知异常
            Result result = ResultBuilder.initResult()
                    .msg("System Error")
                    .code(SYSTEM_ERROR)
                    .build();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ResponseUtil.send(response,result);
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
