package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.interceptor.InterceptorNoPass.*;

/***
 * @author niumengliang
 * Date:2024/9/19
 * Time:13:49
 */
@SuppressWarnings("NullableProblems")
@Component
@Log4j2
public class LocalInterceptor extends XInterceptor implements HandlerInterceptor {

    /**
     * 1、配置 是否启用ip限制 比如说某一个ip一秒访问次数过多，就禁止访问X分钟，也可以不禁止，返回蜜罐值
     * 2、配置 1 是否判断从head里面取某个或者几个属性 没有就返回蜜罐🍯值
     * 3、配置 1 是否设定全局的最大并发数
     * 4、配置 是否设定某个或者某些接口的最大并发数，超过了就默认返回，可配置默认返回格式或者返回体
     * 5、配置 如果当前最大并发数到了设定的某个阈值数之后，是否开启验证码的方式限制请求（也算是为了减少并发）
     * 6、配置 限制国外IP
     * 7、配置 1 是否增加验证请求head里面的对称加密算法AES属性 （比如：请求头里面增加一个sign签名属性，
     *      服务端解密后，如果解密失败，则返回蜜罐值）
     * 8、配置 某个ip的异常请求次数，如果配置10个，当异常请求到达10个后，这个ip在X分钟内禁止访问
     * 9、接口权限 todo
     * 10、数据权限 todo
     *
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = getIPAddress(request);
        String uri = request.getRequestURI();
//        System.out.println(cache.getIfPresent(ipAddress));
        ipTL.set(ipAddress);
        log.info("request请求地址uri={},ip={}", uri, ipAddress);
        /*
         * ip白名单
         */
        if(!enableIpWhiteList(ipAddress)){
            log.info("当前IP:{}，不在白名单中，不允许请求",ipAddress);
            returnJson.accept(response,IpWhiteList);
            return false;
        }
        /*
         * ip黑名单
         */
        if(!enableIpBlackList(ipAddress)){
            log.info("当前IP:{}，在黑名单中，不允许请求",ipAddress);
            returnJson.accept(response,IpBlackList);
            return false;
        }
        /*
         * 请求不检验过滤
         */
//        List<String> list = Arrays.asList("/a/b/c","/a/1", "/a/2");
//        if (list.contains(uri)) {
//            return true;
//        }

        /*
         * ip请求禁用检查
         */
        if(!ipLimitation()){
            log.info("当前IP:{}，请求次数过多，被临时管控，不允许请求",ipAddress);
            returnJson.accept(response,IpLimitation);
            return false;
        }
        /*
          全局请求次数限制
         */
        if (!canLetThrough()) {
            log.info("当前IP:{}，获取请求锁失败",ipAddress);
            returnJson.accept(response,GlobalInterception);
            return false;
        }
        /*
          验证请求头
         */
        if(!checkHeadEncryption(request)){
            log.info("当前IP:{}，请求头验证失败",ipAddress);
            returnJson.accept(response,CheckHeadEncryption);
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
