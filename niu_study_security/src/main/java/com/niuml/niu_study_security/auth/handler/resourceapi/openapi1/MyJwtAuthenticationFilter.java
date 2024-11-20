package com.niuml.niu_study_security.auth.handler.resourceapi.openapi1;

import com.niuml.niu_study_security.auth.handler.login.UserLoginInfo;
import com.niuml.niu_study_security.auth.handler.resourceapi.InterfaceLevelFilter;
import com.niuml.niu_study_security.auth.service.JwtService;
import com.niuml.niu_study_security.common.exception.ExceptionTool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class MyJwtAuthenticationFilter extends InterfaceLevelFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyJwtAuthenticationFilter.class);

    private JwtService jwtService;

    public MyJwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Use OpenApi1AuthenticationFilter");
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwtToken)) {
            ExceptionTool.throwException("JWT token is missing!", "miss.token");
        }
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }
//        try {
        UserLoginInfo userLoginInfo = jwtService.verifyJwt(jwtToken, UserLoginInfo.class);
        judgeRequestInter(request,userLoginInfo.getInterfaces() );
        Collection<? extends GrantedAuthority> authorities = userLoginInfo.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new).toList();
        MyJwtAuthentication authentication = new MyJwtAuthentication(authorities);
        authentication.setJwtToken(jwtToken);
        authentication.setAuthenticated(true); // 设置true，认证通过。
        authentication.setCurrentUser(userLoginInfo);
        // 认证通过后，一定要设置到SecurityContextHolder里面去。
        SecurityContextHolder.getContext().setAuthentication(authentication);
//            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
//        } catch (ExpiredJwtException e) {
//            // 转换异常，指定code，让前端知道时token过期，去调刷新token接口
//            ExceptionTool.throwException("jwt过期", HttpStatus.UNAUTHORIZED, "token.expired");
//        } catch (Exception e) {
//            ExceptionTool.throwException("jwt无效", HttpStatus.UNAUTHORIZED, "token.invalid");
//        }
        // 放行
        filterChain.doFilter(request, response);
    }
}
