package com.zhch.paysvc.controller;

import com.zhch.paysvc.config.TradeStateEnum;
import com.zhch.paysvc.core.web.ListRes;
import com.zhch.paysvc.core.web.ObjRes;
import com.zhch.paysvc.core.web.PageRes;
import com.zhch.paysvc.dto.TradeRecordDto;
import com.zhch.paysvc.dto.TradeRecordQueryParams;
import com.zhch.paysvc.service.TradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lumos
 * @date 2020/10/18 04
 */
@RestController
@RequestMapping("/trade")
public class TradeRecordController {

    @Autowired
    TradeRecordService recordService;

    @GetMapping("/getTradeRecords")
    public PageRes<TradeRecordDto> getTradeRecords(TradeRecordQueryParams params) {
        List<TradeRecordDto> records = this.recordService.getTradeRecords(params);
        return new PageRes<>(params.getPage().getTotal(), params.getPagesize(), records);
    }

    @GetMapping("/getTradeDetail")
    public ObjRes<TradeRecordDto> getTradeDetail(Long id) throws Exception{
        TradeRecordDto record = this.recordService.getTradeDetail(id);
        return new ObjRes<TradeRecordDto>().data(record);
    }

    @GetMapping("/getTradeStates")
    public ListRes<Map<String, String>> getTradeStates() {
        List<Map<String, String>> states = Arrays.stream(TradeStateEnum.values()).map(e -> {
            Map<String, String> map = new HashMap<>(2);
            map.put("state", e.getState());
            map.put("label", e.getLabel());
            return map;
        }).collect(Collectors.toList());
        return new ListRes<Map<String, String>>().data(states);
    }

    @PostMapping("/refund")
    public ObjRes<TradeRecordDto> refund(Long id) throws Exception {
        TradeRecordDto record = this.recordService.refund(id);
        return new ObjRes<TradeRecordDto>().data(record);
    }

}
