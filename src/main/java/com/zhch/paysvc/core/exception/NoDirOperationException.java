package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "E00002",message = "文件夹创建失败")
public class NoDirOperationException extends AbstractPayException {

    public NoDirOperationException() {
    }

    public NoDirOperationException(String message) {
        super(message);
    }

    public NoDirOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDirOperationException(Throwable cause) {
        super(cause);
    }

    public NoDirOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
