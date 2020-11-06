package com.zhch.paysvc.core.config;

import com.zhch.paysvc.core.annotation.ErrorCode;
import com.zhch.paysvc.core.exception.AbstractPayException;
import com.zhch.paysvc.core.web.BaseRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;


/**
 * 客户端 + 服务端错误异常统一处理
 *
 * @author lumos
 */
@ConditionalOnClass(ServletException.class)
@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * BindException: controller参数绑定异常, 针对@Valid xxxDTO 的错误, 如类型转换异常造成的绑定异常
     * MethodArgumentNotValidException: controller参数绑定验证异常,如: 前端传输@RequestBody时
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {BindException.class})
    public BaseRes handleBindingException(BindException e) {
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P4001;
        logger.warn(errCodeEnum.getMessage(), e);
        String msg = handleBindingResult(e.getBindingResult());
        return new BaseRes().code(errCodeEnum.getCode()).message(msg);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public BaseRes handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P4004;
        logger.warn(errCodeEnum.getMessage(), e);
        String message = e.getMessage();
        return new BaseRes().code(errCodeEnum.getCode()).message(message);
    }


    /**
     * controller参数类型转换异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({UnexpectedTypeException.class, MethodArgumentTypeMismatchException.class})
    public BaseRes handleUnexpectedTypeException(Exception e) {
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P4002;
        logger.warn(errCodeEnum.getMessage(), e);
        String message = e.getMessage();
        return new BaseRes()
                .code(errCodeEnum.getCode())
                .message(message);
    }

    /**
     * 处理请求method不匹配
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseRes handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P4003;
        logger.warn(errCodeEnum.getMessage(), e);
        String message = e.getMessage();
        return new BaseRes()
                .code(errCodeEnum.getCode())
                .message(message);
    }


    /**
     * controller参数绑定异常, 针对@NotNull String name 这种情况
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServletRequestBindingException.class)
    public BaseRes handleServletRequestBindingException(ServletRequestBindingException e) {
        String message = e.getMessage();
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P5001;
        return new BaseRes().code(errCodeEnum.getCode()).message(message);
    }


    /**
     * controller参数绑定异常, 针对客户端传递json为空或者格式不正确 这种情况
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseRes handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = e.getMessage();
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P5001;
        return new BaseRes().code(errCodeEnum.getCode()).message(message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseRes handleValidException(ConstraintViolationException e) {
        List<String> messages = new ArrayList<>();
        e.getConstraintViolations().forEach((cv -> {
            messages.add(cv.getMessage());
        }));
        //判断是否是controller参数验证异常
        boolean controllerParamValidException = isControllerParamValidException(e);
        //如果不是controller的参数异常, 那就是service的参数验证异常
        //一般service参数验证不应该出异常, 如果有那就是程序写的有问题, 必须抛500了
        BaseRes res;
        ErrCodeEnum errCodeEnum;
        if (controllerParamValidException) {
            errCodeEnum = ErrCodeEnum.P5001;
        } else {
            errCodeEnum = ErrCodeEnum.P5002;
        }
        res = new BaseRes().code(errCodeEnum.getCode()).message(String.join(",", messages));
        return res;
    }

    /**
     * 判断该异常是controller参数异常还是service参数异常
     * 判断依据是异常堆栈信息中是否有service的package相关正则匹配
     * controller参数异常归为400错误
     * service参数异常归为500错误
     *
     * @param e
     * @return
     */
    private boolean isControllerParamValidException(ConstraintViolationException e) {
        boolean isControllerParamValidException = true;
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            boolean matched = (element.getClassName().endsWith("controller") || element.getClassName().endsWith("control"));
            if (matched) {
                isControllerParamValidException = false;
                break;
            }
        }
        return isControllerParamValidException;
    }


    //////////////////////  上面是 client 异常, 即参数没传对  ////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////  下面是 server 异常, 即业务或者运行时异常  ////////////////////////////////////////////////////

    /**
     * 所有自定义业务异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AbstractPayException.class)
    public BaseRes handleCommonBizException(AbstractPayException e, HttpServletRequest request) {
        logger.warn(e.getMessage(), e);
        ErrorCode errorCode = e.getClass().getAnnotation(ErrorCode.class);
        if (errorCode != null) {
            return new BaseRes()
                    .code(errorCode.value())
                    .message(errorCode.message());
        }
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P5001;
        return new BaseRes()
                .code(errCodeEnum.getCode())
                .message(errCodeEnum.getMessage());
    }


    /**
     * 通用服务器端运行时异常
     *
     * @param throwable
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseRes handleException(Throwable throwable) {
        ErrCodeEnum errCodeEnum = ErrCodeEnum.P5000;
        logger.error("server exception: ", throwable);
        String message = throwable.getMessage();
        /*
         * 内部异常处理
         */
        return new BaseRes()
                .code(errCodeEnum.getCode())
                .message(StringUtils.hasLength(message) ? message : throwable.getClass().getName());
    }


    private String handleBindingResult(BindingResult bindingResult) {
        List<String> errorMsg = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMsg.add(fieldError.getDefaultMessage());
            }
        }
        return String.join(",", errorMsg);
    }
}
