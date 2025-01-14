package com.niuml.niu_study_security_all_i18n.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 默认登录方式 ef_login_mode_default
 */
@Data
public class LoginModeDefault implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 基础用户表ID
     */
    private Integer baseUserId;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    @Serial
    private static final long serialVersionUID = 1L;
}
