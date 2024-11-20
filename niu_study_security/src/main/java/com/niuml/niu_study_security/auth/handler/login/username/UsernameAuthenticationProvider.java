package com.niuml.niu_study_security.auth.handler.login.username;

import com.niuml.niu_study_security.auth.handler.login.UserLoginInfo;
import com.niuml.niu_study_security.auth.service.UserService;
import com.niuml.niu_study_security.common.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 帐号密码登录认证
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsernameAuthenticationProvider() {
        super();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户提交的用户名 + 密码：
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // 查数据库，匹配用户信息
        User user = userService.getUserFromDB(username);
        if (user == null
                || !passwordEncoder.matches(password, user.getPassword())) {
            // 密码错误，直接抛异常。
            // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
            // BadCredentialsException就是这个异常的子类
            // 抛出异常后后，AuthenticationFailureHandler的实现类会处理这个异常。
            throw new BadCredentialsException("${invalid.username.or.pwd:用户名或密码不正确}");
        }
        //下面应该是获取该用户的信息，写入到Authentication里面
        UsernameAuthentication token = new UsernameAuthentication(handSga(Arrays.asList("sys:add", "sys:del")));
        UserLoginInfo uli = new UserLoginInfo();
        uli.setInterfaces(Arrays.asList("/open-api/ttt2","/open-api/ttt1"));
        BeanUtils.copyProperties(user, uli);
        token.setCurrentUser(uli);
        token.setAuthenticated(true); // 认证通过，这里一定要设成true
        return token;
    }

    private List<SimpleGrantedAuthority> handSga(List<String> authorities) {
        List<SimpleGrantedAuthority> sga = new ArrayList<>();
        if (authorities != null) {
            for (String role : authorities) {
                sga.add(new SimpleGrantedAuthority(role));
            }
        }
        return sga;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernameAuthentication.class);
    }
}

