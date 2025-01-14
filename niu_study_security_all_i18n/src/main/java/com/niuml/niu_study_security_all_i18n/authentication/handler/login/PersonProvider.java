package com.niuml.niu_study_security_all_i18n.authentication.handler.login;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/***
 * @author niumengliang
 * Date:2024/12/12
 * Time:10:29
 */
public class PersonProvider {

    protected List<SimpleGrantedAuthority> handDbPre(List<String> authorities) {
        List<SimpleGrantedAuthority> sga = new ArrayList<>();
        if (authorities != null) {
            for (String role : authorities) {
                sga.add(new SimpleGrantedAuthority(role));
            }
        }
        return sga;
    }
}
