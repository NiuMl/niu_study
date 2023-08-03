package com.niuml.niu_study_spring_web_check.common;

import com.niuml.niu_study_spring_web_check.enums.BaseErrorInfoInterface;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:09
 */
@Data
public class Res {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    public static Res ok(String msg){
        return new Res("200",msg,null);
    }
    public static Res ok(Object result){
        return new Res("200","操作成功",result);
    }
    public static Res fail(BaseErrorInfoInterface infoInterface){
        return new Res(infoInterface.getResultCode(),infoInterface.getResultMsg(),null);
    }
    public static Res fail(String msg){
        return new Res("300",msg,null);
    }
    public static Res fail(SelfException e){
        return new Res(e.getErrorCode(),e.getErrorMsg(),null);
    }
    public static Res fail(CheckException e){
        return new Res(e.getErrorCode(),e.getErrorMsg(),null);
    }


    public Res(String code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
