package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "P4009",message = "用户不存在")
public class UserNotFoundException extends AbstractPayException {
}
