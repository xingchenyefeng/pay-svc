package com.zhch.paysvc.dto;

import com.zhch.paysvc.entity.ChannelConfig;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lumos
 * @date 2020/10/15 04
 */
@Setter
@Getter
@ApiModel("渠道信息")
public class ChannelConfigDto extends ChannelConfig {

    private String hospitalName;

    private String payName;

}
