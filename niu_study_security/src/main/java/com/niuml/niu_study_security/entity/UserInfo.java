package com.niuml.niu_study_security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/***
 * @author niumengliang
 * Date:2024/12/13
 * Time:17:28
 */
@Data
public class UserInfo implements Serializable {
    /**
     *
     */
    private Integer id = 1;

    /**
     * 身份证号
     */
    private String idNumber = "123123123123";

    /**
     * 姓名
     */
    private String name = "niu niu";

    /**
     * 1男 0女
     */
    private Integer sex = 1;

    private List<String> list= Arrays.asList("123","2345");
}
