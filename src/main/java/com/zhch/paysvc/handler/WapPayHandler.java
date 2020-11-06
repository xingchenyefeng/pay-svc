package com.zhch.paysvc.handler;

import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.support.AbstractMessageHandler;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.support.bean.TradePrecreateResponse;
import com.zhch.paysvc.utils.ValidateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 *
 * @author lumos
 * @date 2017/11/20
 */
@Handler(code = "700006",name = "H5支付台")
public class WapPayHandler extends AbstractMessageHandler {

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

        String transIntroduce = requestMessage.getArgsPropertyStringValue("transIntroduce");
        String totalAmount = requestMessage.getArgsPropertyStringValue("totalAmount");
        Assert.isTrue(StringUtils.hasLength(totalAmount) && ValidateUtils.Number(totalAmount), "totalAmount 不正确");
        String timeStart = requestMessage.getArgsPropertyStringValue("timeStart");
        Assert.hasLength(timeStart, "timeStart 为空");
        String deptCode = requestMessage.getArgsPropertyStringValue("deptCode");
        String doctorCode = requestMessage.getArgsPropertyStringValue("doctorCode");
        String transDetail = requestMessage.getArgsPropertyStringValue("transDetail");

        String deviceInfo = requestMessage.getArgsPropertyStringValue("deviceInfo");

        TradePrecreateResponse response = getPayAgent().wapPayCreate(channelCode, bsType, outTradeNo, totalAmount, subject, idCard, phone, name, transIntroduce, timeStart, deptCode, doctorCode, transDetail, deviceInfo);
        responseMessage.setCode(response.getCode());
        responseMessage.setMessage(response.getMessage());
        responseMessage.setDataAttribute("form",response.getForm());
        return responseMessage;
    }

    @Override
    protected boolean signCheck(RequestMessage requestMessage) {
        return true;
    }
}
