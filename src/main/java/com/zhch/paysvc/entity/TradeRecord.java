package com.zhch.paysvc.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
@TableName("trade_record")
public class TradeRecord extends Entity {

    private static final long serialVersionUID = 1914695869003676898L;


    /**
     * 支付渠道交易id
     */
    private String transactionId;


    /**
     * 支付类型
     */
    private String payType;


    /**
     * 收到请求支付时间
     */
    private Date receiveTime;


    /**
     * 渠道编号
     */
    private String channelCode;


    /**
     * 医院代码
     */
    private String hospitalCode;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 姓名
     */
    private String name;


    /**
     * 身份证号
     */
    private String idCard;


    /**
     * 用户id
     */
    private String buyerId;


    /**
     * {name: '挂号', value: '1'}, {name: '诊间结算', value: '2'}, {name: '住院预交金', value: '3'}
     * 业务类型
     */
    private String bsType;


    /**
     * 交易内容
     */
    private String subject;


    /**
     * 交易简介
     */
    private String transIntroduce;


    /**
     * 交易详情
     */
    private String transDetail;


    /**
     * 交易流水
     */
    private String tradeNo;


    /**
     * 内部流水ID，对账用
     */
    private String outTradeNo;


    /**
     * 部门代码
     */
    private String deptCode;


    /**
     * 医生代码
     */
    private String doctorCode;


    /**
     * 设备信息(请求支付的设备代码)
     */
    private String deviceInfo;


    /**
     * 币种
     */
    private String feeType;


    /**
     * 总金额
     */
    private String totalAmount;


    /**
     * 实收金额
     */
    private String receiptAmount;


    /**
     * 支付金额
     */
    private String payAmount;


    /**
     * 客户端id
     */
    private String clientId;


    /**
     * 交易开始时间
     */
    private String timeStart;


    /**
     * 交易结束时间
     */
    private String timeEnd;


    /**
     * 交易有效期
     */
    private String timeExpire;


    /**
     * 交易类型
     */
    private String tradeType;


    /**
     * 限制交易的渠道
     */
    private String limitPay;


    /**
     * 交易状态码
     */
    private String state;


    /**
     * 状态码对应的内容
     */
    private String resultText;


    /**
     * 退款时间
     */
    private String refundTime;


    /**
     * 退款金额
     */
    private String refundAmount;


    /**
     * 退款说明
     */
    private String refundDesc;



    public static final String TBL_ID = "id";
    public static final String TBL_TRANSACTION_ID = "transaction_id";
    public static final String TBL_PAY_TYPE = "pay_type";
    public static final String TBL_RECEIVE_TIME = "receive_time";
    public static final String TBL_CHANNEL_CODE = "channel_code";
    public static final String TBL_HOSPITAL_CODE = "hospital_code";
    public static final String TBL_PHONE = "phone";
    public static final String TBL_NAME = "name";
    public static final String TBL_ID_CARD = "id_card";
    public static final String TBL_BUYER_ID = "buyer_id";
    public static final String TBL_BS_TYPE = "bs_type";
    public static final String TBL_SUBJECT = "subject";
    public static final String TBL_TRANS_INTRODUCE = "trans_introduce";
    public static final String TBL_TRANS_DETAIL = "trans_detail";
    public static final String TBL_TRADE_NO = "trade_no";
    public static final String TBL_OUT_TRADE_NO = "out_trade_no";
    public static final String TBL_DEPT_CODE = "dept_code";
    public static final String TBL_DOCTOR_CODE = "doctor_code";
    public static final String TBL_DEVICE_INFO = "device_info";
    public static final String TBL_FEE_TYPE = "fee_type";
    public static final String TBL_TOTAL_AMOUNT = "total_amount";
    public static final String TBL_RECEIPT_AMOUNT = "receipt_amount";
    public static final String TBL_PAY_AMOUNT = "pay_amount";
    public static final String TBL_CLIENT_ID = "client_id";
    public static final String TBL_TIME_START = "time_start";
    public static final String TBL_TIME_END = "time_end";
    public static final String TBL_TIME_EXPIRE = "time_expire";
    public static final String TBL_TRADE_TYPE = "trade_type";
    public static final String TBL_LIMIT_PAY = "limit_pay";
    public static final String TBL_STATE = "state";
    public static final String TBL_RESULT_TEXT = "result_text";
    public static final String TBL_REFUND_TIME = "refund_time";
    public static final String TBL_REFUND_AMOUNT = "refund_amount";
    public static final String TBL_REFUND_DESC = "refund_desc";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
