package com.niuml.niu_study_security_all_i18n.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.util.Calendar;

/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:15:20
 */
@Log4j2
public class JwtUtil {

    private static final String secretKey = "123456";
    private static final String DATA = "data";//登录成功后 将用户的信息直接转换成json字符串保存到jwt中，方便获取

    public static String generateToken(Object obj, int field, int amount) {
        String json = JsonUtil.toJson(obj);
        Calendar instance = Calendar.getInstance();
        instance.add(field,amount);
        return JWT.create().withClaim(DATA,json)
                .withExpiresAt(instance.getTime())//设置过期时间
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static  <T> T verifyJwt(String jwt, Class<T> jwtPayloadClass) {
        if(!StringUtils.hasText(jwt)) return null;
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT verify = jwtVerifier.verify(jwt);
        String jsonData = verify.getClaim(DATA).asString();
        log.info("verifyJwt:{}",jsonData);
        return JsonUtil.parse(jsonData,jwtPayloadClass);
    }



}
