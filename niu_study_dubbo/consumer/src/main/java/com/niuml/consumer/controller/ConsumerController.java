package com.niuml.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.niuml.api.UserService;
import com.niuml.api.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ConsumerController {
    /**
     *  e invoker dubbo-provider
     */
    @Reference
    private UserService userService;


    @GetMapping("/info")
    public String getUserById() {
		return userService.getUserInfo();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

}

