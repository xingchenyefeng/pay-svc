package com.zhch.paysvc.core.exception;


/**
 * 基本异常抽象类
 *
 * @author lumos
 */
public abstract class AbstractPayException extends RuntimeException {
    private static final long serialVersionUID = -6894083000864900896L;

    public AbstractPayException() {
    }

    public AbstractPayException(String message) {
        super(message);
    }

    public AbstractPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractPayException(Throwable cause) {
        super(cause);
    }

    public AbstractPayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
