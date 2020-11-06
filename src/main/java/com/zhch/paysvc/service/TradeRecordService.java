package com.zhch.paysvc.service;

import com.alibaba.fastjson.JSON;
import com.zhch.paysvc.core.config.Constants;
import com.zhch.paysvc.core.service.BaseService;
import com.zhch.paysvc.dto.TradeRecordDto;
import com.zhch.paysvc.dto.TradeRecordQueryParams;
import com.zhch.paysvc.entity.TradeRecord;
import com.zhch.paysvc.exception.TradeRefundException;
import com.zhch.paysvc.mapper.TradeRecordMapper;
import com.zhch.paysvc.support.MessageProcessor;
import com.zhch.paysvc.support.RequestMessage;
import com.zhch.paysvc.support.ResponseMessage;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lumos
 * @date 2020/10/11 13
 */
@Service
public class TradeRecordService extends BaseService<TradeRecord> {

    @Autowired
    TradeRecordMapper recordMapper;

    @Autowired
    MessageProcessor processor;

    TradeRecord getTradeRecord(Map<String, Object> params) {
        String tradeNo = (String) params.get("tradeNo");
        String transactionId = (String) params.get("transactionId");
        String channelCode = (String) params.get("channelCode");
        String outTradeNo = (String) params.get("outTradeNo");
        return this.recordMapper.getTradeRecord(tradeNo, transactionId, channelCode, outTradeNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTradeRecord(TradeRecord tradeRecord) {
        this.insert(tradeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(TradeRecord tradeRecord) {
        this.updateById(tradeRecord);
    }

    public List<TradeRecordDto> getTradeRecords(TradeRecordQueryParams params) {
        if (StringUtils.hasLength(params.getTradeNo()) && StringUtils.hasLength(params.getChannelCode())) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("tradeNo", params.getTradeNo());
            data.put("channelCode", params.getChannelCode());
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setFuncode("720001");
            requestMessage.setCaptcha("token");
            requestMessage.setArgs(data);
            try {
                processor.handler(requestMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.recordMapper.getTradeRecords(
                params.getTradeNo(),
                params.getTransactionId(),
                params.getChannelCode(),
                params.getHospitalCode(),
                params.getOutTradeNo(),
                params.getPhone(),
                params.getName(),
                params.getPayType(),
                params.getState(),
                params.getCreatedStartAt(),
                params.getCreatedEndAt(),
                params.builderPage());
    }

    public TradeRecordDto getTradeDetail(Long id) throws Exception {
        TradeRecordDto record = this.recordMapper.getTradeDetail(id);
        Map<String, Object> data = new HashMap<>(2);
        data.put("tradeNo", record.getTradeNo());
        data.put("channelCode", record.getChannelCode());
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setFuncode("720001");
        requestMessage.setCaptcha("token");
        requestMessage.setArgs(data);
        ResponseMessage responseMessage = processor.handler(requestMessage);
        if (responseMessage.getCode().equals(Constants.SUCCESS)) {
            return this.recordMapper.getTradeDetail(id);
        } else {
            throw new TradeRefundException();
        }
    }

    public TradeRecordDto refund(Long id) throws Exception {
        TradeRecordDto record = this.getTradeDetail(id);
        Map<String, Object> params = new HashMap<>(16);
        params.put("channelCode", record.getChannelCode());
        params.put("tradeNo", record.getTradeNo());
        params.put("totalAmount", record.getTotalAmount());
        params.put("refundTime", DataFormatUtils.getTimeStringNoSpace(new Date()));
        params.put("deviceInfo", "system");
        params.put("refundDesc", "管理员退费");
        params.put("sign", DesUtil.encrypt(JSON.toJSONString(params)));
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setFuncode("710001");
        requestMessage.setCaptcha("token");
        requestMessage.setArgs(params);
        ResponseMessage responseMessage = processor.handler(requestMessage);
        if (Constants.SUCCESS.equals(responseMessage.getCode())) {
            return this.getTradeDetail(id);
        } else {
            throw new TradeRefundException(responseMessage.getMessage());
        }
    }
}
