package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "P4008", message = "您没有权限访问此接口")
public class NoPermissionException extends AbstractPayException {
    private static final long serialVersionUID = 6905095667946337019L;
}
