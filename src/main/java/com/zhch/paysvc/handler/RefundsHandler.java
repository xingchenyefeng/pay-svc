package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.core.config.Constants;
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
 *
 * @author lumos
 * @date 2017/9/14
 */


@Handler(code = "710001", name = "统一退费（全退，冲正）")
public class RefundsHandler extends AbstractMessageHandler {


    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String tradeNo = requestMessage.getArgsPropertyStringValue("tradeNo");
        Assert.hasLength(tradeNo, "tradeNo 为空");
        String totalAmount = requestMessage.getArgsPropertyStringValue("totalAmount");
        Assert.isTrue(StringUtils.hasLength(totalAmount) && ValidateUtils.Number(totalAmount), "totalAmount 不正确");
        String refundTime = requestMessage.getArgsPropertyStringValue("refundTime");
        Assert.hasLength(refundTime, "refundTime 为空");
        String deviceInfo = requestMessage.getArgsPropertyStringValue("deviceInfo");
        Assert.hasLength(deviceInfo, "deviceInfo 为空");
        String refundDesc = requestMessage.getArgsPropertyStringValue("refundDesc");
        TradeRefundResponse tradeRefundResponse = getPayAgent().refundApply(channelCode, tradeNo, totalAmount, refundTime, deviceInfo, refundDesc);
        responseMessage.setCode(tradeRefundResponse.getCode());
        responseMessage.setMessage(tradeRefundResponse.getMessage());
        if (tradeRefundResponse.getCode().equals(Constants.SUCCESS)) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("refundAmount", tradeRefundResponse.getRefundAmount());
            data.put("tradeNo", tradeNo);
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
