package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.web.BaseRes;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luoxiaoming
 * @since 2020/7/30 下午3:28
 */
@Setter
@Getter
public class FeignApiInvokeException extends AbstractPayException {

    private BaseRes baseRes;

    public FeignApiInvokeException() {
    }

    public FeignApiInvokeException(String message) {
        super(message);
    }

    public FeignApiInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignApiInvokeException(Throwable cause) {
        super(cause);
    }

    public FeignApiInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
