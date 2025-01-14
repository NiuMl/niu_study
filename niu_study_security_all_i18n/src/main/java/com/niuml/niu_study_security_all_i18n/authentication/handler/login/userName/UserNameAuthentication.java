package com.niuml.niu_study_security_all_i18n.authentication.handler.login.userName;

import com.niuml.niu_study_security_all_i18n.authentication.handler.common.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:16:11
 * SpringSecurity传输登录认证的数据的载体，相当一个Dto
 * 必须是 Authentication 实现类
 * 这里选择extends AbstractAuthenticationToken ，而不是直接implements Authentication,
 * 是为了少些写代码。因为 Authentication 定义了很多接口，我们用不上。
 */
@Getter
@Setter
public class UserNameAuthentication extends AbstractAuthenticationToken {

    // 前端传过来
    private String username;
    // 前端传过来
    private String password;
    // 认证成功后，后台从数据库获取信息
    private UserLoginInfo currentUser;

    public UserNameAuthentication() {
        super(null);
    }

    public UserNameAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        // 根据SpringSecurity的设计，授权成后，Credential（比如，登录密码）信息需要被清空
        return isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        // 根据SpringSecurity的设计，授权成功之前，getPrincipal返回的客户端传过来的数据。授权成功后，返回当前登陆用户的信息
        return isAuthenticated() ? currentUser : username;
    }

}
