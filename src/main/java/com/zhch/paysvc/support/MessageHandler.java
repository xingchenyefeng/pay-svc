package com.zhch.paysvc.support;


/**
 * Created by ThinkPad on 2017/8/17.
 */
public interface MessageHandler {

    ResponseMessage handler(final RequestMessage requestMessage) throws Exception;

    String getFunctionCode();

    void setFunctionCode(String functionCode);

    String getHandlerName();

    void setHandlerName(String handlerName);

    IPayAgent getPayAgent();

    void setPayAgent(IPayAgent payAgent);

}
