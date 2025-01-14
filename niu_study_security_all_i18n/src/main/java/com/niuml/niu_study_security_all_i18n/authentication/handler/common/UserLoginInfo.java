package com.niuml.niu_study_security_all_i18n.authentication.handler.common;

import com.niuml.niu_study_security_all_i18n.entity.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/***
 * @author niumengliang
 * Date:2024/12/10
 * Time:15:28
 * 用户登录成功后，将用户的一些信息保存到redis里面或者jwt里面
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginInfo extends BaseUser implements Serializable {

    /**
     * 过期时间
     */
    private long expiredTime;
//    /**
//     * 用户权限列表
//     */
    private List<String> authorities;
    /**
     * 接口权限列表
     */
    private List<String> interfaces;
}
