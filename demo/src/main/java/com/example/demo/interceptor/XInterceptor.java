package com.example.demo.interceptor;

import com.example.demo.lock.LockByKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/***
 * @author niumengliang
 * Date:2024/9/21
 * Time:10:02
 */
@SuppressWarnings("UnstableApiUsage")
@Component
@Log4j2
public class XInterceptor {

    protected ThreadLocal<String> ipTL = new ThreadLocal<>();

    ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 限流速率
     * 每秒允许的请求数
     */
    @Value("${firewall.gi.rate:200.0}")
    protected double rate;
    /**
     * 1 是否启用全局拦截 默认不开启
     */
    @Value("${firewall.gi.enableGlobalInterception:false}")
    protected boolean enableGlobalInterception;
    /**
     * 2 是否启用验证请求头某值拦截 默认不开启
     */
    @Value("${firewall.vh.enableValidateHeadInterception:false}")
    protected boolean enableValidateHeadInterception;
    /**
     * 是否启用验证请求头时间戳跟本地时间差比较
     */
    @Value("${firewall.vh.enableHeadEncryptionTimeCheck:false}")
    protected boolean enableHeadEncryptionTimeCheck;

    @Value("${firewall.vh.headEncryptionTimeCheckDiffer:1000}")
    protected long headEncryptionTimeCheckDiffer;

    /**
     * 3 验证请求头的值 跟2一起用
     */
    protected final String validateHeadSign = "X-Validate-Head-Sign";
    protected final String validateHeadTime = "X-Validate-Head-Time";
    /**
     * 4 验证请求头的验证加密方式 跟2一起用 为空就是默认aes
     * AES/CBC/PKCS5Padding
     */
    @Value("#{T(com.example.demo.interceptor.ValueUtils).handlerValidateHeadEncryption('${firewall.vh.validateHeadEncryption:}')}")
    private String validateHeadEncryption;
    /**
     * 5 加密串 跟2一起用  密钥
     * 有值就用输入的值，没值就使用默认动态值 yyyy-MM-dd_dd-MM-yyyy
     */
    @Value("${firewall.vh.validateHeadEncryptionStr}")
    private String validateHeadEncryptionStr;
    /**
     * 是否开启ip缓存拦截
     */
    @Value("${firewall.cache.enableIpCacheInterception:false}")
    protected boolean enableIpCacheInterception;
    /**
     * 是否启用ip缓存同步，之所以弄这个，是因为多并发的时候，ip计数会有延迟，
     * 加锁之后就可以了，但是会有一点点性能问题，不过不怎么影响，因为这是纯内存操作
     */
    @Value("${firewall.cache.enableIpCacheSynchronized:false}")
    protected boolean enableIpCacheSynchronized;
    /**
     * ip缓存拦截最大值  设置缓存容量
     */
    @Value("${firewall.cache.ipCacheMaximumSize:1000}")
    protected int enableIpCacheMaximumSize;
    /**
     * 设置缓存项的过期时间  分钟  如果某个key在这个时间内没有补访问，就会删掉这个key
     */
    @Value("${firewall.cache.enableIpCacheExpireAfterAccess:10}")
    protected int enableIpCacheExpireAfterAccess;
    /**
     * 设置ip最大记录数  超过这个就不可以访问系统
     */
    @Value("${firewall.cache.ipMaxRecord:10}")
    protected int ipMaxRecord;

    /**
     * 开启ip白名单
     */
    @Value("${firewall.gi.enableIpWhiteList:false}")
    protected boolean enableIpWhiteList;
    //ip白名单列表
    @Value("${firewall.gi.ipWhiteList}")
    protected List<String> ipWhiteList;
    /**
     * 开启ip黑名单
     */
    @Value("${firewall.gi.enableIpBlackList:false}")
    protected boolean enableIpBlackList;
    //ip黑名单列表
    @Value("${firewall.gi.ipBlackList}")
    protected List<String> ipBlackList;
    /**
     * 开启url白名单
     */
    @Value("${firewall.gi.enableUrlWhiteList:false}")
    protected boolean enableUrlWhiteList;
    /**
     * url白名单
     */
    @Value("${firewall.gi.urlWhiteList}")
    protected List<String> urlWhiteList;


    @Resource
    private LockByKey<String> lockByKey;

    //限制器
    protected RateLimiter limiter;

    //ip缓存
    protected Cache<String, Integer> cache;

    @PostConstruct
    public void initRateLimiter() {
        limiter = RateLimiter.create(rate);
    }

