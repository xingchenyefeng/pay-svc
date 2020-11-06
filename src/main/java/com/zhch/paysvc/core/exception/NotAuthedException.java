package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "P4007",message = "认证失败或者已无效，请登录")
public class NotAuthedException extends AbstractPayException {
}
