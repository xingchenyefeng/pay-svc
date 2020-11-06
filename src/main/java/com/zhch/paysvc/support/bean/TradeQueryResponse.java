package com.zhch.paysvc.support.bean;

import com.zhch.paysvc.utils.DataFormatUtils;
import org.springframework.util.StringUtils;

/**
 * Created by ThinkPad on 2017/11/19.
 */
public class TradeQueryResponse extends TradeResponse {

    private static final long serialVersionUID = -3351738418707958342L;

    private String tradeState;
    private String tradeStateDesc;
    private String payType;
    private String outTradeNo;
    private String tradeNo;
    private String transactionId;
    private String timeEnd;
    private String buyerId;
    private String receiveAmount; //实收金额
    private String totalAmount; //订单金额
    private String refundAmount; //退款金额


    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getReceiveAmount() {
        if (StringUtils.hasLength(receiveAmount) || !StringUtils.hasLength(refundAmount)){
            return totalAmount;
        }else {
            return DataFormatUtils.getSubtractAmount(totalAmount,refundAmount);
        }
    }

    public void setReceiveAmount(String receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }
}
