package com.zhch.paysvc.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;

/**
 * @author lumos
 * @date 2020/10/12 25
 */
@ErrorCode(value = "P60001",message = "医院信息已经存在")
public class HospitalConfigDuplicateException extends AbstractPayException {
}
