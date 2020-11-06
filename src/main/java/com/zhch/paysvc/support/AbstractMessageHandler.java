package com.zhch.paysvc.support;

import com.zhch.paysvc.utils.MD5Util;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lumos
 * @date 2017/8/24
 */

public abstract class AbstractMessageHandler implements MessageHandler {


    private String functionCode;
    private String handlerName;
    private IPayAgent payAgent;


    @Override
    public String getFunctionCode() {
        return functionCode;
    }

    public AbstractMessageHandler() {
    }

    public AbstractMessageHandler(String functionCode, String handlerName) {
        this.functionCode = functionCode;
        this.handlerName = handlerName;
    }

    @Override
    public void setFunctionCode(String functionCode) {
        Assert.notNull(functionCode, "functionCode，不能为空.");
        this.functionCode = functionCode;
    }

    @Override
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override
    public String getHandlerName() {
        return handlerName;
    }

    @Override
    public IPayAgent getPayAgent() {
        return payAgent;
    }

    @Override
    public void setPayAgent(IPayAgent payAgent) {
        this.payAgent = payAgent;
    }

    protected abstract void paramsCheck();

    @Override
    public ResponseMessage handler(RequestMessage requestMessage) throws Exception {
        Assert.notNull(this.getFunctionCode(), "handler 的functionCode 为空");
        String funcode = requestMessage.getFuncode();
        String captcha = requestMessage.getCaptcha();
        Assert.isTrue(signCheck(requestMessage), "签名验证失败");
        if (this.getFunctionCode().equals(funcode)) {
            ResponseMessage responseMessage = this.innerHandler(requestMessage);
            Map<String, Object> data = responseMessage.getData();
            if (data != null) {
                String resMsgSign = MD5Util.string2MD5(data.toString());
                responseMessage.setDataAttribute("sign", resMsgSign);
            } else {
                responseMessage.setData(new HashMap<>());
            }
            return responseMessage;
        } else {
            throw new RuntimeException(this.getFunctionCode() + "messageHandler,被错误的指定为:" + funcode + "处理");
        }
    }

    protected abstract ResponseMessage innerHandler(RequestMessage requestMessage) throws Exception;


    protected abstract boolean signCheck(RequestMessage requestMessage);


    protected void getPropertiesFromInvokerResultMap(ResponseMessage responseMessage, Map<String, Object> result) {
        String code = (String) result.get("code");
        String messageIn = (String) result.get("messageIn");
        String messageOut = (String) result.get("messageOut");
        String message = (String) result.get("message");
        responseMessage.setCode(code);
        result.remove("code");
        responseMessage.setMessage(message);
        result.remove("message");
        responseMessage.setMessageIn(messageIn);
        result.remove("messageIn");
        responseMessage.setMessageOut(messageOut);
        result.remove("messageOut");
    }

}
