package com.niuml;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

/***
 * @author niumengliang
 * Date:2024/9/30
 * Time:11:02
 */
public class FireWallInterceptor {
    public static void beforeRequest(Object request, Object response) throws Exception {
//        System.out.println("beforeRequest Intercepting HTTP request headers");
//        System.out.println(request);
//        System.out.println(response);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String requestedId = httpServletRequest.getHeader("X-Requested-ID");
        // 打印客户端提交的X-Requested-ID
        // 当请求报错时，可根据此X-Requested-ID 搜索相关日志
        // 关键词前后300行  grep "X-Requested-ID" test.log -C 300
        // 关键词之后300行  grep "X-Requested-ID" test.log -A 300
        System.out.println("X-Requested-ID ====> " + requestedId);
        if (requestedId.equals("123123123")) {
            System.out.println("开始拦截");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 增加一个特别地响应请求头
            httpServletResponse.addHeader("Interceptor", "wa o wa o wa o");
            httpServletResponse.setContentType("text/plain;charset=UTF-8");
            final PrintWriter writer;
//            try {
            writer = httpServletResponse.getWriter();
            writer.write("拦截了的返回");
            writer.flush();
            writer.close();
            throw new RuntimeException("拦截了");
//            } catch (Exception e) {
//                System.out.println("异常了");
//                throw new Exception(e);
//            }
        }
    }
}
