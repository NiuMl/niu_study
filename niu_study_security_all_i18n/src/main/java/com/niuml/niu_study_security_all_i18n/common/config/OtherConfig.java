package com.niuml.niu_study_security_all_i18n.common.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

/***
 * @author niumengliang
 * Date:2024/12/17
 * Time:16:59
 */
@Configuration
public class OtherConfig {

    /**
     * 使用 @Async 注解的时候用虚拟线程执行你的 @Async 任务。
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor(){
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * tomcat 使用虚拟线程池
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
