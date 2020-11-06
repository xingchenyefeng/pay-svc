package com.zhch.paysvc.utils;

/**
 * com.heren.appointment.config
 *
 * @author zhiwei
 * @create 2017-06-21 11:17.
 * @github {@https://github.com/loveqianqian}
 */
public enum HttpConfigEnum {

    CONTENT_TYPE("text/plain"),
    APPLICATION_JSON("application/json");

    private String name;
    
    HttpConfigEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}