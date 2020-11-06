package com.zhch.paysvc.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;

/**
 * @author lumos
 * @date 2020/10/12 25
 */
@ErrorCode(value = "U00001",message = "账号或者密码错误")
public class NotPermissionException extends AbstractPayException {

    public NotPermissionException() {
    }

    public NotPermissionException(String message) {
        super(message);
    }

    public NotPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotPermissionException(Throwable cause) {
        super(cause);
    }

    public NotPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
