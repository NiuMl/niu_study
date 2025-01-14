package com.niuml.niu_study_security_all_i18n.authentication.config;

import com.niuml.niu_study_security_all_i18n.authentication.handler.common.LoginFailHandler;
import com.niuml.niu_study_security_all_i18n.authentication.handler.common.LoginSuccessHandler;
import com.niuml.niu_study_security_all_i18n.authentication.handler.exception.CustomAuthenticationExceptionHandler;
import com.niuml.niu_study_security_all_i18n.authentication.handler.exception.CustomAuthorizationExceptionHandler;
import com.niuml.niu_study_security_all_i18n.authentication.handler.exception.CustomSecurityExceptionHandler;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.sms.SmsAuthenticationFilter;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.sms.SmsAuthenticationProvider;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.userName.UserNameAuthenticationFilter;
import com.niuml.niu_study_security_all_i18n.authentication.handler.login.userName.UserNameAuthenticationProvider;
import com.niuml.niu_study_security_all_i18n.authentication.handler.requestBefore.rb1.JwtAuthenticationFilter;
import jakarta.servlet.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:15:43
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final ApplicationContext applicationContext;

    private final AuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationExceptionHandler();
    private final AccessDeniedHandler accessDeniedHandler = new CustomAuthorizationExceptionHandler();
    private final Filter globalSpringSecurityExceptionHandler = new CustomSecurityExceptionHandler();


    public WebSecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void commonHttpSetting(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
                .requestCache(cache -> cache
                        .requestCache(new NullRequestCache())
                )
                // 无需给用户一个匿名身份
                .anonymous(AbstractHttpConfigurer::disable);
        // 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        // 鉴权失败异常
                        .accessDeniedHandler(accessDeniedHandler)
                        // 认证失败异常
                        .authenticationEntryPoint(authenticationEntryPoint)
        );
        // 其他未知异常. 尽量提前加载。
        http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);
    }

    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        http
                .securityMatcher("/public/**","/user/login/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll());

        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);
        // 加一个登录方式。用户名、密码登录
        UserNameAuthenticationFilter usernameLoginFilter = new UserNameAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/username", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(UserNameAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
        http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);
        // 加一个登录方式。短信验证码 登录
        SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/sms", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
        http.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 定义一个可以无需验证的规则
     * 如果不这么写，直接写到上面的Chain里面 会有 Can't configure anyRequest after itself问题
     *
     */
//    @Bean
//    @Order(1)
//    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
//        commonHttpSetting(http);
//        http
//                .securityMatcher("/public/**")
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain myApiFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        JwtAuthenticationFilter openApi1Filter = new JwtAuthenticationFilter();
        // 加一个登录方式。用户名、密码登录
        http.addFilterBefore(openApi1Filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
