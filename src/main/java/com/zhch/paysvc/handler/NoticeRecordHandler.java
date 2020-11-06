package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradeQueryResponse;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


@Handler(code = "770001",name = "接收收费记录")
public class NoticeRecordHandler extends AbstractMessageHandler {

    @Override
    protected void paramsCheck() {

    }

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String bsType = requestMessage.getArgsPropertyStringValue("bsType");
        String subject = requestMessage.getArgsPropertyStringValue("subject");
        Assert.hasLength(subject, "subject 为空");
        String idCard = requestMessage.getArgsPropertyStringValue("idCard");
        String phone = requestMessage.getArgsPropertyStringValue("phone");
        String name = requestMessage.getArgsPropertyStringValue("name");
        String tradeNo = requestMessage.getArgsPropertyStringValue("tradeNo");
        Assert.hasLength(tradeNo, "tradeNo 为空");
        String outTradeNo = requestMessage.getArgsPropertyStringValue("outTradeNo");
        String transIntroduce = requestMessage.getArgsPropertyStringValue("transIntroduce");
        String totalAmount = requestMessage.getArgsPropertyStringValue("totalAmount");
        String timeStart = requestMessage.getArgsPropertyStringValue("timeStart");
        Assert.hasLength(timeStart, "timeStart 为空");
        String deptCode = requestMessage.getArgsPropertyStringValue("deptCode");
        String doctorCode = requestMessage.getArgsPropertyStringValue("doctorCode");
        String transDetail = requestMessage.getArgsPropertyStringValue("transDetail");
        String deviceInfo = requestMessage.getArgsPropertyStringValue("deviceInfo");
        Assert.hasLength(deviceInfo, "deviceInfo 为空");
        TradeQueryResponse response = getPayAgent().noticeComplete(channelCode, bsType, tradeNo,outTradeNo, totalAmount, subject, idCard, phone, name, transIntroduce, timeStart, deptCode, doctorCode, transDetail, deviceInfo);
        responseMessage.setCode(response.getCode());
        responseMessage.setMessage(response.getMessage());
        if ("0".equals(response.getCode())) {
            Map<String, Object> data = new HashMap<>();
            data.put("totalAmount", response.getTotalAmount());
            data.put("tradeNo", response.getTradeNo());
            data.put("phone", phone);
            data.put("idCard", idCard);
            data.put("outTradeNo", outTradeNo);
            data.put("bsType", bsType);
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
