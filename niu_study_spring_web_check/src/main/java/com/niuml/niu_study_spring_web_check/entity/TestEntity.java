package com.niuml.niu_study_spring_web_check.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:57
 */
@Data
public class TestEntity {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少为6位")
    private String password;

}
