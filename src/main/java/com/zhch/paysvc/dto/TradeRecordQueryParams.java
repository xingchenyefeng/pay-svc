package com.zhch.paysvc.dto;

import com.zhch.paysvc.core.web.request.PageQueryParams;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lumos
 * @date 2020/10/18 09
 */
@Setter
@Getter
public class TradeRecordQueryParams extends PageQueryParams {

    private String tradeNo;
    private String transactionId;
    private String channelCode;
    private String hospitalCode;
    private String outTradeNo;
    private String phone;
    private String name;
    private String payType;
    private String state;
    private String createdStartAt;
    private String createdEndAt;

}
