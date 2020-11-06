package com.zhch.paysvc.support;

import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.support.bean.*;

/**
 * Created by ThinkPad on 2017/11/18.
 */
public interface PaySupport {

    ChannelConfig getChannelConfig();

    /**
     * QR支付
     *
     * @param tradeNo
     * @param subject
     * @param totalAmount
     * @param timeStart
     * @param terminalId
     * @param transIntroduce
     * @param timeoutExpress
     * @return
     */
    TradePrecreateResponse qrPay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String timeoutExpress);

    /**
     * 订单查询
     *
     * @param tradeNo
     * @return
     */
    TradeQueryResponse queryByTradeNo(String tradeNo);

    /**
     * 统一退款
     *
     * @param tradeNo
     * @param totalAmount
     * @param refundDesc
     * @return
     */
    TradeRefundResponse refundApply(String tradeNo, String totalAmount, String refundDesc) throws Exception;

    /**
     * 创建wap支付
     *
     * @param tradeNo
     * @param subject
     * @param totalAmount
     * @return
     */
    TradePrecreateResponse wapPayCreate(String tradeNo, String subject, String totalAmount);

    /**
     * 通过tradeNo 关闭交易
     *
     * @param tradeNo
     * @return
     */
    TradeCloseResponse closeTradeByTradeNo(String tradeNo) throws Exception;

    /**
     * 扫码付
     * @param tradeNo
     * @param subject
     * @param totalAmount
     * @param timeStart
     * @param terminalId
     * @param transIntroduce
     * @param authCode
     * @return
     */
    TradePayResponse tradePay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String authCode) throws Exception;

    /**
     *
     * @param billDate
     * @return
     */
    TradeRecordResponse billDownload(String billDate) throws Exception;

    /**
     * 交易冲正
     * @param tradeNo
     * @return
     * @throws Exception
     */
    TradeCancelResponse cancelTradeByTradeNo(String tradeNo) throws Exception;

    /**
     * 加签
     * @param signContent
     * @return
     */
    TradeSignResponse doSign(String signContent) throws Exception;

    TradeSignResponse doAndriodSign(String signContent) throws Exception;

    TradeRefundResponse refundPartApply(String tradeNo, String totalAmount,String refundAmount, String outRequestNo, String refundDesc) throws Exception;

    TradeQueryResponse queryByTransactionId(String transactionId) throws Exception;

    TradeRefundResponse refundPartApplyByTransactionId(String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception;
}
