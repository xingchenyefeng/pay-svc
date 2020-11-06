package com.zhch.paysvc.core.annotation;

import java.lang.annotation.*;


/**
 * @author lumos
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ErrorCode {

    String value();

    String message() default "";

}
