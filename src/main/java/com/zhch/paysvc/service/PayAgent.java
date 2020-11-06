package com.zhch.paysvc.service;

import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.entity.TradeRecord;
import com.zhch.paysvc.support.IPayAgent;
import com.zhch.paysvc.support.PaySupport;
import com.zhch.paysvc.support.PaySupportServicePool;
import com.zhch.paysvc.support.bean.*;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.Dates;
import com.zhch.paysvc.utils.OrderNumberGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author lumos
 */
@Component
@Log4j2
public class PayAgent implements IPayAgent {


    private PaySupportServicePool paySupportServicePool;
    @Autowired
    private ChannelConfigService channelConfigService;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Override
    public void setPaySupportServicePool(PaySupportServicePool paySupportServicePool) {
        this.paySupportServicePool = paySupportServicePool;
    }

    @Override
    public PaySupportServicePool getPaySupportServicePool() {
        return paySupportServicePool;
    }

    @Override
    public TradePrecreateResponse qrPay(String channelCode, String bsType, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo, String timeoutExpress) throws Exception {
        TradePrecreateResponse response = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        if (PayTypeEnum.ALIPAY.getPayType().equals(channelConfig.getPayType())) {
            Assert.isTrue(Integer.parseInt(timeoutExpress) >= 1, "timeoutExpress 参数不正确");
        } else if (PayTypeEnum.WEPAY.getPayType().equals(channelConfig.getPayType())) {
            Assert.isTrue(Integer.parseInt(timeoutExpress) >= 5, "timeoutExpress 参数不正确");
        }
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        String tradeNo = OrderNumberGenerator.generatorOrderNum(channelCode);
        response = paySupport.qrPay(tradeNo, subject, totalAmount, timeStart, deviceInfo, transIntroduce, timeoutExpress);
        response.setOutTradeNo(outTradeNo);
        if ("0".equals(response.getCode())) {
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setBsType(bsType);
            tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
            tradeRecord.setDeptCode(deptCode);
            tradeRecord.setFeeType("CNY");
            tradeRecord.setTradeNo(tradeNo);
            tradeRecord.setIdCard(idCard);
            tradeRecord.setDoctorCode(doctorCode);
            tradeRecord.setLimitPay(channelConfig.getEnablePayChannels());
            tradeRecord.setName(name);
            tradeRecord.setPayAmount(totalAmount);
            tradeRecord.setOutTradeNo(outTradeNo);
            tradeRecord.setPhone(phone);
            tradeRecord.setReceiveTime(Dates.now());
            tradeRecord.setState("1");
            tradeRecord.setReceiveTime(DataFormatUtils.noSpaceParser(timeStart));
            tradeRecord.setPayType(channelConfig.getPayType());
            tradeRecord.setTradeType("QR");
            tradeRecord.setTransDetail(transDetail);
            tradeRecord.setSubject(subject);
            tradeRecord.setTransIntroduce(transIntroduce);
            tradeRecord.setTotalAmount(totalAmount);
            tradeRecord.setTimeStart(timeStart);
            tradeRecord.setResultText("订单创建");
            tradeRecord.setTimeExpire(timeoutExpress);
            tradeRecord.setChannelCode(channelConfig.getChannelCode());
            tradeRecord.setDeviceInfo(deviceInfo);
            tradeRecord.setState("1");
            tradeRecordService.addTradeRecord(tradeRecord);
        }
        return response;
    }

    @Override
    public TradeQueryResponse queryByTradeNo(String channelCode, String tradeNo, String transactionId) throws Exception {
        TradeRecord tradeRecord = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        TradeQueryResponse queryResponse = null;
        if (StringUtils.hasLength(tradeNo)) {
            queryResponse = paySupport.queryByTradeNo(tradeNo);
        } else {
            queryResponse = paySupport.queryByTransactionId(transactionId);
            tradeNo = queryResponse.getTradeNo();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transactionId);
        params.put("tradeNo", tradeNo);
        params.put("channelCode", channelCode);
        params.put("hospitalCode", channelConfig.getHospitalCode());
        tradeRecord = tradeRecordService.getTradeRecord(params);
        if (tradeRecord == null) {
            TradeQueryResponse tradeQueryResponse = paySupport.queryByTradeNo(tradeNo);
            if ("0".equals(tradeQueryResponse.getCode())) {
                tradeRecord = new TradeRecord();
                tradeRecord.setChannelCode(channelCode);
                tradeRecord.setTransactionId(tradeQueryResponse.getTransactionId());
                tradeRecord.setBuyerId(tradeQueryResponse.getBuyerId());
                tradeRecord.setTradeNo(tradeNo);
                tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
                tradeRecord.setState(tradeQueryResponse.getTradeState());
                tradeRecord.setResultText(tradeQueryResponse.getTradeStateDesc());
                tradeRecord.setPayType(channelConfig.getPayType());
                tradeRecord.setPayAmount(tradeQueryResponse.getReceiveAmount());
                tradeRecordService.addTradeRecord(tradeRecord);
            }
        }
        if ("0".equals(queryResponse.getCode()) && !queryResponse.getTradeState().equals(tradeRecord.getState())) {
            tradeRecord.setState(queryResponse.getTradeState());
            tradeRecord.setResultText(queryResponse.getTradeStateDesc());
            tradeRecord.setTransactionId(queryResponse.getTransactionId());
            tradeRecord.setTimeEnd(queryResponse.getTimeEnd());
            tradeRecord.setBuyerId(queryResponse.getBuyerId());
            tradeRecord.setReceiptAmount(tradeRecord.getPayAmount());
            this.tradeRecordService.modify(tradeRecord);
        }
        return queryResponse;
    }

