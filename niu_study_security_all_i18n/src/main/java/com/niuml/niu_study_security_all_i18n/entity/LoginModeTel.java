package com.niuml.niu_study_security_all_i18n.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 电话号登录方式
 *  login_mode_tel
 */
@Data
public class LoginModeTel implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 基础用户表ID
     */
    private Integer baseUserId;

    /**
     * 登录使用的电话号
     */
    private String tel;

    @Serial
    private static final long serialVersionUID = 1L;
}
