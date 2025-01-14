package com.niuml.niu_study_security_all_i18n.common.codes;

/***
 * @author niumengliang
 * Date:2024/12/11
 * Time:15:59
 */
public class ExceptionCode {

    /**
     * 9开头的为系统级别错误编码
     */
    public static final int SYSTEM_ERROR = 999;

    /**
     * 8开头的为登录、鉴权错误编码
     */
    public static final int AUTHORIZE_ERROR = 800;//登录成功，授权失败
    public static final int TOKEN_LOSE = 801;//token失效
    public static final int TOKEN_ANALYSIS_ERROR = 802;//解析token失败
    public static final int TOKEN_MISS = 803;//token为空

    /**
     * 7开头的为用户系列
     */
    public static final int USER_DATA_ERROR = 700;//用户数据异常
}
