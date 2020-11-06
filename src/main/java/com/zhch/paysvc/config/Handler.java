package com.zhch.paysvc.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author lumos
 * @date 2017/11/12
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Handler {


    String name();

    String code();

}
