package com.zhch.paysvc.core.config;


/**
 * 请求头部需要用到的属性
 *
 * @author lumos
 * @since 2020/8/3 上午10:28
 */
public enum HeadersEnum {

    /**
     * jwt 内部验权使用
     */
    JWT("jwt"),

    /**
     * 终端类型属性
     */
    CHANNEL_TYPE("channel-type"),

    /**
     * 用户类型属性
     */
    CLIENT_TYPE("client-type"),

    /**
     * 序列属性 每次请求的唯一标记
     */
    SERIAL("serial");

    private final String header;

    HeadersEnum(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

}
