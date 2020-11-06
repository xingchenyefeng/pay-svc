package com.zhch.paysvc.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;

/**
 * @author lumos
 * @date 2020/10/12 25
 */
@ErrorCode(value = "P60004",message = "退费操作出现异常")
public class TradeRefundException extends AbstractPayException {

    public TradeRefundException() {
    }

    public TradeRefundException(String message) {
        super(message);
    }

    public TradeRefundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeRefundException(Throwable cause) {
        super(cause);
    }

    public TradeRefundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
