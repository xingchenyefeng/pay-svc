package com.zhch.paysvc.core.exception;

import com.zhch.paysvc.core.annotation.ErrorCode;

/**
 * @author lumos
 */
@ErrorCode(value = "E00001",message = "视频资源文件不存在")
public class VideoSourceFileNotExistsException extends AbstractPayException {

    public VideoSourceFileNotExistsException() {
    }

    public VideoSourceFileNotExistsException(String message) {
        super(message);
    }

    public VideoSourceFileNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoSourceFileNotExistsException(Throwable cause) {
        super(cause);
    }

    public VideoSourceFileNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
