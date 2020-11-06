package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "P4011",message = "您需要购买此课程")
public class NotPurchaseOfCourseException extends AbstractPayException {

    private static final long serialVersionUID = 344464342702471297L;

    public NotPurchaseOfCourseException() {
    }

    public NotPurchaseOfCourseException(String message) {
        super(message);
    }

    public NotPurchaseOfCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotPurchaseOfCourseException(Throwable cause) {
        super(cause);
    }

    public NotPurchaseOfCourseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
