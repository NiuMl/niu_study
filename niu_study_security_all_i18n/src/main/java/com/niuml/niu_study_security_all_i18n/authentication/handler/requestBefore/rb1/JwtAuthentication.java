package com.niuml.niu_study_security_all_i18n.authentication.handler.requestBefore.rb1;

import com.niuml.niu_study_security_all_i18n.authentication.handler.common.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtAuthentication extends AbstractAuthenticationToken {

    // 前端传过来
    private String jwtToken;
    // 认证成功后，后台从数据库获取信息
    private UserLoginInfo currentUser;

    public JwtAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        // 根据SpringSecurity的设计，授权成后，Credential（比如，登录密码）信息需要被清空
        return isAuthenticated() ? null : jwtToken;
    }

    @Override
    public Object getPrincipal() {
        // 根据SpringSecurity的设计，授权成功之前，getPrincipal返回的客户端传过来的数据。授权成功后，返回当前登陆用户的信息
        return isAuthenticated() ? currentUser : jwtToken;
    }


}
