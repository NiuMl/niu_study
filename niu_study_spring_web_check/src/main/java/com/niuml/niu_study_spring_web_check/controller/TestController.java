package com.niuml.niu_study_spring_web_check.controller;

import com.niuml.niu_study_spring_web_check.common.Res;
import com.niuml.niu_study_spring_web_check.common.SelfException;
import com.niuml.niu_study_spring_web_check.entity.TestEntity;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.niuml.niu_study_spring_web_check.enums.ExceptionEnum.INTERNAL_SERVER_ERROR;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:07
 */
@Log4j2
@RestController
public class TestController {

    @GetMapping("test")
    public Res ttest(){
        int a = 1/0;
        return Res.ok("hello world");
    }
    @GetMapping("test2")
    public Res ttest2(){
        if(true){
            throw new SelfException(INTERNAL_SERVER_ERROR);
        }
        return Res.ok("hello world");
    }
    @PostMapping("test3")
    public Res ttest3(@Valid @RequestBody TestEntity te){
        log.info("输入：{}",te);
        return Res.ok("hello world");
    }
}
