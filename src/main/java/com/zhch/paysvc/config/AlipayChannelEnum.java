package com.zhch.paysvc.config;

/**
 * @author lumos
 */

public enum AlipayChannelEnum {
    BALANCE("余额", "balance"),
    MONEY_FUND("余额宝", "moneyFund"),
    BANK_PAY("网银", "bankPay"),
    DEBIT_CARD_EXPRESS("借记卡快捷", "debitCardExpress"),
    CREDIT_CARD_EXPRESS("信用卡快捷", "creditCardExpress"),
    CREDIT_CARD_CARTOON("信用卡卡通", "creditCardCartoon"),
    CREDIT_CARD("信用卡", "creditCard"),
    CARTOON("卡通", "cartoon"),
    PCREDIT("花呗", "pcredit"),
    PCREDITPAY_INSTALLMENT("花呗分期", "pcreditpayInstallment"),
    CREDIT_GROUP("信用支付类型(包含 信用卡卡通，信用卡快捷,花呗，花呗分期)", "credit_group"),
    COUPON("红包", "coupon"),
    POINT("积分", "point"),
    PROMOTION("优惠（包含实时优惠+商户优惠）", "promotion"),
    VOUCHER("营销券", "voucher"),
    MDISCOUNT("商户优惠", "mdiscount"),
    HONEY_PAY("亲密付", "honeyPay"),
    MCARD("商户预存卡", "mcard"),
    PCARD("个人预存卡", "pcard");


    private String value;
    private String label;

    AlipayChannelEnum(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
