package com.zhch.paysvc.support;


import com.zhch.paysvc.support.bean.*;

/**
 *
 * @author lumos
 * @date 2017/11/12
 */


public interface IPayAgent {

    void setPaySupportServicePool(PaySupportServicePool paySupportServicePool);

    PaySupportServicePool getPaySupportServicePool();

    TradePrecreateResponse qrPay(String channelCode, String bsType, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo, String timeoutExpress) throws Exception;

    TradeQueryResponse queryByTradeNo(String channelCode,  String tradeNo,String transactionId) throws Exception;

    TradeRefundResponse refundApply(String channelCode, String tradeNo, String totalAmount, String refundTime, String deviceInfo, String refundDesc) throws Exception;

    TradePrecreateResponse wapPayCreate(String channelCode,  String bsType, String tradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo) throws Exception;

    TradeCloseResponse closeTrade(String channelCode,String tradeNo) throws Exception;

    TradePayResponse tradePay(String channelCode,String bsType, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo, String authCode) throws Exception;

    TradeRecordResponse billDownload(String channelCode, String billDate) throws Exception;

    TradeSignResponse doSign(String channelCode, String signContent)  throws Exception;

    TradeSignResponse doAndriodSign(String channelCode, String signContent) throws Exception;

    TradeRefundResponse refundPartApply(String channelCode, String tradeNo,String totalAmount, String refundAmount,String outRequestNo, String refundTime, String deviceInfo, String refundDesc) throws Exception;

    TradeRefundResponse refundPartApplyByTransactionId(String channelCode, String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundTime, String deviceInfo, String refundDesc) throws Exception;

    TradeQueryResponse noticeComplete(String channelCode, String bsType,String tradeNo, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo) throws Exception;

}
