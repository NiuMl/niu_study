package com.niuml.niu_study_security_all_i18n.authentication.handler.common;

import com.niuml.niu_study_security_all_i18n.common.exceptions.BaseException;
import com.niuml.niu_study_security_all_i18n.common.model.Result;
import com.niuml.niu_study_security_all_i18n.common.model.ResultBuilder;
import com.niuml.niu_study_security_all_i18n.utils.JwtUtil;
import com.niuml.niu_study_security_all_i18n.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.niuml.niu_study_security_all_i18n.common.codes.ExceptionCode.AUTHORIZE_ERROR;


/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:15:42
 * 登录成功处理器
 */
@Log4j2
@Component
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal) || !(principal instanceof UserLoginInfo uli)) {
            log.error("登录成功，但返回的对象类型不对");
            throw new BaseException(AUTHORIZE_ERROR,"鉴权失败");
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authorityList = authorities.stream().map(String::valueOf).toList();
        uli.setAuthorities(authorityList);
        // 生成token和refreshToken
        Map<String, Object> responseData = new LinkedHashMap<>();
        //token 20分钟
        responseData.put("token", JwtUtil.generateToken(uli,Calendar.MINUTE,20));
        //refreshToken 刷新token 2天
        responseData.put("refreshToken", JwtUtil.generateToken(uli,Calendar.DAY_OF_YEAR,2));
        Result r = ResultBuilder.initResult().data(responseData).msg("${login.success:登录成功！}").build();
        ResponseUtil.send(response,r);
    }
}
