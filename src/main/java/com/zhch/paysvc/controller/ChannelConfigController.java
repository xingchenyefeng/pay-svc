package com.zhch.paysvc.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.zhch.paysvc.config.AlipayChannelEnum;
import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.core.web.ListRes;
import com.zhch.paysvc.core.web.ObjRes;
import com.zhch.paysvc.core.web.PageRes;
import com.zhch.paysvc.dto.ChannelConfigDto;
import com.zhch.paysvc.dto.ChannelConfigQueryParams;
import com.zhch.paysvc.entity.ChannelCert;
import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.entity.HospitalConfig;
import com.zhch.paysvc.exception.ChannelConfigNotExistsException;
import com.zhch.paysvc.service.ChannelConfigService;
import com.zhch.paysvc.support.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lumos
 * @date 2020/10/15 00
 */
@RestController
@RequestMapping("/channel")
public class ChannelConfigController {

    @Autowired
    ChannelConfigService channelConfigService;

    @Autowired
    MessageProcessor messageProcessor;

    @GetMapping("/getChannelConfigs")
    public PageRes<ChannelConfigDto> getChannelConfigs(ChannelConfigQueryParams params) {
        List<ChannelConfigDto> channelConfigs = this.channelConfigService.getChannelConfigs(params);
        if (CollectionUtil.isNotEmpty(channelConfigs)) {
            channelConfigs.forEach(e -> {
                e.setPayName(PayTypeEnum.getPayName(e.getPayType()));
            });
        }
        return new PageRes<>(params.getPage().getTotal(), params.getPagesize(), channelConfigs);
    }

    @GetMapping("/getChannelCodes")
    public ListRes<String> getChannelCodes(String code) {
        List<String> channelCodes = this.channelConfigService.getChannelCodes(code);
        return new ListRes<String>().data(channelCodes);
    }

    @PostMapping("/saveChannelConfig")
    public ObjRes<ChannelConfigDto> saveChannelConfig(@RequestBody ChannelConfigDto config) {
        this.channelConfigService.saveChannelConfig(config);
        return new ObjRes<ChannelConfigDto>().data(config);
    }

    @GetMapping("/getChannelConfigById")
    public ObjRes<ChannelConfigDto> getChannelConfigById(Long id) {
        ChannelConfigDto config = this.channelConfigService.getChannelConfigById(id);
        return new ObjRes<ChannelConfigDto>().data(config);
    }

    @GetMapping("/getAlipayChannels")
    public ListRes<Map<String, String>> getAlipayChannels() {
        List<Map<String, String>> results = new ArrayList<>(AlipayChannelEnum.values().length);
        for (AlipayChannelEnum channelEnum : AlipayChannelEnum.values()) {
            Map<String, String> el = new HashMap<>(2);
            el.put("value", channelEnum.getValue());
            el.put("label", channelEnum.getLabel());
            results.add(el);
        }
        return new ListRes<Map<String, String>>().data(results);
    }

    @PostMapping("/uploadChannelCert")
    public ObjRes<Map<String, String>> uploadChannelCert(MultipartHttpServletRequest request) throws Exception {
        Map<String, MultipartFile> fileMap = request.getFileMap();
        MultipartFile file = fileMap.get("file");
        ChannelCert channelCert = channelConfigService.uploadChannelCert(file.getOriginalFilename(), file.getInputStream());
        Map<String, String> responseMap = new HashMap<>(1);
        responseMap.put("certId", channelCert.getId().toString());
        return new ObjRes<Map<String, String>>().data(responseMap);
    }

    @PostMapping("/release")
    public ObjRes<ChannelConfig> release(String channelCode) throws Exception{
        ChannelConfig channelConfig = this.channelConfigService.getByChannelCode(channelCode);
        if (channelConfig==null) {
            throw new ChannelConfigNotExistsException(String.format("%s 的渠道信息未找到",channelCode));
        }
        if (!channelConfig.getActive()){
            if (channelConfig.getCertId()!=null && channelConfig.getCertId()>0) {
                ChannelCert channelCert = channelConfigService.getChannelCert(channelConfig.getCertId());
                channelConfig.setCert(channelCert.getCert());
            }
            messageProcessor.releasePaySupport(channelConfig);
            channelConfig.setActive(true);
            this.channelConfigService.updateActiveVal(channelConfig);
        }
        return new ObjRes<ChannelConfig>().data(channelConfig);
    }

    @PostMapping("/unreleased")
    public ObjRes<ChannelConfig> unreleased(String channelCode) throws Exception{
        ChannelConfig channelConfig = this.channelConfigService.getByChannelCode(channelCode);
        if (channelConfig==null) {
            throw new ChannelConfigNotExistsException(String.format("%s 的渠道信息未找到",channelCode));
        }
        if (channelConfig.getActive()){
            messageProcessor.destroyPaySupport(channelConfig);
            channelConfig.setActive(false);
            this.channelConfigService.updateActiveVal(channelConfig);
        }
        return new ObjRes<ChannelConfig>().data(channelConfig);
    }
}
