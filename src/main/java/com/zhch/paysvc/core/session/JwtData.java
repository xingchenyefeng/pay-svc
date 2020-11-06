package com.zhch.paysvc.core.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lumos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtData implements Serializable {

    private static final long serialVersionUID = 646887077025145274L;

    private String userId;
    private String channelType;
    private String clientType;
    private Long loginTime;

}
