package com.niuml.niu_study_security.auth.handler.login.sms;

import com.niuml.niu_study_security.auth.handler.login.UserLoginInfo;
import com.niuml.niu_study_security.auth.service.UserService;
import com.niuml.niu_study_security.common.model.User;
import com.niuml.niu_study_security.common.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String phoneNumber = authentication.getPrincipal().toString();
    String smsCode = authentication.getCredentials().toString();
    // 验证验证码是否正确
    if (validateSmsCode(smsCode)) {
      // 根据手机号查询用户信息
      User user = userService.getUserByPhone(phoneNumber);
      if (user == null) {
        // 密码错误，直接抛异常。
        // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
        // BadCredentialsException就是这个异常的子类
        throw new BadCredentialsException("${user.not.found:找不到用户!}");
      }
      UserLoginInfo currentUser = JSON.convert(user, UserLoginInfo.class);

      SmsAuthentication token = new SmsAuthentication();
      token.setCurrentUser(currentUser);
      token.setAuthenticated(true); // 认证通过，一定要设成true
      return token;
    } else {
      throw new BadCredentialsException("${verify.sms.code.fail:验证码不正确！}");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SmsAuthentication.class.isAssignableFrom(authentication);
  }

  private boolean validateSmsCode(String smsCode) {
    // todo
    return smsCode.equals("000000");
  }
}
