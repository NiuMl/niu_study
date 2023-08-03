package com.niuml.niu_study_spring_web_check.enums;

/***
 * User:niumengliang
 * Date:2023/8/3
 * Time:15:21
 */
public interface BaseErrorInfoInterface {
    /**
     *  错误码
     * @return
     */
    String getResultCode();

    /**
     * 错误描述
     * @return
     */
    String getResultMsg();
}
