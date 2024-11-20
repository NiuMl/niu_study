package com.niuml.niu_study_security.auth.handler.resourceapi;

import com.niuml.niu_study_security.common.exception.InterAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

/***
 * @author niumengliang
 * Date:2024/11/20
 * Time:09:18
 */
public abstract class InterfaceLevelFilter extends OncePerRequestFilter {

    protected void judgeRequestInter(HttpServletRequest request, List<String> interfaces){

        String uri = request.getRequestURI();
        if (!interfaces.contains(uri)){
            throw new InterAuthenticationException("接口受限");
        }

    }
}
