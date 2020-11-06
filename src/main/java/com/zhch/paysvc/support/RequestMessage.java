package com.zhch.paysvc.support;


import com.zhch.paysvc.utils.IdGenerator;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 报文模板类
 * Created by luoxiaoming on 2017/8/14.
 */
public class RequestMessage {

    private String id;
    private String captcha; //签名token
    private boolean decode;
    private Map<String, Object> args;//报文体参数
    private String funcode;//接口编号
    private String originalMsg;


    public RequestMessage(){
        this.id =  IdGenerator.getUUID();
    }

    public RequestMessage(String originalMsg) {
        this();
        this.originalMsg = originalMsg;
    }

    public RequestMessage(String captcha, String funcode, Map<String, Object> args) {
        this();
        Assert.notNull(captcha, "captcha 参数不能为空.");
        Assert.notNull(funcode, "funcode 参数不能为空.");
        Assert.notNull(args, "args 参数不能为空.");
        this.captcha = captcha;
        this.funcode = funcode;
        this.args = args;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public String getArgsPropertyStringValue(String property){
        return (String) args.get(property);
    }

    public String getId() {
        return id;
    }

    public String getFuncode() {
        return funcode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public String getOriginalMsg() {
        return originalMsg;
    }

    public void setOriginalMsg(String originalMsg) {
        this.originalMsg = originalMsg;
    }

    public boolean isDecode() {
        return decode;
    }

    public void setDecode(boolean decode) {
        this.decode = decode;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "id='" + id + '\'' +
                ", captcha='" + captcha + '\'' +
                ", decode=" + decode +
                ", args=" + args +
                ", funcode='" + funcode + '\'' +
                ", originalMsg='" + originalMsg + '\'' +
                '}';
    }
}
