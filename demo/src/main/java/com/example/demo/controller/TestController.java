package com.example.demo.controller;

import com.example.demo.lock.LockByKey;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/***
 * @author niumengliang
 * Date:2024/9/19
 * Time:13:47
 */
@Log4j2
@RestController
public class TestController {


    @Resource
    private LockByKey<String> lockByKey;
//    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @GetMapping("/a/b/c")
    public String test() {
        // 500毫秒内，没拿到令牌，就直接进入服务降级
        log.info("请求成功  /a/b/c");

        return "请求成功";
    }

    @GetMapping("/a/{id}")
    public String test2(@PathVariable("id") int id) {
        // 500毫秒内，没拿到令牌，就直接进入服务降级
        lockByKey.lock(id + "");
        try {
            log.info("当前是：{}", id);
            Thread.sleep(4 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockByKey.unlock(id + "");
        }
        return "请求成功";
    }

}