    @Override
    public TradeRefundResponse refundApply(String channelCode, String tradeNo, String totalAmount, String refundTime, String deviceInfo, String refundDesc) throws Exception {
        TradeRecord tradeRecord = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        TradeRefundResponse tradeRefundResponse = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("tradeNo", tradeNo);
            params.put("channelCode", channelCode);
            params.put("payType", channelConfig.getPayType());
            params.put("hospitalCode", channelConfig.getHospitalCode());
            tradeRecord = tradeRecordService.getTradeRecord(params);
            if (tradeRecord == null) {
                TradeQueryResponse tradeQueryResponse = paySupport.queryByTradeNo(tradeNo);
                if ("0".equals(tradeQueryResponse.getCode())) {
                    tradeRecord = new TradeRecord();
                    tradeRecord.setChannelCode(channelCode);
                    tradeRecord.setTransactionId(tradeQueryResponse.getTransactionId());
                    tradeRecord.setBuyerId(tradeQueryResponse.getBuyerId());
                    tradeRecord.setTradeNo(tradeNo);
                    tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
                    tradeRecord.setState(tradeQueryResponse.getTradeState());
                    tradeRecord.setResultText(tradeQueryResponse.getTradeStateDesc());
                    tradeRecord.setPayType(channelConfig.getPayType());
                    tradeRecord.setPayAmount(tradeQueryResponse.getReceiveAmount());
                    tradeRecordService.addTradeRecord(tradeRecord);
                }
            }
            tradeRefundResponse = paySupport.refundApply(tradeNo, totalAmount, refundDesc);
            if ("0".equals(tradeRefundResponse.getCode())) {
                Objects.requireNonNull(tradeRecord).setRefundDesc(refundDesc);
                if (tradeRefundResponse.getRefundAmount() == null) {
                    tradeRefundResponse.setRefundAmount("0.00");
                    tradeRecord.setState("6");
                    tradeRecord.setResultText("取消支付");
                } else {
                    tradeRecord.setState("3");
                    tradeRecord.setResultText("已退费");
                    tradeRecord.setRefundAmount(tradeRefundResponse.getRefundAmount());
                    tradeRecord.setRefundTime(tradeRefundResponse.getRefundTime());
                    tradeRecord.setBuyerId(tradeRefundResponse.getBuyerId());
                }
                this.tradeRecordService.modify(tradeRecord);
            }
        } finally {
            TradeCancelResponse cancelResponse = paySupport.cancelTradeByTradeNo(tradeNo);
            if ("0".equals(cancelResponse.getCode())) {
                tradeRecord.setState("4");
                tradeRecord.setResultText("订单已关闭");
                tradeRecord.setRefundAmount(tradeRefundResponse.getRefundAmount());
                tradeRecord.setRefundTime(Dates.now("yyyy-MM-dd hh:mm:ss"));
                this.tradeRecordService.modify(tradeRecord);
            }
        }
        return tradeRefundResponse;
    }


    @Override
    public TradePrecreateResponse wapPayCreate(String channelCode, String bsType, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo) throws Exception {
        TradePrecreateResponse response = null;
        boolean exists = false;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        Map<String, Object> params = new HashMap<>();
        params.put("outTradeNo", outTradeNo);
        params.put("channelCode", channelCode);
        params.put("payType", channelConfig.getPayType());
        params.put("hospitalCode", channelConfig.getHospitalCode());
        String tradeNo = OrderNumberGenerator.generatorOrderNum(channelCode);
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setBsType(bsType);
        tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
        tradeRecord.setDeptCode(deptCode);
        tradeRecord.setFeeType("CNY");
        tradeRecord.setTradeNo(tradeNo);
        tradeRecord.setIdCard(idCard);
        tradeRecord.setDoctorCode(doctorCode);
        tradeRecord.setLimitPay(channelConfig.getEnablePayChannels());
        tradeRecord.setName(name);
        tradeRecord.setPayAmount(totalAmount);
        tradeRecord.setOutTradeNo(outTradeNo);
        tradeRecord.setPhone(phone);
        tradeRecord.setReceiveTime(Dates.now());
        tradeRecord.setState("1");
        tradeRecord.setReceiveTime(DataFormatUtils.noSpaceParser(timeStart));
        tradeRecord.setPayType(channelConfig.getPayType());
        tradeRecord.setTradeType("WAP");
        tradeRecord.setTransDetail(transDetail);
        tradeRecord.setSubject(subject);
        tradeRecord.setTransIntroduce(transIntroduce);
        tradeRecord.setTotalAmount(totalAmount);
        tradeRecord.setTimeStart(timeStart);
        tradeRecord.setResultText("订单创建");
        tradeRecord.setChannelCode(channelCode);
        tradeRecord.setDeviceInfo(deviceInfo);
        response = paySupport.wapPayCreate(tradeNo, subject, totalAmount);
        if (response.getCode().equals("0")) {
            tradeRecord.setState("1");
            tradeRecordService.addTradeRecord(tradeRecord);
        }
        return response;
    }

    @Override
    public TradeCloseResponse closeTrade(String channelCode, String tradeNo) throws Exception {
        TradeCloseResponse response = null;
        boolean exists = false;
        response = new TradeCloseResponse();
        TradeRecord tradeRecord = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        Map<String, Object> params = new HashMap<>();
        params.put("tradeNo", tradeNo);
        params.put("channelCode", channelCode);
        params.put("payType", channelConfig.getPayType());
        params.put("hospitalCode", channelConfig.getHospitalCode());
        tradeRecord = tradeRecordService.getTradeRecord(params);
        if (tradeRecord == null) {
            throw new RuntimeException("未查询到该订单");
        }
        if (tradeRecord.getState().equals("2")) {
            /**
             * code 为2 表示订单已经支付，关闭失败
             */
            response.setCode("2");
            response.setMessage("订单已支付,交易关闭失败");
            response.setTradeState("2");
            response.setTradeStateDesc("已支付");
            response.setTransactionId(tradeRecord.getTradeNo());
            response.setTimeEnd(tradeRecord.getTimeEnd());
            response.setBuyerId(tradeRecord.getBuyerId());
            response.setTradeNo(tradeNo);
        } else {
            response = paySupport.closeTradeByTradeNo(tradeNo);
            if (response.getCode().equals("0") && !response.getTradeState().equals(tradeRecord.getState())) {
                tradeRecord.setState(response.getTradeState());
                tradeRecord.setResultText(response.getTradeStateDesc());
                this.tradeRecordService.modify(tradeRecord);
            }
        }
        return response;
    }

    @Override
    public TradePayResponse tradePay(String channelCode, String bsType, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo, String authCode) throws Exception {
        TradePayResponse response = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        String tradeNo = OrderNumberGenerator.generatorOrderNum(channelCode);
        response = paySupport.tradePay(tradeNo, subject, totalAmount, timeStart, deviceInfo, transIntroduce, authCode);
        response.setOutTradeNo(outTradeNo);
        if (response.getCode().equals("0")) {
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setBsType(bsType);
            tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
            tradeRecord.setDeptCode(deptCode);
            tradeRecord.setFeeType("CNY");
            tradeRecord.setTradeNo(tradeNo);
            tradeRecord.setIdCard(idCard);
            tradeRecord.setDoctorCode(doctorCode);
            tradeRecord.setLimitPay(channelConfig.getEnablePayChannels());
            tradeRecord.setName(name);
            tradeRecord.setPayAmount(totalAmount);
            tradeRecord.setOutTradeNo(outTradeNo);
            tradeRecord.setPhone(phone);
            tradeRecord.setReceiveTime(Dates.now());
            tradeRecord.setState(response.getTradeState());
            tradeRecord.setReceiveTime(DataFormatUtils.noSpaceParser(timeStart));
            tradeRecord.setPayType(channelConfig.getPayType());
            tradeRecord.setTradeType("BAR_CODE");
            tradeRecord.setTransDetail(transDetail);
            tradeRecord.setSubject(subject);
            tradeRecord.setTransIntroduce(transIntroduce);
            tradeRecord.setTotalAmount(totalAmount);
            tradeRecord.setTimeStart(timeStart);
            tradeRecord.setResultText(response.getTradeStateDesc());
            tradeRecord.setChannelCode(channelCode);
            tradeRecord.setDeviceInfo(deviceInfo);
            tradeRecord.setTransactionId(response.getTransactionId());
            tradeRecordService.addTradeRecord(tradeRecord);
        }
        return response;
    }

    @Override
    public TradeRecordResponse billDownload(String channelCode, String billDate) throws Exception {
        TradeRecordResponse response = null;
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        response = paySupport.billDownload(billDate);
        return response;
    }

    @Override
    public TradeSignResponse doSign(String channelCode, String signContent) throws Exception {
        TradeSignResponse response = null;
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        response = paySupport.doSign(signContent);
        return response;
    }

    @Override
    public TradeSignResponse doAndriodSign(String channelCode, String signContent) throws Exception {
        TradeSignResponse response = null;
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        response = paySupport.doAndriodSign(signContent);
        return response;
    }

    @Override
    public TradeRefundResponse refundPartApply(String channelCode, String tradeNo, String totalAmount, String refundAmount, String outRequestNo, String refundTime, String deviceInfo, String refundDesc) throws Exception {
        TradeRecord tradeRecord = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        TradeRefundResponse tradeRefundResponse = null;
        Map<String, Object> params = new HashMap<>(8);
        params.put("tradeNo", tradeNo);
        params.put("channelCode", channelCode);
        params.put("payType", channelConfig.getPayType());
        params.put("hospitalCode", channelConfig.getHospitalCode());
        tradeRecord = tradeRecordService.getTradeRecord(params);
        if (tradeRecord == null) {
            TradeQueryResponse tradeQueryResponse = paySupport.queryByTradeNo(tradeNo);
            if (tradeQueryResponse.getCode().equals("0")) {
                tradeRecord = new TradeRecord();
                tradeRecord.setChannelCode(channelCode);
                tradeRecord.setTransactionId(tradeQueryResponse.getTransactionId());
                tradeRecord.setBuyerId(tradeQueryResponse.getBuyerId());
                tradeRecord.setTradeNo(tradeNo);
                tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
                tradeRecord.setState(tradeQueryResponse.getTradeState());
                tradeRecord.setResultText(tradeQueryResponse.getTradeStateDesc());
                tradeRecord.setPayType(channelConfig.getPayType());
                tradeRecord.setPayAmount(tradeQueryResponse.getReceiveAmount());
                tradeRecordService.addTradeRecord(tradeRecord);
            }
        }
        tradeRefundResponse = paySupport.refundPartApply(tradeNo, totalAmount, refundAmount, outRequestNo, refundDesc);
        if (tradeRefundResponse.getCode().equals("0")) {
            tradeRecord.setRefundDesc(refundDesc);
            if (tradeRefundResponse.getRefundAmount() == null) {
                tradeRefundResponse.setRefundAmount("0.00");
                tradeRecord.setState("6");
                tradeRecord.setResultText("取消支付");
            } else if (tradeRefundResponse.getRefundAmount().equals("0.0")) {
                tradeRecord.setState("1");
                tradeRecord.setResultText("未付款");
            } else {
                tradeRecord.setState("3");
                tradeRecord.setResultText("已退费");
                tradeRecord.setRefundAmount(tradeRefundResponse.getRefundAmount());
                tradeRecord.setRefundTime(tradeRefundResponse.getRefundTime());
                tradeRecord.setBuyerId(tradeRefundResponse.getBuyerId());
            }
            this.tradeRecordService.modify(tradeRecord);
        }
        return tradeRefundResponse;
    }

    @Override
    public TradeQueryResponse noticeComplete(String channelCode, String bsType, String tradeNo, String outTradeNo, String totalAmount, String subject, String idCard, String phone, String name, String transIntroduce, String timeStart, String deptCode, String doctorCode, String transDetail, String deviceInfo) throws Exception {
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        TradeQueryResponse queryResponse = paySupport.queryByTradeNo(tradeNo);
        if (queryResponse.getCode().equals("0")) {
            Map<String, Object> params = new HashMap<>();
            params.put("tradeNo", tradeNo);
            params.put("channelCode", channelCode);
            TradeRecord tradeRecord = tradeRecordService.getTradeRecord(params);
            if (tradeRecord == null) {
                tradeRecord = new TradeRecord();
                tradeRecord.setBsType(bsType);
                tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
                tradeRecord.setDeptCode(deptCode);
                tradeRecord.setFeeType("CNY");
                tradeRecord.setTradeNo(tradeNo);
                tradeRecord.setIdCard(idCard);
                tradeRecord.setDoctorCode(doctorCode);
                tradeRecord.setLimitPay(channelConfig.getEnablePayChannels());
                tradeRecord.setName(name);
                tradeRecord.setPayAmount(totalAmount);
                tradeRecord.setOutTradeNo(outTradeNo);
                tradeRecord.setPhone(phone);
                tradeRecord.setReceiveTime(Dates.now());
                tradeRecord.setPayType(channelConfig.getPayType());
                tradeRecord.setTradeType("JS");
                tradeRecord.setTransDetail(transDetail);
                tradeRecord.setSubject(subject);
                tradeRecord.setTransIntroduce(transIntroduce);
                tradeRecord.setTotalAmount(totalAmount);
                tradeRecord.setTimeStart(timeStart);
                tradeRecord.setResultText("已支付");
                tradeRecord.setChannelCode(channelConfig.getChannelCode());
                tradeRecord.setDeviceInfo(deviceInfo);
                this.tradeRecordService.addTradeRecord(tradeRecord);
            } else if (queryResponse.getCode().equals("0") && !queryResponse.getTradeState().equals(tradeRecord.getState())) {
                tradeRecord.setState(queryResponse.getTradeState());
                tradeRecord.setResultText(queryResponse.getTradeStateDesc());
                tradeRecord.setTransactionId(queryResponse.getTransactionId());
                tradeRecord.setTimeEnd(queryResponse.getTimeEnd());
                tradeRecord.setBuyerId(queryResponse.getBuyerId());
                tradeRecord.setReceiptAmount(tradeRecord.getPayAmount());
                this.tradeRecordService.modify(tradeRecord);
            }
        }
        return queryResponse;
    }

    @Override
    public TradeRefundResponse refundPartApplyByTransactionId(String channelCode, String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundTime, String deviceInfo, String refundDesc) throws Exception {
        TradeRecord tradeRecord = null;
        ChannelConfig channelConfig = channelConfigService.getByChannelCode(channelCode);
        Assert.notNull(channelConfig, "支付通道未配置或者已失效");
        PaySupport paySupport = paySupportServicePool.getPaySupport(channelCode);
        TradeRefundResponse tradeRefundResponse = null;
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transactionId);
        params.put("channelCode", channelCode);
        params.put("payType", channelConfig.getPayType());
        params.put("hospitalCode", channelConfig.getHospitalCode());
        tradeRecord = tradeRecordService.getTradeRecord(params);
        if (tradeRecord == null) {
            TradeQueryResponse tradeQueryResponse = paySupport.queryByTransactionId(transactionId);
            if (tradeQueryResponse.getCode().equals("0")) {
                tradeRecord = new TradeRecord();
                tradeRecord.setChannelCode(channelCode);
                tradeRecord.setTransactionId(tradeQueryResponse.getTransactionId());
                tradeRecord.setBuyerId(tradeQueryResponse.getBuyerId());
                tradeRecord.setTradeNo(tradeQueryResponse.getTradeNo());
                tradeRecord.setHospitalCode(channelConfig.getHospitalCode());
                tradeRecord.setState(tradeQueryResponse.getTradeState());
                tradeRecord.setResultText(tradeQueryResponse.getTradeStateDesc());
                tradeRecord.setPayType(channelConfig.getPayType());
                tradeRecord.setPayAmount(tradeQueryResponse.getReceiveAmount());
                tradeRecordService.addTradeRecord(tradeRecord);
            }
        }
        tradeRefundResponse = paySupport.refundPartApplyByTransactionId(transactionId, totalAmount, refundAmount, outRequestNo, refundDesc);
        if (tradeRefundResponse.getCode().equals("0")) {
            tradeRecord.setRefundDesc(refundDesc);
            if (tradeRefundResponse.getRefundAmount() == null) {
                tradeRefundResponse.setRefundAmount("0.00");
                tradeRecord.setState("6");
                tradeRecord.setResultText("取消支付");
            } else if (tradeRefundResponse.getRefundAmount().equals("0.0")) {
                tradeRecord.setState("1");
                tradeRecord.setResultText("未付款");
            } else {
                tradeRecord.setState("3");
                tradeRecord.setResultText("已退费");
                tradeRecord.setRefundAmount(tradeRefundResponse.getRefundAmount());
                tradeRecord.setRefundTime(tradeRefundResponse.getRefundTime());
                tradeRecord.setBuyerId(tradeRefundResponse.getBuyerId());
            }
            this.tradeRecordService.modify(tradeRecord);
        }
        return tradeRefundResponse;
    }
}
