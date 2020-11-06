package com.zhch.paysvc.dto;

import com.zhch.paysvc.core.web.request.PageQueryParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lumos
 * @date 2020/10/12 08
 */
@ApiModel("支付渠道查询参数对象")
@Setter
@Getter
public class ChannelConfigQueryParams extends PageQueryParams {


    @ApiModelProperty("医院名称")
    private String hospitalName;

    @ApiModelProperty("渠道代码")
    private String channelCode;

    @ApiModelProperty("支付类型")
    private String payType;

    @ApiModelProperty("状态")
    private Boolean active;

    @ApiModelProperty("注册开始时间")
    private String createdStartAt;

    @ApiModelProperty("注册结束时间")
    private String createdEndAt;
}
