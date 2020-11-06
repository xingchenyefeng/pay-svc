package com.zhch.paysvc.support.bean;


import java.io.Serializable;

/**
 * Created by ThinkPad on 2017/11/19.
 */
public class TradeResponse implements Serializable{
    private static final long serialVersionUID = -7990478651708634822L;

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
