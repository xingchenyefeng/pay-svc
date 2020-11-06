package com.zhch.paysvc.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lumos
 * @since 2020-10-10
 */
@Getter
@Setter
public class Bill extends Entity {

    private static final long serialVersionUID = -1153815164007454459L;


    /**
     * 支付渠道编号
     */
    private String channelCode;

    /**
     * 支付类型
     */
    private String payType;
    /**
     * 账期
     */
    private String accountPeriod;


    /**
     * 交易类型
     */
    private String tradeType;


    /**
     * 门店编号
     */
    private String storeNum;


    /**
     * 门店名称
     */
    private String storeName;


    /**
     * 交易笔数
     */
    private String transCount;


    /**
     * 退费笔数
     */
    private String refundCount;


    /**
     * 总金额
     */
    private String amount;


    /**
     * 商家实收
     */
    private String revenue;


    /**
     * 支付宝|微信优惠
     */
    private String discount;


    /**
     * 商家优惠
     */
    private String merchantDiscount;


    /**
     * 卡消费金额
     */
    private String cardConsumeAmount;


    /**
     * 服务费
     */
    private String serviceCharge;


    /**
     * 分润
     */
    private String shareBenefit;


    /**
     * 实收净额
     */
    private String actualAmount;

    @TableField(exist = false)
    private List<BillDetail> billDetails;


    public static final String TBL_ID = "id";
    public static final String TBL_CHANNEL_CODE = "channel_code";
    public static final String TBL_PAY_TYPE = "pay_type";
    public static final String TBL_ACCOUNT_PERIOD = "account_period";
    public static final String TBL_TRADE_TYPE = "trade_type";
    public static final String TBL_STORE_NUM = "store_num";
    public static final String TBL_STORE_NAME = "store_name";
    public static final String TBL_TRANS_COUNT = "trans_count";
    public static final String TBL_REFUND_COUNT = "refund_count";
    public static final String TBL_AMOUNT = "amount";
    public static final String TBL_REVENUE = "revenue";
    public static final String TBL_DISCOUNT = "discount";
    public static final String TBL_MERCHANT_DISCOUNT = "merchant_discount";
    public static final String TBL_CARD_CONSUME_AMOUNT = "card_consume_amount";
    public static final String TBL_SERVICE_CHARGE = "service_charge";
    public static final String TBL_SHARE_BENEFIT = "share_benefit";
    public static final String TBL_ACTUAL_AMOUNT = "actual_amount";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////

    public Bill(String[] array) {
        int index = 0;
        this.storeNum = StringUtils.trimAllWhitespace(array[index++]);
        this.storeName = StringUtils.trimAllWhitespace(array[index++]);
        this.transCount = StringUtils.trimAllWhitespace(array[index++]);
        this.refundCount = StringUtils.trimAllWhitespace(array[index++]);
        this.amount = StringUtils.trimAllWhitespace(array[index++]);
        this.revenue = StringUtils.trimAllWhitespace(array[index++]);
        this.discount = StringUtils.trimAllWhitespace(array[index++]);
        this.merchantDiscount = StringUtils.trimAllWhitespace(array[index++]);
        this.cardConsumeAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.serviceCharge = StringUtils.trimAllWhitespace(array[index++]);
        this.shareBenefit = StringUtils.trimAllWhitespace(array[index++]);
        this.actualAmount = StringUtils.trimAllWhitespace(array[index++]);
    }

    public Bill() {
    }

    public void reSetProperties(String[] array) {
        int index = 0;
        this.storeNum = StringUtils.trimAllWhitespace(array[index++]);
        this.storeName = StringUtils.trimAllWhitespace(array[index++]);
        this.transCount = StringUtils.trimAllWhitespace(array[index++]);
        this.refundCount = StringUtils.trimAllWhitespace(array[index++]);
        this.amount = StringUtils.trimAllWhitespace(array[index++]);
        this.revenue = StringUtils.trimAllWhitespace(array[index++]);
        this.discount = StringUtils.trimAllWhitespace(array[index++]);
        this.merchantDiscount = StringUtils.trimAllWhitespace(array[index++]);
        this.cardConsumeAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.serviceCharge = StringUtils.trimAllWhitespace(array[index++]);
        this.shareBenefit = StringUtils.trimAllWhitespace(array[index++]);
        this.actualAmount = StringUtils.trimAllWhitespace(array[index++]);
    }

}
