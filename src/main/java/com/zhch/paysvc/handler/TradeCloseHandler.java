package com.zhch.paysvc.handler;


import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradeCloseResponse;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lumos
 * @date 2017/11/21
 */

@Deprecated
@Handler(code = "730001", name = "统一交易关闭")
public class TradeCloseHandler extends AbstractMessageHandler {

    @Override
    protected void paramsCheck() {

    }

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String tradeNo = requestMessage.getArgsPropertyStringValue("tradeNo");
        Assert.hasLength(tradeNo, "tradeNo 为空");
        TradeCloseResponse closeResponse = getPayAgent().closeTrade(channelCode, tradeNo);
        responseMessage.setCode(closeResponse.getCode());
        responseMessage.setMessage(closeResponse.getMessage());

        if (!closeResponse.getCode().equals("1")) {
            Map<String, Object> data = new HashMap<>();
            data.put("tradeNo", tradeNo);
            data.put("tradeState",closeResponse.getTradeState());
            data.put("tradeStateDesc",closeResponse.getTradeStateDesc());
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
