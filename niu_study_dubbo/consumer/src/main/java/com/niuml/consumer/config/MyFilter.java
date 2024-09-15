package com.niuml.consumer.config;

import jakarta.servlet.*;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.put("TRACKING_LOG_SESSION_TOKEN_ID", UUID.randomUUID().toString().toUpperCase());
        try {
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        } finally {
            MDC.remove("TRACKING_LOG_SESSION_TOKEN_ID");
        }

    }

}
