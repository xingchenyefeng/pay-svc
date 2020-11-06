package com.zhch.paysvc.entrance;


import com.zhch.paysvc.support.MessageBuilder;
import com.zhch.paysvc.support.MessageProcessor;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@Log4j2
@RequestMapping("/gateway")
public class HttpEntrance {

    @Autowired
    private MessageProcessor processor;


    @PostMapping(value = "/service", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseMessage service(@RequestBody final String originalMsg) {
        log.info("request:" + originalMsg);
        ResponseMessage res;
        RequestMessage requestMessage = new RequestMessage(originalMsg);
        try {
            MessageBuilder.decoration(requestMessage);
            res = processor.handler(requestMessage);
        } catch (Exception e) {
            res = MessageBuilder.builderErrResMsg(requestMessage, e.getMessage());
            log.error("exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("response:" + res);
        return res;
    }


    @RequestMapping(value = "/wap", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String wap(HttpServletRequest request, HttpServletResponse response) {
        log.info("request:请求wap支付");
        String resultHtml = "";
        String prefix = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>pay plantform</title>\n" +
                "</head>\n" +
                "<body>";
        String end = "</body>\n" +
                "</html>";
        Map<String, Object> args = new HashMap<>();
        String channelCode = request.getParameter("channelCode");
        Assert.hasLength(channelCode, "channelCode 为空");
        args.put("channelCode", channelCode);
        String bsType = request.getParameter("bsType");
        Assert.hasLength(bsType, "bsType 为空");
        args.put("bsType", bsType);
        String subject = request.getParameter("subject");
        Assert.hasLength(subject, "subject 为空");
        args.put("subject", subject);
        String idCard = request.getParameter("idCard");
        if (StringUtils.hasLength(idCard)) {
            args.put("idCard", idCard);
        }
        String phone = request.getParameter("phone");
        if (StringUtils.hasLength(phone)) {
            args.put("phone", phone);
        }
        String name = request.getParameter("name");
        if (StringUtils.hasLength(name)) {
            args.put("name", name);
        }
        String outTradeNo = request.getParameter("outTradeNo");
        Assert.hasLength(outTradeNo, "outTradeNo 为空");
        args.put("outTradeNo", outTradeNo);
        String transIntroduce = request.getParameter("transIntroduce");
        if (StringUtils.hasLength(transIntroduce)) {
            args.put("transIntroduce", transIntroduce);
        }
        String totalAmount = request.getParameter("totalAmount");
        Assert.hasLength(totalAmount, "totalAmount 为空");
        args.put("totalAmount", totalAmount);
        String timeStart = request.getParameter("timeStart");
        if (StringUtils.hasLength(timeStart)) {
            args.put("timeStart", timeStart);
        }
        String deptCode = request.getParameter("deptCode");
        if (StringUtils.hasLength(deptCode)) {
            args.put("deptCode", deptCode);
        }
        String doctorCode = request.getParameter("doctorCode");
        if (StringUtils.hasLength(doctorCode)) {
            args.put("doctorCode", doctorCode);
        }
        String transDetail = request.getParameter("transDetail");
        if (StringUtils.hasLength(transDetail)) {
            args.put("transDetail", transDetail);
        }
        String deviceInfo = request.getParameter("deviceInfo");
        if (StringUtils.hasLength(deviceInfo)) {
            args.put("deviceInfo", deviceInfo);
        }
        ResponseMessage res = null;
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setFuncode("700006");
        requestMessage.setArgs(args);
        try {
            res = processor.handler(requestMessage);
        } catch (Exception e) {
            res = MessageBuilder.builderErrResMsg(requestMessage, e.getMessage());
            log.info("exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("response:" + "请求已跳转");
        String form = (String) res.getData().get("form");
        resultHtml = prefix + form + end;
        return resultHtml;
    }


    @RequestMapping(value = "/desService", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseMessage desService(@RequestBody final String originalMsg) {
        log.info("request:" + originalMsg);
        ResponseMessage res = null;
        RequestMessage requestMessage = new RequestMessage(originalMsg);
        try {
            MessageBuilder.decorationByDes(requestMessage);
            res = processor.handler(requestMessage);
        } catch (Exception e) {
            res = MessageBuilder.builderErrResMsg(requestMessage, e.getMessage());
            log.error("exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("response:" + res);
        return res;
    }

    @RequestMapping(value = "/notifies", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Map<String, Object> notifies(@RequestBody final String originalMsg) {
        log.info("request:" + originalMsg);
        ResponseMessage res = null;
        RequestMessage requestMessage = new RequestMessage(originalMsg);
        try {
            MessageBuilder.decorationXml(requestMessage);
            requestMessage.setFuncode("700101");
            requestMessage.setCaptcha("token");
            res = processor.handler(requestMessage);
        } catch (Exception e) {
            log.error("exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("response:" + res.getData());
        return res.getData();
    }
}
