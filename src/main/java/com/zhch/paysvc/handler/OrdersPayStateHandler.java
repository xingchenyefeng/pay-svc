package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.IPayAgent;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradeQueryResponse;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ThinkPad on 2017/9/14.
 */

@Handler(code = "720001",name = "支付状态查询")
public class OrdersPayStateHandler extends AbstractMessageHandler {

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String tradeNo = requestMessage.getArgsPropertyStringValue("tradeNo");
        String transactionId = requestMessage.getArgsPropertyStringValue("transactionId");
        Assert.isTrue(StringUtils.hasLength(tradeNo) || StringUtils.hasLength(transactionId), "tradeNo 和transactionId 都为空");
        IPayAgent agent =getPayAgent();
        TradeQueryResponse queryResponse= agent.queryByTradeNo(channelCode,tradeNo,transactionId);
        responseMessage.setCode(queryResponse.getCode());
        responseMessage.setMessage(queryResponse.getMessage());
        if ("0".equals(queryResponse.getCode())) {
            Map<String, Object> data = new HashMap<>(16);
            data.put("transactionId",queryResponse.getTransactionId());
            data.put("tradeStateDesc", queryResponse.getTradeStateDesc());
            data.put("tradeNo", queryResponse.getTradeNo());
            data.put("tradeState", queryResponse.getTradeState());
            data.put("totalAmount",queryResponse.getTotalAmount());
            data.put("receiveAmount",queryResponse.getReceiveAmount());
            data.put("refundAmount",queryResponse.getRefundAmount());
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
