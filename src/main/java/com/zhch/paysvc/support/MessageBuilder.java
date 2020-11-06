package com.zhch.paysvc.support;

import com.alibaba.fastjson.JSONObject;
import com.zhch.paysvc.utils.DesUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/8/14.
 */
public class MessageBuilder {

    public static RequestMessage builder(JSONObject jsonObject) {
        RequestMessage requestMessage = null;
        String captcha = jsonObject.getString("captcha");
        String funcode = jsonObject.getString("funcode");
        Map<String, Object> args = jsonObject.getJSONObject("args");
        requestMessage = new RequestMessage(captcha, funcode, args);
        return requestMessage;
    }

    public static RequestMessage decoration(RequestMessage requestMessage) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(requestMessage.getOriginalMsg());
        String captcha = jsonObject.getString("captcha");
        String funcode = jsonObject.getString("funcode");
        Assert.notNull(funcode, "funcode 不能为空.");
        Map<String, Object> args = jsonObject.getJSONObject("args");
        requestMessage.setArgs(args);
        requestMessage.setCaptcha(captcha);
        requestMessage.setFuncode(funcode);
        return requestMessage;
    }


    public static RequestMessage decorationXml(RequestMessage requestMessage) throws Exception {
        Document document = DocumentHelper.parseText(requestMessage.getOriginalMsg());
        List<Element> elements = document.getRootElement().elements();
        if (elements != null) {
            Map<String, Object> args = new HashMap<>();
            for (Element element : elements) {
                args.put(element.getName(), element.getTextTrim());
            }
            requestMessage.setArgs(args);
        }
        return requestMessage;
    }


    public static ResponseMessage builderErrResMsg(RequestMessage requestMessage, String message) {
        ResponseMessage responseMessage = new ResponseMessage(requestMessage.getId());
        if (!StringUtils.hasLength(message)) {
            message = "空指针异常.";
        }
        responseMessage.setCode("-1");
        responseMessage.setMessage(message);
        responseMessage.setMessageIn(message);
        responseMessage.setMessageOut(message);
        responseMessage.setData(new HashMap<>());
        return responseMessage;
    }

    public static RequestMessage decorationByDes(RequestMessage requestMessage) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(requestMessage.getOriginalMsg());
        String captcha = jsonObject.getString("captcha");
        String funcode = jsonObject.getString("funcode");
        Assert.notNull(funcode, "funcode 不能为空.");
        String temp = DesUtil.decrypt(jsonObject.getString("args"));
        JSONObject args = JSONObject.parseObject(temp);
        requestMessage.setArgs(args);
        requestMessage.setCaptcha(captcha);
        requestMessage.setFuncode(funcode);
        requestMessage.setDecode(true);
        return requestMessage;
    }
}
