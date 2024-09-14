package com.niuml.niu_study_spring_web_check.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2023/9/7
 * Time:11:01
 */
@Data
public class TtEntity {
    @NotBlank(message = "用户名不能为空")
    private String name;
}
