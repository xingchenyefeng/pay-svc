package com.zhch.paysvc.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.IdGenerator;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lumos
 * @date 2017/8/14
 */
public class ResponseMessage {


    public static final String RES_CODE = "code";
    public static final String MESSAGE_IN = "messageIn";
    public static final String MESSAGE_OUT = "messageOut";
    public static final String MESSAGE = "message";


    private final String id;
    /**
     * 对应的请求报文的id
     */
    private final String reqId;
    /**
     * 报文体参数
     */
    private Map<String, Object> data;
    /**
     * 结果说明（内部）
     */
    private String messageIn;
    /**
     * 结果说明（外部）
     */
    private String messageOut;
    /**
     * 返回结果
     */
    private String message;
    /**
     * 执行状态码 0 正常
     */
    private String code;


    public ResponseMessage(String reqId) {
        Assert.notNull(reqId, "reqId 参数不能为空.");
        this.id = IdGenerator.getUUID();
        this.reqId = reqId;
    }

    public void setDataAttribute(String property, Object val) {
        Assert.notNull(property, "属性不能为空.");
        Assert.notNull(val, "属性值不能为空.");
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        if (val instanceof Date) {
            val = DataFormatUtils.getTimeString((Date) val);
        }
        this.data.put(property, val);
    }

    @JsonIgnore
    public String getMessageIn() {
        return messageIn;
    }

    public void setMessageIn(String messageIn) {
        this.messageIn = messageIn;
    }

    @JsonIgnore
    public String getMessageOut() {
        return messageOut;
    }

    public void setMessageOut(String messageOut) {
        this.messageOut = messageOut;
    }

    public Map<String, Object> getData() {
        if (data == null) {
            this.data = new HashMap<>();
        }
        return data;
    }

    public String getId() {
        return id;
    }

    public String getReqId() {
        return reqId;
    }

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

    public void setData(Map<String, Object> data) {
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof Date) {
                    entry.setValue(DataFormatUtils.getTimeString((Date) entry.getValue()));
                }
            }
        }
        this.data = data;
    }
}
