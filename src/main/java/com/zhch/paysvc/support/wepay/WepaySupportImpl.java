package com.zhch.paysvc.support.wepay;

import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.entity.Bill;
import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.support.bean.BillContainer;
import com.zhch.paysvc.support.bean.*;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.Dates;
import com.zhch.paysvc.utils.WePayConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/11/18.
 */

@Log4j2
public class WepaySupportImpl implements WepaySupport {


    private final ChannelConfig channelConfig;
    private final WePayClient payClient;
    /**
     * 联图二维码转换
     */
    private static final String LIANTU_URL = "http://qr.liantu.com/api.php?text=";

    @Override
    public ChannelConfig getChannelConfig() {
        return this.channelConfig;
    }

    public WepaySupportImpl(ChannelConfig channelConfig) throws Exception {
        this.channelConfig = channelConfig;
        this.payClient = new WePayClient(channelConfig.getAppId(), channelConfig.getSellerId(), channelConfig.getAppKey(),
                channelConfig.getNotifyUrl(), channelConfig.getEnablePayChannels(), channelConfig.getSysServiceProviderId(), new ByteArrayInputStream(channelConfig.getCert()));
    }


    @Override
    public TradePrecreateResponse qrPay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String timeoutExpress) {
        TradePrecreateResponse response = new TradePrecreateResponse();
        try {

            Map<String, String> respData = payClient.qrPay(tradeNo, subject, DataFormatUtils.getWepayFormatAmount(totalAmount), timeStart, terminalId, transIntroduce, timeoutExpress);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeNo(tradeNo);
                    response.setTotalAmount(totalAmount);
                    response.setTimeStart(timeStart);
                    response.setCodeUrl(respData.get(WepayField.CODE_URL));
                    response.setPayUrl(LIANTU_URL + respData.get(WepayField.CODE_URL));
                } else {
                    response.setCode("1");
                    response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        TradeQueryResponse response = new TradeQueryResponse();
        try {
            Map<String, String> respData = payClient.queryByTradeNo(tradeNo);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                response.setCode("0");
                response.setMessage("查询成功");
                if (respData.get(WepayField.TRADE_STATE).equals("SUCCESS")) {
                    response.setTradeState("2");
                    response.setTradeStateDesc("已支付");
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTimeEnd(respData.get(WepayField.TIME_END));
                    response.setBuyerId(respData.get(WepayField.OPEN_ID));
                    response.setTotalAmount(DataFormatUtils.getAmountFormatWepayAmount(respData.get(WepayField.TOTAL_FEE)));
                    response.setRefundAmount("0");
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                } else if (respData.get(WepayField.TRADE_STATE).equals("CLOSED")) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                } else if (respData.get(WepayField.TRADE_STATE).equals("NOTPAY") || respData.get(WepayField.TRADE_STATE).equals("USERPAYING")) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("1");
                    response.setTradeStateDesc("未支付");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                } else if (respData.get(WepayField.TRADE_STATE).equals("REFUND")) {
                    Map<String, String> refundData =  payClient.queryRefundBillByTradeNo(tradeNo);
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setRefundAmount(DataFormatUtils.getAmountFormatWepayAmount(refundData.get(WepayField.REFUND_FEE)));
                    response.setTotalAmount(DataFormatUtils.getAmountFormatWepayAmount(refundData.get(WepayField.TOTAL_FEE)));
                    response.setTradeState("3");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeStateDesc(respData.get(WepayField.TRADE_STATE_DESC));
                } else {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("5");
                    response.setTradeStateDesc("账单状态异常");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                }
            } else if (respData.get(WepayField.RETURN_CODE).equals(WepayField.FAIL)) {
                response.setCode("1");
                response.setMessage("系统异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("订单不存在");
            }
        }
        return response;
    }

    @Override
    public TradeQueryResponse queryByTransactionId(String transactionId) {
        TradeQueryResponse response = new TradeQueryResponse();
        try {
            Map<String, String> respData = payClient.queryByTransactionId(transactionId);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                response.setCode("0");
                response.setMessage("查询成功");
                if (respData.get(WepayField.TRADE_STATE).equals("SUCCESS")) {
                    response.setTradeState("2");
                    response.setTradeStateDesc("已支付");
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTimeEnd(respData.get(WepayField.TIME_END));
                    response.setBuyerId(respData.get(WepayField.OPEN_ID));
                    response.setTotalAmount(DataFormatUtils.getAmountFormatWepayAmount(respData.get(WepayField.TOTAL_FEE)));
                    response.setRefundAmount("0");
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                } else if (respData.get(WepayField.TRADE_STATE).equals("CLOSED")) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                } else if (respData.get(WepayField.TRADE_STATE).equals("NOTPAY") || respData.get(WepayField.TRADE_STATE).equals("USERPAYING")) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("1");
                    response.setTradeStateDesc("未支付");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                } else if (respData.get(WepayField.TRADE_STATE).equals("REFUND")) {
                    Map<String, String> refundData =  payClient.queryRefundBillByTransactionId(transactionId);
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setRefundAmount(DataFormatUtils.getAmountFormatWepayAmount(refundData.get(WepayField.REFUND_FEE)));
                    response.setTotalAmount(DataFormatUtils.getAmountFormatWepayAmount(refundData.get(WepayField.TOTAL_FEE)));
                    response.setTradeState("3");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeStateDesc(respData.get(WepayField.TRADE_STATE_DESC));
                } else {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeState("5");
                    response.setTradeStateDesc("账单状态异常");
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                }
            } else if (respData.get(WepayField.RETURN_CODE).equals(WepayField.FAIL)) {
                response.setCode("1");
                response.setMessage("系统异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradeRefundResponse refundApply(String tradeNo, String totalAmount, String refundDesc) {
        TradeRefundResponse response = new TradeRefundResponse();
        try {

            Map<String, String> respData = payClient.refundApply(tradeNo, DataFormatUtils.getWepayFormatAmount(totalAmount), refundDesc);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setCode("0");
                    response.setMessage("退款成功");
                    response.setRefundAmount(totalAmount);
                    response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                }
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.FAIL)) {
                    if (respData.get(WepayField.ERR_CODE).equals(WepayField.ORDER_NOT_EXIST)) {
                        response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                        response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                        response.setCode("0");
                        response.setMessage("未付款");
                        response.setRefundAmount("0.00");
                        response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                    } else {
                        response.setCode("1");
                        response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                    }
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradePrecreateResponse wapPayCreate(String outTradeNo, String subject, String totalAmount) {
//        TradePrecreateResponse response = null;
//
//        boolean exists = false;
//        StringBuilder form = new StringBuilder();
//
//        form.append("<form id=\"pay_form\" name=\"pay_form\"").append(" action=\"" + Alipay.GATEWAY).append(AlipayField.INPUT_CHARSET+"=").append(alipay.inputCharset).append("\" method=\"POST\">");
//        for (Map.Entry<String, String> param : payParams.entrySet()){
//            form.append("<input type=\"hidden\" name=\"")
//                    .append(param.getKey()).append("\" value=\"").append(param.getValue()).append("\" />");
//        }
//        form.append("<input type=\"submit\" value=\"去支付\" style=\"display:none;\" />");
//        form.append("</form>");
//        form.append("<script>document.forms['pay_form'].submit();</script>");


        return null;
    }


    @Override
    public TradeCloseResponse closeTradeByTradeNo(String tradeNo) throws Exception {
        TradeCloseResponse response = new TradeCloseResponse();
        try {
            Map<String, String> respData = payClient.closeTradeByTradeNo(tradeNo);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setCode("0");
                    response.setMessage("关闭成功");
                    response.setTradeState("4");
                    response.setTradeStateDesc("订单失效");
                } else if (respData.get(WepayField.ERR_CODE).equals(WepayField.ORDER_REFUND)) {
                    response.setCode("1");
                    response.setMessage("关闭失败,订单已退款");
                    response.setTradeState("3");
                    response.setTradeStateDesc("订单已退款");
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradePayResponse tradePay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String authCode) {
        TradePayResponse response = new TradePayResponse();
        try {
            Map<String, String> respData = payClient.tradePay(tradeNo, subject, DataFormatUtils.getWepayFormatAmount(totalAmount), timeStart, terminalId, transIntroduce, authCode);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setTradeNo(tradeNo);
                    response.setTotalAmount(totalAmount);
                    response.setTimeStart(timeStart);
                    response.setBuyerId(respData.get(WepayField.OPEN_ID));
                    response.setTradeState("2");
                    response.setTradeStateDesc("已支付");
                } else if (respData.get(WepayField.RESULT_CODE).equals(WepayField.FAIL)) {
                    response.setCode("0");
                    response.setTradeNo(tradeNo);
                    response.setTotalAmount(totalAmount);
                    response.setTimeStart(timeStart);
                    response.setBuyerId(respData.get(WepayField.OPEN_ID));
                    response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                    response.setTradeStateDesc(respData.get(WepayField.ERR_CODE_DES));
                    if (respData.get(WepayField.ERR_CODE).equals(WepayField.USER_PAYING)) {
                        response.setTradeState("1");
                    } else {
                        response.setTradeState("5");
                    }
                } else {
                    response.setCode("1");
                    response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradeRecordResponse billDownload(String billDate) {
        TradeRecordResponse response = new TradeRecordResponse();
        try {
            BillContainer billContainer = new BillContainer();
            response.setBillContainer(billContainer);
            billContainer.setPayType(PayTypeEnum.WEPAY.getPayType());
            Map<String, Object> respData = payClient.billDownload(billDate, getChannelConfig().getSellerId(), getChannelConfig().getChannelCode());
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {

                billContainer.setBill((Bill) respData.get("bill"));
                billContainer.setState("0");
                billContainer.setStateDesc("账单下载成功");
                response.setCode("0");
                response.setMessage("下载成功");
            } else if (respData.get(WepayField.RETURN_CODE).equals(WepayField.FAIL)) {
                if (respData.get(WepayField.EROOR_CODE).equals("20002")) {
                    billContainer.setState("1");
                    billContainer.setStateDesc("账单不存在");
                    response.setCode("0");
                    response.setMessage((String) respData.get(WepayField.RETURN_MSG));
                } else {
                    billContainer.setState("1");
                    billContainer.setStateDesc((String) respData.get(WepayField.RETURN_MSG));
                    response.setCode("1");
                    response.setMessage((String) respData.get(WepayField.RETURN_MSG));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradeCancelResponse cancelTradeByTradeNo(String tradeNo) throws Exception {
        TradeCancelResponse response = new TradeCancelResponse();
        Map<String, String> respData = null;
        try {
            respData = payClient.closeTradeByTradeNo(tradeNo);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                } else if (respData.get(WepayField.RESULT_CODE).equals(WepayField.FAIL) && respData.get(WepayField.ERR_CODE).equals(WepayField.USER_PAYING)) {
                    response.setCode("0");
                    response.setMessage(respData.get(WepayField.RETURN_MSG));
                    response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                } else {
                    response.setCode("1");
                    response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradeSignResponse doSign(String signContent) throws Exception {
        TradeSignResponse response = new TradeSignResponse();
        String signResult = payClient.rsaSign(signContent);
        response.setCharset("UTF-8");
        response.setCode("0");
        response.setMessage("加签成功");
        response.setSignType(WePayConstants.HMACSHA256);
        response.setSign(signResult);
        response.setVersion("1.0");
        return response;
    }

    @Override
    public TradeSignResponse doAndriodSign(String signContent) throws Exception {
        return doSign(signContent);
    }

    @Override
    public TradeRefundResponse refundPartApply(String tradeNo, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception {
        TradeRefundResponse response = new TradeRefundResponse();
        try {

            Map<String, String> respData = payClient.refundPartApply(tradeNo, DataFormatUtils.getWepayFormatAmount(totalAmount), DataFormatUtils.getWepayFormatAmount(refundAmount), outRequestNo, refundDesc);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setCode("0");
                    response.setMessage("退款成功");
                    response.setRefundAmount(refundAmount);
                    response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                }
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.FAIL)) {
                    if (respData.get(WepayField.ERR_CODE).equals(WepayField.ORDER_NOT_EXIST)) {
                        response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                        response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                        response.setCode("0");
                        response.setMessage("未付款");
                        response.setRefundAmount("0.00");
                        response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                    } else {
                        response.setCode("1");
                        response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                    }
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public TradeRefundResponse refundPartApplyByTransactionId(String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception {
        TradeRefundResponse response = new TradeRefundResponse();
        try {

            Map<String, String> respData = payClient.refundPartApplyByTransactionId(transactionId, DataFormatUtils.getWepayFormatAmount(totalAmount), DataFormatUtils.getWepayFormatAmount(refundAmount), outRequestNo, refundDesc);
            if (respData.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.SUCCESS)) {
                    response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                    response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                    response.setCode("0");
                    response.setMessage("退款成功");
                    response.setRefundAmount(refundAmount);
                    response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                }
                if (respData.get(WepayField.RESULT_CODE).equals(WepayField.FAIL)) {
                    if (respData.get(WepayField.ERR_CODE).equals(WepayField.ORDER_NOT_EXIST)) {
                        response.setTransactionId(respData.get(WepayField.TRANSACTION_ID));
                        response.setTradeNo(respData.get(WepayField.OUT_TRADE_NO));
                        response.setCode("0");
                        response.setMessage("未付款");
                        response.setRefundAmount("0.00");
                        response.setRefundTime(Dates.now("yyyyMMddHHmmss"));
                    } else {
                        response.setCode("1");
                        response.setMessage(respData.get(WepayField.ERR_CODE_DES));
                    }
                }
            } else {
                response.setCode("1");
                response.setMessage(respData.get(WepayField.RETURN_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode("1");
            if (StringUtils.hasLength(e.getMessage())) {
                response.setMessage(e.getMessage());
            } else {
                response.setMessage("系统异常");
            }
        }
        return response;
    }
}