    @PostConstruct
    public void initCache() {
        CacheBuilder<Object, Object> cb = CacheBuilder.newBuilder();
        cb.maximumSize(enableIpCacheMaximumSize);// 设置缓存容量
//        cb.expireAfterWrite(enableIpCacheExpireAfterWrite, TimeUnit.MINUTES); // 设置缓存项的过期时间  指定对象被写入到缓存后多久过期
        cb.expireAfterAccess(enableIpCacheExpireAfterAccess, TimeUnit.MINUTES); // 设置缓存项的过期时间  指定对象多久没有被访问后过期。
        cb.concurrencyLevel(1);
        cache = cb.build();
    }


    private final Supplier<String> getVHS = () -> {
        String ss = DateTimeUtils.getNowDateTime();
        String s2 = new StringBuilder(ss).reverse().toString();
        return ss + "_" + s2;
    };

    private final Supplier<String> getValidateHeadEncryptionStr = () -> !StringUtils.hasText(validateHeadEncryptionStr) ? getVHS.get() : validateHeadEncryptionStr;


    private void addIpCache(String ip, Integer count) {
//        synchronized (ip) {
//        Integer count = cache.getIfPresent(ip);
        if (count == null) {
            cache.put(ip, 1);
        } else {
            cache.put(ip, count + 1);
        }
    }

    protected final BiConsumer<HttpServletResponse, InterceptorNoPass> returnJson = (response, in) -> {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(in.getMsg());
        } catch (IOException e) {
            log.error("返回数据异常 error ---> {}", e.getMessage(), e);
        }
    };
    protected final BiFunction<Long, Long, Long> differ = (l1, l2) -> {
        long lx = l1 - l2;
        return lx < 0 ? Math.abs(lx) : lx;
    };

    /**
     * 是否启用ip白名单
     */
    protected boolean enableIpWhiteList(String ip) {
        if (!enableIpWhiteList) return true;
        //ip白名单校验
        return ipWhiteList.contains(ip);
    }

    /**
     * 是否启用ip黑名单
     */
    protected boolean enableIpBlackList(String ip) {
        if (!enableIpBlackList) return true;
        //ip黑名单校验
        return ipBlackList.contains(ip);
    }

    /**
     * 是否放行
     *
     * @return false不放行 true放行
     */
    protected boolean canLetThrough() {
        if (!enableGlobalInterception) return true;
        boolean tryAcquire = limiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
//            log.warn("当前IP:{}，获取令牌失败",ipTL.get());
            return false;
        }
        log.info("当前IP:{}，获取令牌成功",ipTL.get());
        return true;
    }

    /**
     * 校验请求头
     *
     * @return false不放行 true放行
     */
    protected boolean checkHeadEncryption(HttpServletRequest request) {
        if (!enableValidateHeadInterception) return true;
        //前端加密后的串
        String vhs = request.getHeader(validateHeadSign);
        //前端加密的时间
        String vht = request.getHeader(validateHeadTime);
        if (vhs == null || vht == null)
            return false;
        //加密串
        String vhe = getValidateHeadEncryptionStr.get();
        try {
            String encrypt = EncryptionUtil.encrypt(validateHeadEncryption, vht, vhe);
            if (!encrypt.equals(vhs)) {
                log.info("传入的时间戳：{}，加密串(密钥)：{}，加密后值：{}，不等于前端传入的加密后值：{}", vht, vhe, encrypt, vhs);
                return false;
            }
        } catch (Exception e) {
            log.error("校验请求头 error :{}", e.getMessage());
            return false;
        }
        if (enableHeadEncryptionTimeCheck) {
            long l2 = differ.apply(System.currentTimeMillis(), Long.parseLong(vht));
            if (headEncryptionTimeCheckDiffer < l2) {
                log.info("请求头加密时间校验失败");
                return false;
            }
        }
        return true;
    }


    private boolean cl(String ip) {
//        System.out.println(cache.getIfPresent(ip));
        Integer count = cache.getIfPresent(ip);
        addIpCache(ip, count);
        if (count != null && count > ipMaxRecord) {
            log.info("ip拦截：{}，当前异常请求次数：{}，超过最大访问次数：{}", ip, count, ipMaxRecord);
            return false;
        }
        return true;
    }

    private boolean syCl(String ip) {
        lockByKey.lock(ip);
        boolean cl = cl(ip);
        lockByKey.unlock(ip);
        return cl;
    }

    protected boolean ipLimitation() {
        if (!enableIpCacheInterception) return true;
        String ip = ipTL.get();
        return enableIpCacheSynchronized ? syCl(ip) : cl(ip);
    }


    protected String getIPAddress(HttpServletRequest request) {
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }
        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && !ipAddresses.isEmpty()) {
            ip = ipAddresses.split(",")[0];
        }
        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
