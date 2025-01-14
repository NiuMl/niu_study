package com.niuml.niu_study_security_all_i18n.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础信息
 * ef_base_user
 */
@Data
public class BaseUser implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 1男 0女
     */
    private Integer sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 1：锁住 ， 0：未锁
     */
    private Integer locked;

    /**
     * 1:启用 ， 0：禁用
     */
    private Integer enabled;

    /**
     * 0：删除  1：存在
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
