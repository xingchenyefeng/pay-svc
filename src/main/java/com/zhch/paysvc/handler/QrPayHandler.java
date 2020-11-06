package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradePrecreateResponse;
import com.zhch.paysvc.utils.ValidateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lumos
 * @date 2017/11/12
 */

@Handler(name = "请求QR支付", code = "700001")
public class QrPayHandler extends AbstractMessageHandler {


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
        String outTradeNo = requestMessage.getArgsPropertyStringValue("outTradeNo");
        Assert.hasLength(outTradeNo, "outTradeNo 为空");
        String timeoutExpress = requestMessage.getArgsPropertyStringValue("timeoutExpress");
        Assert.isTrue(StringUtils.hasLength(timeoutExpress) && ValidateUtils.Integer(timeoutExpress), "timeoutExpress 参数不正确");
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
        TradePrecreateResponse response = getPayAgent().qrPay(channelCode, bsType, outTradeNo, totalAmount, subject, idCard, phone, name, transIntroduce, timeStart, deptCode, doctorCode, transDetail, deviceInfo, timeoutExpress);
        responseMessage.setCode(response.getCode());
        responseMessage.setMessage(response.getMessage());
        if (response.getCode().equals("0")) {
            Map<String, Object> data = new HashMap<>();
            data.put("totalAmount", response.getTotalAmount());
            data.put("codeUrl", response.getCodeUrl());
            data.put("timeStart", response.getTimeStart());
            data.put("tradeNo", response.getTradeNo());
            data.put("phone", phone);
            data.put("idCard", idCard);
            data.put("outTradeNo", response.getOutTradeNo());
            data.put("payUrl", response.getPayUrl());
            data.put("bsType", bsType);
            responseMessage.setData(data);
        }
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }

    @Override
    protected void paramsCheck() {

    }
}
