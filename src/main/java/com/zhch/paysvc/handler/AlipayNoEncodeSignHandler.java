package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradeSignResponse;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lumos
 * @date 2018/2/5
 */


@Handler(name = "支付宝应用加签(非encode)", code = "750002")
public class AlipayNoEncodeSignHandler extends AbstractMessageHandler {

    @Override
    protected void paramsCheck() {

    }

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String signContent = requestMessage.getArgsPropertyStringValue("signContent");
        TradeSignResponse tradeSignResponse = getPayAgent().doAndriodSign(channelCode, signContent);
        responseMessage.setCode(tradeSignResponse.getCode());
        responseMessage.setMessage(tradeSignResponse.getMessage());
        if (tradeSignResponse.getCode().equals("0")) {
            Map<String, Object> data = new HashMap<>();
            data.put("signResult", tradeSignResponse.getSign());
            data.put("signType", tradeSignResponse.getSignType());
            data.put("charset", tradeSignResponse.getCharset());
            data.put("version", tradeSignResponse.getVersion());
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
