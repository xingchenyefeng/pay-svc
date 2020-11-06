package com.zhch.paysvc.core.web.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lumos
 */
@Data
public class RequestInfo implements Serializable {
    private static final long serialVersionUID = -1217312267782217222L;

    private String userId;
    private String serial;
    private String jwt;
    private String clientHost;
    private String clientType;
    private String channelType;
    private long timestamp;

}
