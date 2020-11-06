package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradeRefundResponse;
import com.zhch.paysvc.utils.ValidateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lumos
 */
@Handler(code = "710002", name = "统一退费接口，支付部分退费")
public class RefundsPartHandler extends AbstractMessageHandler {


    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String tradeNo = requestMessage.getArgsPropertyStringValue("tradeNo");
        String transactionId = requestMessage.getArgsPropertyStringValue("transactionId");
        Assert.isTrue(StringUtils.hasLength(tradeNo) || StringUtils.hasLength(transactionId), "tradeNo 和 transactionId 不能同时为空");
        String totalAmount = requestMessage.getArgsPropertyStringValue("totalAmount");
        Assert.notNull(totalAmount,"totalAmount 为空.");
        String outRequestNo = requestMessage.getArgsPropertyStringValue("outRequestNo");
        Assert.notNull(outRequestNo,"outRequestNo 为空.");
        String refundAmount = requestMessage.getArgsPropertyStringValue("refundAmount");
        Assert.isTrue(StringUtils.hasLength(refundAmount) && ValidateUtils.Number(refundAmount), "refundAmount 不正确");
        String refundTime = requestMessage.getArgsPropertyStringValue("refundTime");
        Assert.hasLength(refundTime, "refundTime 为空");
        String deviceInfo = requestMessage.getArgsPropertyStringValue("deviceInfo");
        Assert.hasLength(deviceInfo, "deviceInfo 为空");
        String refundDesc = requestMessage.getArgsPropertyStringValue("refundDesc");
        TradeRefundResponse tradeRefundResponse = null;
        if (StringUtils.hasLength(tradeNo)){
            tradeRefundResponse = getPayAgent().refundPartApply(channelCode, tradeNo, totalAmount,refundAmount,outRequestNo, refundTime, deviceInfo, refundDesc);
        }else {
             tradeRefundResponse = getPayAgent().refundPartApplyByTransactionId(channelCode, transactionId, totalAmount,refundAmount,outRequestNo, refundTime, deviceInfo, refundDesc);
        }
        responseMessage.setCode(tradeRefundResponse.getCode());
        responseMessage.setMessage(tradeRefundResponse.getMessage());
        if (tradeRefundResponse.getCode().equals("0")) {
            Map<String, Object> data = new HashMap<>();
            data.put("refundAmount", tradeRefundResponse.getRefundAmount());
            data.put("outRequestNo",outRequestNo);
            data.put("tradeNo", tradeRefundResponse.getTradeNo());
            data.put("transactionId",tradeRefundResponse.getTransactionId());
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected void paramsCheck() {

    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
