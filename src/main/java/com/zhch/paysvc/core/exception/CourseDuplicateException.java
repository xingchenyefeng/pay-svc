package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 * @since 2020/8/2 上午7:16
 */
@ErrorCode(value = "C0001",message = "课程名重复")
public class CourseDuplicateException extends AbstractPayException {
}
