package com.niuml.api;

import com.niuml.api.entity.User;

/***
 * @author niumengliang
 * Date:2024/9/14
 * Time:16:29
 */
public interface UserService {
    String getUserInfo();

    User getUserById(String userId);
}
