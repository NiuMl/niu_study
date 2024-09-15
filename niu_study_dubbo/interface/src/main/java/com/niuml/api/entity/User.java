package com.niuml.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/***
 * @author niumengliang
 * Date:2024/9/14
 * Time:16:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -4294369157631461921L;
    Long userId;
    String userName;
    String userInfo;
}
