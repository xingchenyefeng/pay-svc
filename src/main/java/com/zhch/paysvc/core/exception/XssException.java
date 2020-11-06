package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lomus
 */
@ErrorCode(value = "P4010", message = "跨域攻击异常")
public class XssException extends AbstractPayException {
    private static final long serialVersionUID = 8646248729966385504L;

    public XssException() {
    }

    public XssException(String message) {
        super(message);
    }

    public XssException(String message, Throwable cause) {
        super(message, cause);
    }

    public XssException(Throwable cause) {
        super(cause);
    }

    public XssException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
