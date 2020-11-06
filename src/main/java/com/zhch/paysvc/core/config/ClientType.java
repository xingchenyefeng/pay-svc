package com.zhch.paysvc.core.config;

import org.springframework.util.StringUtils;

/**
 * @author lumos
 */
public enum ClientType {
    /**
     * 中台用户
     */
    MANAGER("manager"),

    USER("user"),

    ANONYMOUS("anonymous");

    ClientType(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public static ClientType getClientType(String value) {
        if (StringUtils.hasLength(value)) {
            for (ClientType clientType : ClientType.values()) {
                if (clientType.getValue().equals(StringUtils.trimWhitespace(value))) {
                    return clientType;
                }
            }
        }
        return null;
    }
}
