package com.zhch.paysvc.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;


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
@TableName("bill_detail")
public class BillDetail extends Entity {

    private static final long serialVersionUID = -6173036388473468574L;


    /**
     * 支付渠道编号
     */
    private String channelCode;


    /**
     * 账期
     */
    private String accountPeriod;


    /**
     * 交易流水
     */
    private String transNo;


    /**
     * 院内支付订单
     */
    private String outTransNo;


    /**
     * 交易类型
     */
    private String tradeType;


    /**
     * 商品名称
     */
    private String subject;


    /**
     * 创建时间
     */
    private String startTime;


    /**
     * 支付时间
     */
    private String payTime;


    /**
     * 门店编号
     */
    private String storeId;


    /**
     * 门店名称
     */
    private String storeName;


    /**
     * 操作员id
     */
    private String operator;


    /**
     * 终端编号
     */
    private String terminalId;


    /**
     * 对方账户
     */
    private String buyerId;


    /**
     * 订单金额
     */
    private String totalAmount;


    /**
     * 商家实收金额
     */
    private String receiptAmount;


    /**
     * 红包
     */
    private String envelopes;


    /**
     * 集分宝
     */
    private String treasure;


    /**
     * 支付渠道优惠
     */
    private String channelDiscount;


    /**
     * 商家优惠
     */
    private String storeDiscount;


    /**
     * 券核销金额
     */
    private String coupon;


    /**
     * 券名称
     */
    private String couponName;


    /**
     * 店家红包
     */
    private String storeEnvelopes;


    /**
     * 卡消费金额
     */
    private String cardSpendingAmount;


    /**
     * 退款批次号/请求号
     */
    private String outRequestNo;


    /**
     * 分润
     */
    private String point;


    /**
     * 服务费
     */
    private String serviceAmount;


    /**
     * 备注
     */
    private String mark;



    public static final String TBL_ID = "id";
    public static final String TBL_CHANNEL_CODE = "channel_code";
    public static final String TBL_ACCOUNT_PERIOD = "account_period";
    public static final String TBL_TRANS_NO = "trans_no";
    public static final String TBL_OUT_TRANS_NO = "out_trans_no";
    public static final String TBL_TRADE_TYPE = "trade_type";
    public static final String TBL_SUBJECT = "subject";
    public static final String TBL_START_TIME = "start_time";
    public static final String TBL_PAY_TIME = "pay_time";
    public static final String TBL_STORE_ID = "store_id";
    public static final String TBL_STORE_NAME = "store_name";
    public static final String TBL_OPERATOR = "operator";
    public static final String TBL_TERMINAL_ID = "terminal_id";
    public static final String TBL_BUYER_ID = "buyer_id";
    public static final String TBL_TOTAL_AMOUNT = "total_amount";
    public static final String TBL_RECEIPT_AMOUNT = "receipt_amount";
    public static final String TBL_ENVELOPES = "envelopes";
    public static final String TBL_TREASURE = "treasure";
    public static final String TBL_CHANNEL_DISCOUNT = "channel_discount";
    public static final String TBL_STORE_DISCOUNT = "store_discount";
    public static final String TBL_COUPON = "coupon";
    public static final String TBL_COUPON_NAME = "coupon_name";
    public static final String TBL_STORE_ENVELOPES = "store_envelopes";
    public static final String TBL_CARD_SPENDING_AMOUNT = "card_spending_amount";
    public static final String TBL_OUT_REQUEST_NO = "out_request_no";
    public static final String TBL_POINT = "point";
    public static final String TBL_SERVICE_AMOUNT = "service_amount";
    public static final String TBL_MARK = "mark";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////

    public BillDetail(String[] array) {
        int index = 0;
        this.transNo = StringUtils.trimAllWhitespace(array[index++]);
        this.outTransNo = StringUtils.trimAllWhitespace(array[index++]);
        this.tradeType = StringUtils.trimAllWhitespace(array[index++]);
        this.subject = StringUtils.trimAllWhitespace(array[index++]);
        this.startTime = array[index++];
        this.payTime = StringUtils.trimWhitespace(array[index++]);
        this.storeId = StringUtils.trimAllWhitespace(array[index++]);
        this.storeName = StringUtils.trimAllWhitespace(array[index++]);
        this.operator = StringUtils.trimAllWhitespace(array[index++]);
        this.terminalId = StringUtils.trimAllWhitespace(array[index++]);
        this.buyerId = StringUtils.trimAllWhitespace(array[index++]);
        this.totalAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.receiptAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.envelopes = StringUtils.trimAllWhitespace(array[index++]);

        this.treasure = StringUtils.trimAllWhitespace(array[index++]);
        this.channelDiscount = StringUtils.trimAllWhitespace(array[index++]);
        this.storeDiscount = StringUtils.trimAllWhitespace(array[index++]);
        this.coupon = StringUtils.trimAllWhitespace(array[index++]);
        this.couponName = StringUtils.trimAllWhitespace(array[index++]);
        this.storeEnvelopes = StringUtils.trimAllWhitespace(array[index++]);
        this.cardSpendingAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.outRequestNo = StringUtils.trimAllWhitespace(array[index++]);
        this.point = StringUtils.trimAllWhitespace(array[index++]);
        this.serviceAmount = StringUtils.trimAllWhitespace(array[index++]);
        this.mark="";
    }

    public BillDetail() {
    }
}
