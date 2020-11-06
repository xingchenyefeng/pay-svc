package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author luoxiaoming
 */
@ErrorCode(value = "P4007",message = "认证失败或者已无效，请登录")
public class AuthFailedException extends AbstractPayException {

    private static final long serialVersionUID = -3569427442589707796L;
}
