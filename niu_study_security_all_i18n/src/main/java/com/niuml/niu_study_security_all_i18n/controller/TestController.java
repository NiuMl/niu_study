package com.niuml.niu_study_security_all_i18n.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @author niumengliang
 * Date:2024/12/10
 * Time:11:22
 */
@Slf4j
@RestController
public class TestController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/test")
    public String test(){
        log.info("test");
        return "test";
    }
    @PreAuthorize("hasAuthority('user:admin')")
    @GetMapping("/testHasAuth")
    public String test11(){
        log.info("test");
        return "test";
    }
    @PreAuthorize("hasAuthority('user:admin1')")
    @GetMapping("/testHasAuth1")
    public String test111(){
        log.info("test");
        return "test";
    }
    @GetMapping("/public/test")
    public String test1(){
        log.info("public test");
        return "public test";
    }
    @GetMapping("/private/test")
    public String test2(){
        log.info("public test");
        return "public test";
    }
    @GetMapping("/anonymous/test")
    public String test3(){
        log.info("public test");
        return "public test";
    }


}
