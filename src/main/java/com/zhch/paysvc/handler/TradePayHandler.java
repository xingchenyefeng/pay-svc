package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradePayResponse;
import com.zhch.paysvc.utils.ValidateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lumos
 * @date 2017/11/23
 */



@Handler(code = "700002",name = "扫码付")
public class TradePayHandler extends AbstractMessageHandler {

    @Override
    protected void paramsCheck() {

    }

    @Override
    protected ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        String channelCode = requestMessage.getArgsPropertyStringValue("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        String bsType = requestMessage.getArgsPropertyStringValue("bsType");
        Assert.hasLength(bsType, "bsType 为空");
        String subject = requestMessage.getArgsPropertyStringValue("subject");
        Assert.hasLength(subject, "subject 为空");
        String idCard = requestMessage.getArgsPropertyStringValue("idCard");
        String phone = requestMessage.getArgsPropertyStringValue("phone");
        String name = requestMessage.getArgsPropertyStringValue("name");
        String outTradeNo = requestMessage.getArgsPropertyStringValue("outTradeNo");
        Assert.hasLength(outTradeNo, "outTradeNo 为空");
        String authCode = requestMessage.getArgsPropertyStringValue("authCode");
        Assert.hasLength(authCode,"authCode 为空");
        String transIntroduce = requestMessage.getArgsPropertyStringValue("transIntroduce");
        String totalAmount = requestMessage.getArgsPropertyStringValue("totalAmount");
        Assert.isTrue(StringUtils.hasLength(totalAmount) && ValidateUtils.Number(totalAmount), "totalAmount 不正确");
        String timeStart = requestMessage.getArgsPropertyStringValue("timeStart");
        Assert.hasLength(timeStart, "timeStart 为空");
        String deptCode = requestMessage.getArgsPropertyStringValue("deptCode");
        String doctorCode = requestMessage.getArgsPropertyStringValue("doctorCode");
        String transDetail = requestMessage.getArgsPropertyStringValue("transDetail");
        String deviceInfo = requestMessage.getArgsPropertyStringValue("deviceInfo");
        Assert.hasLength(deviceInfo, "deviceInfo 为空");
        TradePayResponse response = getPayAgent().tradePay(channelCode, bsType, outTradeNo, totalAmount, subject, idCard, phone, name, transIntroduce, timeStart, deptCode, doctorCode, transDetail, deviceInfo, authCode);
        responseMessage.setCode(response.getCode());
        responseMessage.setMessage(response.getMessage());
        if (response.getCode().equals("0")) {
            Map<String, Object> data = new HashMap<>();
            data.put("totalAmount", response.getTotalAmount());
            data.put("timeStart", response.getTimeStart());
            data.put("tradeNo", response.getTradeNo());
            data.put("phone", phone);
            data.put("idCard", idCard);
            data.put("outTradeNo", response.getOutTradeNo());
            data.put("bsType", bsType);
            data.put("tradeState",response.getTradeState());
            data.put("tradeStateDesc",response.getTradeStateDesc());
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
