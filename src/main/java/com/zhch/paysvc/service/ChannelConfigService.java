package com.zhch.paysvc.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.core.entity.EntityKit;
import com.zhch.paysvc.core.service.BaseService;
import com.zhch.paysvc.dto.ChannelConfigDto;
import com.zhch.paysvc.dto.ChannelConfigQueryParams;
import com.zhch.paysvc.entity.ChannelCert;
import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.exception.ChannelConfigDuplicateException;
import com.zhch.paysvc.mapper.ChannelCertMapper;
import com.zhch.paysvc.mapper.ChannelConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lumos
 * @date 2020/10/11 59
 */
@Service
public class ChannelConfigService extends BaseService<ChannelConfig> {

    @Autowired
    private ChannelConfigMapper configMapper;

    @Autowired
    private ChannelCertMapper channelCertMapper;

    public List<ChannelConfig> findAvailableChannelConfig() {
        QueryWrapper<ChannelConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ChannelConfig.TBL_ACTIVE, true);
        List<ChannelConfig> channelConfigs = this.configMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(channelConfigs)) {
            channelConfigs.forEach(e -> {
                if (e.getPayType().equals(PayTypeEnum.WEPAY.getPayType())) {
                    ChannelCert channelCert = channelCertMapper.selectById(e.getCertId());
                    if (channelCert != null) {
                        e.setCert(channelCert.getCert());
                    }
                }
            });
        }
        return channelConfigs;
    }

    public ChannelConfig getByChannelCode(String channelCode) {
        QueryWrapper<ChannelConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ChannelConfig.TBL_CHANNEL_CODE, channelCode);
        return configMapper.selectOne(queryWrapper);
    }

    public List<ChannelConfigDto> getChannelConfigs(ChannelConfigQueryParams params) {
        return this.configMapper.getChannelConfigs(params.getHospitalName(),
                params.getChannelCode(),
                params.getPayType(),
                params.getActive(),
                params.getCreatedStartAt(),
                params.getCreatedEndAt(),
                params.builderPage());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveChannelConfig(ChannelConfigDto config) {
        if (config.getId() != null) {
            if (this.isDuplicate(config.getId(), new QueryWrapper<ChannelConfig>().eq(ChannelConfig.TBL_CHANNEL_CODE, config.getChannelCode()))) {
                throw new ChannelConfigDuplicateException();
            }
            config.setActive(false);
            this.updateById(config);
        } else {
            if (this.isDuplicate(null, new QueryWrapper<ChannelConfig>()
                    .eq(ChannelConfig.TBL_CHANNEL_CODE, config.getChannelCode()))) {
                throw new ChannelConfigDuplicateException();
            }
            config.setActive(false);
            this.insert(config);
        }
    }


    public ChannelConfigDto getChannelConfigById(Long id) {
        return this.configMapper.getChannelConfigById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChannelCert uploadChannelCert(String originalFilename, InputStream inputStream) {
        ChannelCert channelCert = new ChannelCert();
        channelCert.setFileName(originalFilename);
        channelCert.setCert(IoUtil.readBytes(inputStream));
        EntityKit.preCreateWithOverrideAll(channelCert);
        this.channelCertMapper.insert(channelCert);
        return channelCert;
    }

    public List<String> getChannelCodes(String code) {
        LambdaQueryWrapper<ChannelConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ChannelConfig::getChannelCode, code);
        queryWrapper.last("limit 10");
        queryWrapper.select(ChannelConfig::getChannelCode);
        List<ChannelConfig> channelConfigs = this.configMapper.selectList(queryWrapper);
        return channelConfigs.stream().map(ChannelConfig::getChannelCode).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateActiveVal(ChannelConfig channelConfig) {
        ChannelConfig persist = new ChannelConfig();
        persist.setId(channelConfig.getId());
        persist.setActive(channelConfig.getActive());
        this.updateById(channelConfig);
    }

    public ChannelCert getChannelCert(Long certId) {
        return channelCertMapper.selectById(certId);
    }
}
