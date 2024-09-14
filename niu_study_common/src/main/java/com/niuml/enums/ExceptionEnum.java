package com.niuml.enums;

/***
 * User:niumengliang
 * Date:2023/8/3
 * Time:15:21
 */
public enum ExceptionEnum implements BaseErrorInfoInterface{
    // 数据操作错误定义
    SUCCESS("2000", "成功!"),
    BODY_NOT_MATCH("4000","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001","请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    JSON_ERROR("4005", "传输数据格式异常，非json格式"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    SERVER_BUSY("5003","服务器正忙，请稍后再试!")
    ;

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String code, String message) {
        this.resultCode = code;
        this.resultMsg = message;
    }


    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
