package com.niuml.niu_study_security_all_i18n.authentication.handler.login.sms;

import com.niuml.niu_study_security_all_i18n.authentication.handler.common.UserLoginInfo;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.PersonProvider;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.userName.UserNameAuthentication;
import com.niuml.niu_study_security_all_i18n.common.exceptions.BaseException;
import com.niuml.niu_study_security_all_i18n.entity.BaseUser;
import com.niuml.niu_study_security_all_i18n.entity.LoginModeTel;
import com.niuml.niu_study_security_all_i18n.mapper.BaseUserMapper;
import com.niuml.niu_study_security_all_i18n.mapper.LoginModeTelMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.niuml.niu_study_security_all_i18n.common.codes.ExceptionCode.USER_DATA_ERROR;


/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:14:31
 */
@Component
public class SmsAuthenticationProvider extends PersonProvider implements AuthenticationProvider {

    @Resource
    private LoginModeTelMapper loginModeTelMapper;
    @Resource
    private BaseUserMapper baseUserMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String tel = authentication.getPrincipal().toString();
        String code = authentication.getCredentials().toString();
        if (!validateSmsCode(tel, code)) throw new BadCredentialsException("${verify.sms.code.fail:验证码不正确！}");
        LoginModeTel lmt = loginModeTelMapper.findByTel(tel);
        if (lmt == null) throw new BadCredentialsException("${verify.sms.code.fail:该手机号未注册！}");
        //查询用户基础数据
        BaseUser user = baseUserMapper.findById(lmt.getBaseUserId());
        if (user == null) throw new BaseException(USER_DATA_ERROR,"${login.user.data.error:用户数据异常}");
        // 认证成功，返回一个认证成功的Authentication对象。
        // TODO 查询当前用户的角色和权限啥的
        UserNameAuthentication una = new UserNameAuthentication(handDbPre(Arrays.asList("user:admin", "user:common")));
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
        return authentication.isAssignableFrom(SmsAuthentication.class);
    }

    private boolean validateSmsCode(String tel, String smsCode) {
        // todo 根据手机号从redis里面取验证码 进行比对
        return smsCode.equals("000000");
    }
}
