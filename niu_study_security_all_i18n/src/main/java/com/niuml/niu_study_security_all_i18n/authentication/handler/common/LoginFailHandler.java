package com.niuml.niu_study_security_all_i18n.authentication.handler.common;

import com.niuml.niu_study_security_all_i18n.common.model.Result;
import com.niuml.niu_study_security_all_i18n.common.model.ResultBuilder;
import com.niuml.niu_study_security_all_i18n.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.niuml.niu_study_security_all_i18n.common.codes.ExceptionCode.AUTHORIZE_ERROR;


/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:15:38
 * 登录失败处理器
 */
@Log4j2
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登录失败: {}", exception.getMessage());
        Result responseData = ResultBuilder.initResult()
                .data(null)
                .code(AUTHORIZE_ERROR)
                .msg(exception.getMessage())
                .build();
        ResponseUtil.send(response,responseData);
    }
}
