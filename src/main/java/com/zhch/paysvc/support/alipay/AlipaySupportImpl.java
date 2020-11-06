package com.zhch.paysvc.support.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.entity.*;
import com.zhch.paysvc.support.bean.*;
import com.zhch.paysvc.utils.BillFileReader;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.Dates;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author lumos
 */
public class AlipaySupportImpl implements AlipaySupport {

    private final AlipayClient alipayClient;
    private final ChannelConfig channelConfig;
    private final String SUFFIX = "m";

    @Override
    public ChannelConfig getChannelConfig() {
        return this.channelConfig;
    }


    public AlipaySupportImpl(ChannelConfig channelConfig) {
        Assert.notNull(channelConfig, "ChannelConfig is null.");
        this.alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", channelConfig.getAppId(), channelConfig.getOwnerPrivateKey(), "json", "UTF-8", channelConfig.getAlipayPublicKey(), "RSA2");
        ;
        this.channelConfig = channelConfig;
    }


    @Override
    public TradePrecreateResponse qrPay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String timeoutExpress) {
        TradePrecreateResponse response = null;
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("subject", subject);
        args.put("total_amount", totalAmount);
        args.put("notify_url", channelConfig.getNotifyUrl());
        args.put("timestamp", timeStart);
        args.put("terminal_id", terminalId);
        args.put("timeout_express", timeoutExpress + SUFFIX);
        args.put("enable_pay_channels", channelConfig.getEnablePayChannels());
        if (StringUtils.hasLength(channelConfig.getSysServiceProviderId())) {
            JSONObject extendParams = new JSONObject();
            extendParams.put("sys_service_provider_id", channelConfig.getSysServiceProviderId());
            args.put("extend_params", extendParams);
        }
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(args.toJSONString());
        try {
            response = new TradePrecreateResponse();
            AlipayTradePrecreateResponse tradePrecreateResponse = alipayClient.execute(request);
            if ("10000".equals(tradePrecreateResponse.getCode())) {
                response.setCode("0");
                response.setMessage(tradePrecreateResponse.getMsg());
                response.setTradeNo(tradeNo);
                response.setTotalAmount(totalAmount);
                response.setTimeStart(timeStart);
                response.setCodeUrl(tradePrecreateResponse.getQrCode());
                response.setPayUrl(tradePrecreateResponse.getQrCode());
            } else {
                response.setCode("1");
                response.setMessage(tradePrecreateResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }


    @Override
    public TradeQueryResponse queryByTradeNo(String tradeNo) {
        TradeQueryResponse response = null;
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(args.toJSONString());
        try {
            AlipayTradeQueryResponse queryResponse = alipayClient.execute(request);
            response = new TradeQueryResponse();
            if (queryResponse.getCode().equals("10000")) {
                response.setCode("0");
                response.setMessage("查询成功");
                if (queryResponse.getTradeStatus().equals("TRADE_SUCCESS")) {
                    response.setTradeState("2");
                    response.setTradeStateDesc("已支付");
                    response.setTransactionId(queryResponse.getTradeNo());
                    if (queryResponse.getSendPayDate() != null) {
                        response.setTimeEnd(DataFormatUtils.getTimeStringNoSpace(queryResponse.getSendPayDate()));
                    }
                    response.setReceiveAmount(queryResponse.getReceiptAmount());
                    response.setTotalAmount(queryResponse.getTotalAmount());
                    response.setRefundAmount(DataFormatUtils.getSubtractAmount(queryResponse.getTotalAmount(),queryResponse.getReceiptAmount()));
                    response.setBuyerId(queryResponse.getBuyerLogonId());
                    response.setCode("0");
                    response.setMessage(queryResponse.getMsg());
                    response.setTradeNo(queryResponse.getOutTradeNo());
                } else if (queryResponse.getTradeStatus().equals("TRADE_CLOSED")) {
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                    response.setTradeNo(queryResponse.getOutTradeNo());
                } else if (queryResponse.getTradeStatus().equals("WAIT_BUYER_PAY")) {
                    response.setTradeState("1");
                    response.setTradeStateDesc("未支付");
                } else {
                    response.setTradeState("5");
                    response.setTradeStateDesc("账单状态异常");
                }
            } else if (queryResponse.getCode().equals("40004")) {
                /**
                 *
                 */
                response.setTradeState("1");
                response.setTradeStateDesc("未支付");
                response.setCode("0");
                response.setMessage("查询成功");
            } else {
                response.setCode("1");
                response.setMessage(queryResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }


    @Override
    public TradeQueryResponse queryByTransactionId(String transactionId) throws Exception{
        TradeQueryResponse response = null;
        JSONObject args = new JSONObject();
        args.put("trade_no", transactionId);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(args.toJSONString());
        try {
            AlipayTradeQueryResponse queryResponse = alipayClient.execute(request);
            response = new TradeQueryResponse();
            if (queryResponse.getCode().equals("10000")) {
                response.setCode("0");
                response.setMessage("查询成功");
                if (queryResponse.getTradeStatus().equals("TRADE_SUCCESS")) {
                    response.setTradeState("2");
                    response.setTradeStateDesc("已支付");
                    response.setTransactionId(queryResponse.getTradeNo());
                    if (queryResponse.getSendPayDate() != null) {
                        response.setTimeEnd(DataFormatUtils.getTimeStringNoSpace(queryResponse.getSendPayDate()));
                    }
                    response.setReceiveAmount(queryResponse.getReceiptAmount());
                    response.setTotalAmount(queryResponse.getTotalAmount());
                    response.setBuyerId(queryResponse.getBuyerLogonId());
                    response.setCode("0");
                    response.setMessage(queryResponse.getMsg());
                    response.setTradeNo(queryResponse.getOutTradeNo());
                } else if (queryResponse.getTradeStatus().equals("TRADE_CLOSED")) {
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                    response.setTradeNo(queryResponse.getOutTradeNo());
                } else if (queryResponse.getTradeStatus().equals("WAIT_BUYER_PAY")) {
                    response.setTradeState("1");
                    response.setTradeStateDesc("未支付");
                    response.setTradeNo(queryResponse.getOutTradeNo());
                } else {
                    response.setTradeState("5");
                    response.setTradeStateDesc("账单状态异常");
                }
            } else if (queryResponse.getCode().equals("40004")) {
                /**
                 *
                 */
                response.setTradeState("1");
                response.setTradeStateDesc("未支付");
                response.setCode("0");
                response.setMessage("查询成功");
            } else {
                response.setCode("1");
                response.setMessage(queryResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }

    @Override
    public TradeRefundResponse refundApply(String tradeNo, String totalAmount, String refundDesc) throws Exception {
        TradeRefundResponse refundResponse = null;
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("refund_amount", totalAmount);
        args.put("refund_reason", refundDesc);
        args.put("notify_url", channelConfig.getNotifyUrl());
        request.setBizContent(args.toJSONString());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            refundResponse = new TradeRefundResponse();
            if (response.getCode().equals("10000")) {
                refundResponse.setCode("0");
                refundResponse.setTradeNo(response.getTradeNo());
                refundResponse.setOutTradeNo(response.getOutTradeNo());
                refundResponse.setMessage("退款成功");
                refundResponse.setRefundAmount(response.getRefundFee() + "");
                refundResponse.setBuyerId(response.getBuyerLogonId());
                refundResponse.setRefundTime(DataFormatUtils.getTimeStringNoSpace(response.getGmtRefundPay()));
            } else if (response.getCode().equals("40004")) {
                if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                    refundResponse.setCode("0");
                    refundResponse.setMessage("订单未支付");
                } else if (response.getSubCode().equals("ACQ.TRADE_STATUS_ERROR")) {
                    TradeCloseResponse closeResponse = closeTradeByTradeNo(tradeNo);
                    if (closeResponse.getCode().equals("0")) {
                        refundResponse.setCode("0");
                        refundResponse.setMessage("订单已关闭");
                    } else {
                        refundResponse.setCode(closeResponse.getCode());
                        refundResponse.setMessage(closeResponse.getMessage());
                    }
                } else {
                    refundResponse.setCode("1");
                    refundResponse.setMessage(response.getSubMsg());
                }
            } else {
                refundResponse.setCode("1");
                refundResponse.setMessage("订单退费异常");
            }
        } catch (AlipayApiException e) {
            refundResponse.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                refundResponse.setMessage(e.getMessage());
            } else {
                refundResponse.setMessage("系统异常");
            }
        }
        return refundResponse;
    }

    @Override
    public TradePayResponse tradePay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String authCode) throws Exception {
        TradePayResponse response = null;
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("scene", "bar_code");
        args.put("auth_code", authCode);
        args.put("subject", subject);
        args.put("seller_id", channelConfig.getSellerId());
        args.put("total_amount", totalAmount);
        args.put("notify_url", channelConfig.getNotifyUrl());
        args.put("timestamp", timeStart);
        args.put("terminal_id", terminalId);
        if (StringUtils.hasLength(channelConfig.getSysServiceProviderId())) {
            JSONObject extendParams = new JSONObject();
            extendParams.put("sys_service_provider_id", channelConfig.getSysServiceProviderId());
            args.put("extend_params", extendParams);
        }
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizContent(args.toJSONString());
        try {
            response = new TradePayResponse();
            AlipayTradePayResponse tradePayResponse = alipayClient.execute(request);
            if (tradePayResponse.getCode().equals("10000")) {
                response.setCode("0");
                response.setMessage(tradePayResponse.getMsg());
                response.setTradeNo(tradeNo);
                response.setTotalAmount(totalAmount);
                response.setTimeStart(timeStart);
                response.setBuyerId(tradePayResponse.getBuyerUserId());
                response.setTradeState("2");
                response.setTradeStateDesc("已支付");
            } else if (tradePayResponse.getCode().equals("10003")) {
                response.setCode("0");
                response.setMessage("需要用户输入支付密码");
                response.setTradeNo(tradeNo);
                response.setTotalAmount(totalAmount);
                response.setTimeStart(timeStart);
                response.setBuyerId(tradePayResponse.getBuyerUserId());
                response.setTradeStateDesc("需要用户输入支付密码");
                response.setTradeState("1");
            } else {
                response.setCode("1");
                response.setMessage(tradePayResponse.getSubMsg()!=null?tradePayResponse.getSubMsg():tradePayResponse.getMsg());
            }
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }

    @Override
    public AlipayTradePayResponse tradePay(String tradeNo, String barCode, String authCode, String subject, String totalAmount) {
        return null;
    }

    @Override
    public AlipayTradeCancelResponse tradeCancel(String tradeNo) {
        return null;
    }

    @Override
    public TradePrecreateResponse wapPayCreate(String tradeNo, String subject, String totalAmount) {
        TradePrecreateResponse response = null;
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("subject", subject);
        args.put("product_code", "QUICK_WAP_WAY");
        args.put("total_amount", totalAmount);
        args.put("notify_url", channelConfig.getNotifyUrl());
        args.put("timestamp", Dates.now("yyyy-MM-dd hh:mm:ss"));
        args.put("timeout_express", "120m");
        args.put("enable_pay_channels", channelConfig.getEnablePayChannels());
        if (StringUtils.hasLength(channelConfig.getSysServiceProviderId())) {
            JSONObject extendParams = new JSONObject();
            extendParams.put("sys_service_provider_id", channelConfig.getSysServiceProviderId());
            args.put("extend_params", extendParams);
        }
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizContent(args.toJSONString());
        try {
            response = new TradePrecreateResponse();
            AlipayTradeWapPayResponse wapPayResponse = alipayClient.pageExecute(request);
            response.setForm(wapPayResponse.getBody());
            response.setCode("0");
            response.setMessage("请求成功");
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }

    @Override
    public TradeCloseResponse closeTradeByTradeNo(String tradeNo) throws Exception {
        TradeCloseResponse response = null;
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("notify_url", channelConfig.getNotifyUrl());
        AlipayTradeCloseRequest tradeCloseRequest = new AlipayTradeCloseRequest();
        tradeCloseRequest.setBizContent(args.toJSONString());
        try {
            response = new TradeCloseResponse();
            AlipayTradeCloseResponse tradeCloseResponse = alipayClient.execute(tradeCloseRequest);
            if (tradeCloseResponse.getCode().equals("10000")) {
                response.setCode("0");
                response.setMessage("关闭成功");
                response.setTradeState("4");
                response.setTradeStateDesc("订单失效");
            } else if (tradeCloseResponse.getCode().equals("40004")) {
                if (tradeCloseResponse.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                    response.setCode("0");
                    response.setMessage("关闭成功");
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                } else {
                    response.setCode("2");
                    response.setMessage("关闭失败");
                    response.setTradeState("5");
                    response.setTradeStateDesc("账单状态异常");
                }
            }
        } catch (AlipayApiException e) {
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }

    @Override
    public TradeRecordResponse billDownload(String billDate) throws Exception {
        TradeRecordResponse recordResponse = new TradeRecordResponse();
        try {

            AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
            List<Object> details = null;
            JSONObject args = new JSONObject();
            args.put("bill_type", "trade");
            args.put("bill_date", billDate);
            request.setBizContent(args.toJSONString());
            request.setNeedEncrypt(false);
            AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
            BillContainer billContainer = new BillContainer();
            recordResponse.setBillContainer(billContainer);
            billContainer.setPayType(PayTypeEnum.ALIPAY.getPayType());
            try {
                response = alipayClient.execute(request);
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            if (response.isSuccess()) {
                String billDownloadUrl = response.getBillDownloadUrl();

                Bill bill = BillFileReader.analysisAlipayBill(billDownloadUrl, getChannelConfig().getSellerId(), getChannelConfig().getChannelCode(), billDate);
                bill.setPayType(PayTypeEnum.ALIPAY.getPayType());

                billContainer.setBill(bill);
                billContainer.setState("0");
                billContainer.setStateDesc("账单下载成功");
                recordResponse.setCode("0");
                recordResponse.setMessage("下载成功");
            } else if (response.getSubCode().equals("isp.bill_not_exist")) {
                billContainer.setState("1");
                billContainer.setStateDesc(response.getSubMsg());
                recordResponse.setCode("0");
                recordResponse.setMessage(response.getSubMsg());
            } else {
                billContainer.setState("1");
                billContainer.setStateDesc(response.getSubMsg());
                recordResponse.setCode("1");
                recordResponse.setMessage(response.getSubMsg());
            }
        } catch (Exception e) {
            recordResponse.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                recordResponse.setMessage(e.getMessage());
            } else {
                recordResponse.setMessage("系统异常");
            }
        }
        return recordResponse;
    }


    @Override
    public TradeCancelResponse cancelTradeByTradeNo(String tradeNo) throws Exception {
        TradeCancelResponse tradeCancelResponse = new TradeCancelResponse();

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        request.setBizContent(args.toJSONString());
        AlipayTradeCancelResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            tradeCancelResponse.setCode("0");
            tradeCancelResponse.setMessage("调用成功");
        } else {
            tradeCancelResponse.setCode("1");
            tradeCancelResponse.setMessage(response.getSubMsg());
        }
        return tradeCancelResponse;
    }

    @Override
    public TradeSignResponse doSign(String signContent) throws Exception {
        TradeSignResponse response = new TradeSignResponse();
        String signResult = AlipaySignature.rsaSign(signContent, channelConfig.getOwnerPrivateKey(), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
        response.setCharset(AlipayConstants.CHARSET_UTF8);
        response.setCode("0");
        response.setMessage("加签成功");
        response.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        response.setSign(URLEncoder.encode(signResult, AlipayConstants.CHARSET_UTF8));
        response.setVersion("1.0");
        return response;
    }

    @Override
    public TradeSignResponse doAndriodSign(String signContent) throws Exception {
        TradeSignResponse response = new TradeSignResponse();
        String signResult = AlipaySignature.rsaSign(signContent, channelConfig.getOwnerPrivateKey(), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
        response.setCharset(AlipayConstants.CHARSET_UTF8);
        response.setCode("0");
        response.setMessage("加签成功");
        response.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        response.setSign(signResult);
        response.setVersion("1.0");
        return response;
    }

    @Override
    public TradeRefundResponse refundPartApply(String tradeNo, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception{
        TradeRefundResponse refundResponse = null;
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject args = new JSONObject();
        args.put("out_trade_no", tradeNo);
        args.put("refund_amount", refundAmount);
        args.put("refund_reason", refundDesc);
        args.put("out_request_no", outRequestNo);
        args.put("notify_url", channelConfig.getNotifyUrl());
        request.setBizContent(args.toJSONString());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            refundResponse = new TradeRefundResponse();
            refundResponse.setTradeNo(response.getOutTradeNo());
            refundResponse.setTransactionId(response.getTradeNo());
            if (response.getCode().equals("10000")) {
                refundResponse.setCode("0");
                refundResponse.setMessage("退款成功");
                refundResponse.setRefundAmount(response.getSendBackFee() + "");
                refundResponse.setBuyerId(response.getBuyerLogonId());
                refundResponse.setRefundTime(DataFormatUtils.getTimeStringNoSpace(response.getGmtRefundPay()));
            } else if (response.getCode().equals("40004")) {
                if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                    refundResponse.setCode("0");
                    refundResponse.setMessage("订单未支付");
                } else if (response.getSubCode().equals("ACQ.TRADE_STATUS_ERROR")) {
                    refundResponse.setCode("1");
                    refundResponse.setMessage("订单状态异常");
                } else if (response.getSubCode().equals("ACQ.REASON_TRADE_REFUND_FEE_ERR")) {
                    refundResponse.setCode("1");
                    refundResponse.setMessage("请求退费金额不正确，请核对");
                } else {
                    refundResponse.setCode("1");
                    refundResponse.setMessage(response.getSubMsg());
                }
            } else {
                refundResponse.setCode("1");
                refundResponse.setMessage("订单退费异常");
            }
        } catch (AlipayApiException e) {
            refundResponse.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                refundResponse.setMessage(e.getMessage());
            } else {
                refundResponse.setMessage("系统异常");
            }
        }
        return refundResponse;
    }

    @Override
    public TradeRefundResponse refundPartApplyByTransactionId(String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception {
        TradeRefundResponse refundResponse = null;
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject args = new JSONObject();
        args.put("trade_no", transactionId);
        args.put("refund_amount", refundAmount);
        args.put("refund_reason", refundDesc);
        args.put("out_request_no", outRequestNo);
        args.put("notify_url", channelConfig.getNotifyUrl());
        request.setBizContent(args.toJSONString());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            refundResponse = new TradeRefundResponse();
            if (response.getCode().equals("10000")) {
                refundResponse.setCode("0");
                refundResponse.setMessage("退款成功");
                refundResponse.setTransactionId(response.getTradeNo());
                refundResponse.setTradeNo(response.getOutTradeNo());
                refundResponse.setRefundAmount(response.getSendBackFee() + "");
                refundResponse.setBuyerId(response.getBuyerLogonId());
                refundResponse.setRefundTime(DataFormatUtils.getTimeStringNoSpace(response.getGmtRefundPay()));
            } else if (response.getCode().equals("40004")) {
                if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                    refundResponse.setCode("0");
                    refundResponse.setMessage("订单未支付");
                } else if (response.getSubCode().equals("ACQ.TRADE_STATUS_ERROR")) {
                    refundResponse.setCode("1");
                    refundResponse.setMessage("订单状态异常");
                } else if (response.getSubCode().equals("ACQ.REASON_TRADE_REFUND_FEE_ERR")) {
                    refundResponse.setCode("1");
                    refundResponse.setMessage("请求退费金额不正确，请核对");
                } else {
                    refundResponse.setCode("1");
                    refundResponse.setMessage(response.getSubMsg());
                }
            } else {
                refundResponse.setCode("1");
                refundResponse.setMessage("订单退费异常");
            }
        } catch (AlipayApiException e) {
            refundResponse.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                refundResponse.setMessage(e.getMessage());
            } else {
                refundResponse.setMessage("系统异常");
            }
        }
        return refundResponse;
    }
}
