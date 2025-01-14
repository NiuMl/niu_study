package com.niuml.niu_study_security_all_i18n.common.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:15:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException{

    private int code;
    private String message;
    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
