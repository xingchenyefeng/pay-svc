package com.zhch.paysvc.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;

/**
 * @author lumos
 * @date 2020/10/12 25
 */
@ErrorCode(value = "P60005",message = "查询同步操作出现异常")
public class TradeSyncException extends AbstractPayException {

    public TradeSyncException() {
    }

    public TradeSyncException(String message) {
        super(message);
    }

    public TradeSyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeSyncException(Throwable cause) {
        super(cause);
    }

    public TradeSyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
