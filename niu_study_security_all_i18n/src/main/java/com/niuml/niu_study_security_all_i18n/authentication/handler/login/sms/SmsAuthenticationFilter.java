package com.niuml.niu_study_security_all_i18n.authentication.handler.login.sms;

import com.niuml.niu_study_security_all_i18n.utils.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:11:02
 * sms使用的filter
 */
@Log4j2
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public SmsAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
                                   AuthenticationManager authenticationManager,
                                   AuthenticationSuccessHandler authenticationSuccessHandler,
                                   AuthenticationFailureHandler authenticationFailureHandler) {
        super(pathRequestMatcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("当前是使用短信Filter");
        Map<String,Object> parameters = RequestUtil.req2Map(request);
        String phoneNumber = parameters.get("phone").toString();
        String code = parameters.get("code").toString();

        SmsAuthentication authentication = new SmsAuthentication();
        authentication.setPhone(phoneNumber);
        authentication.setCode(code);
        // 提取参数阶段，authenticated一定是false
        authentication.setAuthenticated(false);
        return this.getAuthenticationManager().authenticate(authentication);
    }
}
