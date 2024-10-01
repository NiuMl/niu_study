package com.example.demo.interceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/***
 * @author niumengliang
 * Date:2024/9/22
 * Time:10:20
 */
public class DateTimeUtils {

    private static DateTimeFormatter ymdhms = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getNowDateTime(){
        return ymd.format(LocalDateTime.now());
    }

    /**
     * 1726989966424
     * 1726977583
     *
     */
    public static void main(String[] args) {
        String ss = getNowDateTime();
        String s2 = new StringBuilder(ss).reverse().toString();
        System.out.println(ss+"_"+s2);
        System.out.println(System.currentTimeMillis());

        long l1 = 1726989966424l;
        long l2 = 1726989966524l;
        System.out.println(l1-l2);
    }
}
