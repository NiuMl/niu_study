package com.niuml.niu_study_spring_web_check.common;

import com.niuml.niu_study_spring_web_check.enums.BaseErrorInfoInterface;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:19
 */

@Data
public class CheckException extends RuntimeException{
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public CheckException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getResultCode());
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public CheckException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
