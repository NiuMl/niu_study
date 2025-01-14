package com.niuml.niu_study_security_all_i18n.authentication.handler.login.userName;

import com.niuml.niu_study_security_all_i18n.authentication.handler.common.UserLoginInfo;
import com.niuml.niu_study_security_all_i18n.common.exceptions.BaseException;
import com.niuml.niu_study_security_all_i18n.entity.BaseUser;
import com.niuml.niu_study_security_all_i18n.entity.LoginModeDefault;
import com.niuml.niu_study_security_all_i18n.mapper.BaseUserMapper;
import com.niuml.niu_study_security_all_i18n.mapper.LoginModeDefaultMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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

import static com.niuml.niu_study_security_all_i18n.common.codes.ExceptionCode.USER_DATA_ERROR;

/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:16:34
 * 具体的实现用户名和密码登录的处理逻辑
 * extends PersonProvider
 */
@Component
public class UserNameAuthenticationProvider implements AuthenticationProvider {

    /**
     * 基础用户表 mapper
     */
    @Resource
    private BaseUserMapper baseUserMapper;
    /**
     * 默认登录表 mapper
     */
    @Resource
    private LoginModeDefaultMapper loginModeDefaultMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户提交的用户名 + 密码：
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        LoginModeDefault lmd = loginModeDefaultMapper.findByUserName(username);
        if (lmd == null || !passwordEncoder.matches(password, lmd.getPassword())) {
            // 密码错误，直接抛异常。
            // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
            // BadCredentialsException就是这个异常的子类
            // 抛出异常后后，AuthenticationFailureHandler的实现类会处理这个异常。
            throw new BadCredentialsException("${login.invalid.username.or.pwd:用户名或密码不正确}");
        }
        //查询用户基础数据
        BaseUser user = baseUserMapper.findById(lmd.getBaseUserId());
        if (user == null) throw new BaseException(USER_DATA_ERROR,"${login.user.data.error:用户数据异常}");
        // 认证成功，返回一个认证成功的Authentication对象。
        // TODO 查询当前用户的角色和权限啥的

        UserNameAuthentication una = new UserNameAuthentication(handDbPre(Arrays.asList("user:admin", "user:common","ROLE_USER")));
        UserLoginInfo uli = new UserLoginInfo();
        uli.setInterfaces(Arrays.asList("/open-api/ttt2","/open-api/ttt1"));
        BeanUtils.copyProperties(user, uli);
        una.setCurrentUser(uli);
        // 认证通过，这里一定要设成true
        una.setAuthenticated(true);
        return una;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserNameAuthentication.class);
    }
    protected List<SimpleGrantedAuthority> handDbPre(List<String> authorities) {
        List<SimpleGrantedAuthority> sga = new ArrayList<>();
        if (authorities != null) {
            for (String role : authorities) {
                sga.add(new SimpleGrantedAuthority(role));
            }
        }
        return sga;
    }
}
