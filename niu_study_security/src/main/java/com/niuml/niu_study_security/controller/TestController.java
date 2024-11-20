package com.niuml.niu_study_security.controller;

import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @author niumengliang
 * Date:2024/11/20
 * Time:16:34
 */
@RestController
@RequestMapping("/open-api")
public class TestController {
    @PreAuthorize("hasAuthority('sys:add') ")
    @GetMapping("/ttt1")
    public Result ttt1(){
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt1").build();
    }
    @PreAuthorize("hasAuthority('sys:add1') ")
    @GetMapping("/ttt2")
    public Result ttt2(){
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt2").build();
    }
    @GetMapping("/ttt3")
    public Result ttt3(){
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt2").build();
    }


}
