package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "W0001", message = "微信认证失败，获取用户信息失败")
public class WeiChatAuthorFailureException extends AbstractPayException {
}
