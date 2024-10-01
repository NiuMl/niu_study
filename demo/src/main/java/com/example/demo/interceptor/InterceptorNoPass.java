package com.example.demo.interceptor;

import lombok.Getter;

/***
 * User:niumengliang
 * Date:2024/9/21
 * Time:16:38
 */
@Getter
public enum InterceptorNoPass {


    GlobalInterception("""
            {
               "msg": "请求人数太多，请求稍后再试",
               "code": 300,
               "data": null
            }
            """),
    CheckHeadEncryption("""
            {
               "msg": "请求非法",
               "code": 301,
               "data": null
            }
            """),
    IpLimitation("""
            {
               "msg": "ip限制",
               "code": 302,
               "data": null
            }
            """),
    IpWhiteList("""
            {
               "msg": "ip白名单限制（未在白名单内）",
               "code": 303,
               "data": null
            }
            """),
    IpBlackList("""
            {
               "msg": "ip黑名单限制（在黑名单内）",
               "code": 304,
               "data": null
            }
            """),


    ;

    private final String msg;

    InterceptorNoPass(String msg) {
        this.msg = msg;
    }

}
