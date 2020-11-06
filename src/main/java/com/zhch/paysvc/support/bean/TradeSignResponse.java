package com.zhch.paysvc.support.bean;

/**
 * Created by ThinkPad on 2018/2/5.
 */
public class TradeSignResponse extends TradeResponse {

    private static final long serialVersionUID = 5374237700996345479L;
    private String charset;
    private String signType;
    private String version;
    private String timestamp;
    private String sign;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
