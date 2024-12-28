package com.niuml.niu_study_security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import com.niuml.niu_study_security.common.utils.JSON;
import com.niuml.niu_study_security.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    public Result ttt1() {
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt1").build();
    }

    @PreAuthorize("hasAuthority('sys:add1') ")
    @GetMapping("/ttt2")
    public Result ttt2() {
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt2").build();
    }

    @GetMapping("/ttt3")
    public Result ttt3() {
        return ResultBuilder.aResult().data("test").msg("测试国际化消息ttt2").build();
    }


    static String secret = "HfkjksFKLJISJFKLFKWJFQFIQWIOFJQOFFQGGSDGFFJIQOEUFIEJFIOQWEFHFQOK5FKOIQWUFFEFE423FIQEOFJHUEWHFKASKDLQWJIFSJDJKFHJIJWO";

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
//        Map<String, Object> map = new HashMap<String, Object>() {{
//            put("a", "a");
//            put("b", "b");
//            put("c", "c");
//        }};
//
//        Calendar instance = Calendar.getInstance();
//        // 20秒后令牌token失效
//        instance.add(Calendar.SECOND,20);
//        String sign = JWT.create().withPayload(map)
////                .withHeader(map) // header可以不写，因为默认值就是它
////                .withClaim("userId", 21)  //payload
////                .withClaim("username", "xiaoshuang")
//                .withExpiresAt(instance.getTime()) // 指定令牌的过期时间
//                .sign(Algorithm.HMAC256("XIAOSHUANG"));//签名
//
//        System.out.println(sign);
//
//        String s = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpbnRlcmZhY2VzIjpbIi9vcGVuLWFwaS90dHQyIiwiL29wZW4tYXBpL3R0dDEiXSwiYWRkcmVzcyI6IuahguaelyIsInNleCI6MSwidXBkYXRlVGltZSI6bnVsbCwiaWROdW1iZXIiOiIxMTExMTExMTExMTExMTExMTEiLCJlbmFibGVkIjoxLCJleHBpcmVkVGltZSI6MCwiYXV0aG9yaXRpZXMiOlsidXNlcjphZG1pbiIsInVzZXI6Y29tbW9uIl0sImNyZWF0ZVRpbWUiOjE3MzM3NzkyMTQwMDAsIm5hbWUiOiLniZsiLCJpZCI6MSwibG9ja2VkIjowLCJzdGF0dXMiOjAsImV4cCI6MTczNDA1NTk4OX0.uOuqfwfK-Zsda-cHh9Q3d2YmdWPH3L2sQ01GoFt2HxA";
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("XIAOSHUANG")).build();
//        DecodedJWT verify = jwtVerifier.verify(sign);
//        System.out.println(verify.getPayload());
//        Map<String, Claim> claims = verify.getClaims();
//        System.out.println(claims);
//        System.out.println(verify.getClaims().get("a"));
//        System.out.println(verify.getExpiresAt());
//
//
//        UserInfo userInfo = new UserInfo();
//        Map convert = JSON.convert(userInfo, Map.class);
//        instance.add(Calendar.MINUTE,20);
//        sign = JWT.create().withPayload(map)
//                .withExpiresAt(instance.getTime()) // 指定令牌的过期时间
//                .sign(Algorithm.HMAC256("XIAOSHUANG"));//签名
//        System.out.println(sign);
//        jwtVerifier = JWT.require(Algorithm.HMAC256("XIAOSHUANG")).build();
//        verify = jwtVerifier.verify(sign);
//        System.out.println(verify.getPayload());
//        claims = verify.getClaims();
//        System.out.println(claims);
//        String jsData = JSON.stringify(claims.toString());
//        System.out.println(jsData);

    }


}
