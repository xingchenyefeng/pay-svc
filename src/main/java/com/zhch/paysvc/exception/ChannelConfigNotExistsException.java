package com.zhch.paysvc.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;

/**
 * @author lumos
 * @date 2020/10/12 25
 */
@ErrorCode(value = "P60003",message = "渠道信息未找到")
public class ChannelConfigNotExistsException extends AbstractPayException {

    public ChannelConfigNotExistsException() {
    }

    public ChannelConfigNotExistsException(String message) {
        super(message);
    }

    public ChannelConfigNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelConfigNotExistsException(Throwable cause) {
        super(cause);
    }

    public ChannelConfigNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
