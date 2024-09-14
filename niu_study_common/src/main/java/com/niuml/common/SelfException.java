package com.niuml.common;

import com.niuml.enums.BaseErrorInfoInterface;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:19
 */

@Data
public class SelfException extends RuntimeException{
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public SelfException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getResultCode());
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public SelfException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
