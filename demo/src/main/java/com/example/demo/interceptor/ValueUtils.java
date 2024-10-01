package com.example.demo.interceptor;

import org.springframework.util.StringUtils;

import java.util.Arrays;

/***
 * @author niumengliang
 * Date:2024/9/22
 * Time:12:22
 */
public class ValueUtils {

    private static String getVHS(){
        String ss = DateTimeUtils.getNowDateTime();
        String s2 = new StringBuilder(ss).reverse().toString();
        return ss+"_"+s2;
    }
    public static String handlerValidateHeadEncryptionStr(String key){
        if(!StringUtils.hasText(key))
            return getVHS();
        return key;
    }


    public static String handlerValidateHeadEncryption(String key){
        if(!StringUtils.hasText(key))
            return "AES";
        if(!Arrays.asList("AES","RSA").contains(key))
            return "AES";
        return key;
    }
}
