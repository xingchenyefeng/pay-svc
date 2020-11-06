package com.zhch.paysvc.handler;


import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.BillContainer;
import com.zhch.paysvc.support.bean.TradeRecordResponse;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lumos
 * @date 2017/11/27
 */
@Handler(code = "790001", name = "对账")
public class ReconciliationHandler extends AbstractMessageHandler {

    @Override
    protected void paramsCheck() {
    }

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String billDate = requestMessage.getArgsPropertyStringValue("billDate");
        TradeRecordResponse tradeRecordResponse = getPayAgent().billDownload(channelCode, billDate);
        responseMessage.setCode(tradeRecordResponse.getCode());
        responseMessage.setMessage(tradeRecordResponse.getMessage());
        if (tradeRecordResponse.getCode().equals("0")) {
            Map<String, Object> data = new HashMap<>();
            BillContainer container = tradeRecordResponse.getBillContainer();
            data.put("state",container.getState());
            data.put("stateDesc",container.getStateDesc());
            if (container.getState().equals("0")){
                data.put("bills", tradeRecordResponse.getBillContainer().getBill());
            }
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
