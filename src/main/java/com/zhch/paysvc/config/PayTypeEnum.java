package com.zhch.paysvc.config;

/**
 * Created by ThinkPad on 2017/11/12.
 */
public enum PayTypeEnum {
    WEPAY("1","微信支付"),ALIPAY("2","支付宝"),CMB("3","招行");

    private String payType;
    private String payName;

    PayTypeEnum(String payType, String payName) {
        this.payType = payType;
        this.payName = payName;
    }

    public static String getPayName(String payType) {
        for (PayTypeEnum payTypeEnum: PayTypeEnum.values()) {
            if (payTypeEnum.getPayType().equals(payType)) {
                return payTypeEnum.getPayName();
            }
        }
        return null;
    }


    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayType() {
        return payType;
    }

    public String getPayName() {
        return payName;
    }


}
