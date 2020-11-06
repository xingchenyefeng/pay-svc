package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "P4000", message = "缺少请求参数")
public class RequestExtractParamsException extends AbstractPayException {
    private static final long serialVersionUID = -2313573333896333997L;
}
