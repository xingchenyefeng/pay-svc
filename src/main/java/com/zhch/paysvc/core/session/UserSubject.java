package com.zhch.paysvc.core.session;

import com.zhch.paysvc.core.config.ChannelType;
import com.zhch.paysvc.core.config.ClientType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lumos
 */
@Setter
@Getter
public class UserSubject implements Serializable {

    private static final long serialVersionUID = 3439155713129407371L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 登录时间
     */
    private Long loginDateTime;

    /**
     * 用户名称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 客户端类型
     */
    private ClientType clientType;
    /**
     * 终端类型
     */
    private ChannelType channelType;
    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备推送消息编码
     */
    private String deviceToken;
}
