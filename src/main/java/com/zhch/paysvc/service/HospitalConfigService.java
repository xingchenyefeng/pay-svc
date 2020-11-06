package com.zhch.paysvc.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhch.paysvc.core.service.BaseService;
import com.zhch.paysvc.dto.HospitalQueryParams;
import com.zhch.paysvc.entity.HospitalConfig;
import com.zhch.paysvc.exception.HospitalConfigDuplicateException;
import com.zhch.paysvc.mapper.HospitalConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lumos
 * @date 2020/10/12 01
 */
@Service
public class HospitalConfigService extends BaseService<HospitalConfig> {

    @Autowired
    HospitalConfigMapper hospitalConfigMapper;

    public List<HospitalConfig> getHospitals(HospitalQueryParams params) {
        LambdaQueryWrapper<HospitalConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(params.getHospitalName())) {
            queryWrapper.like(HospitalConfig::getHospitalName, params.getHospitalName());
        }
        if (StringUtils.hasLength(params.getCreatedStartAt())) {
            queryWrapper.gt(HospitalConfig::getCreatedAt,DateUtil.parse(params.getCreatedStartAt()));
        }
        if (StringUtils.hasLength(params.getCreatedEndAt())) {
            queryWrapper.lt(HospitalConfig::getCreatedAt,DateUtil.parse(params.getCreatedEndAt()));
        }
        IPage<HospitalConfig> page = this.hospitalConfigMapper.selectPage(params.builderPage(), queryWrapper);
        return page.getRecords();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveHospitalConfig(HospitalConfig config) {
        if (config.getId() != null) {
            if (this.isDuplicate(config.getId(), new QueryWrapper<HospitalConfig>()
                    .eq(HospitalConfig.TBL_HOSPITAL_NAME, config.getHospitalName()).
                            or().
                            eq(HospitalConfig.TBL_HOSPITAL_CODE, config.getHospitalCode()))) {
                throw new HospitalConfigDuplicateException();
            }
            config.setActive(true);
            this.updateById(config);
        } else {
            if (this.isDuplicate(null, new QueryWrapper<HospitalConfig>()
                    .eq(HospitalConfig.TBL_HOSPITAL_NAME, config.getHospitalName()).
                            or().
                            eq(HospitalConfig.TBL_HOSPITAL_CODE, config.getHospitalCode()))) {
                throw new HospitalConfigDuplicateException();
            }
            this.insert(config);
        }
    }

    public HospitalConfig getHosInfoById(Long id) {
        return this.hospitalConfigMapper.selectById(id);
    }

    public List<String> getHosNames(String name) {
        QueryWrapper<HospitalConfig> hospitalConfigQueryWrapper = new QueryWrapper<>();
        hospitalConfigQueryWrapper.like(HospitalConfig.TBL_HOSPITAL_NAME,name);
        hospitalConfigQueryWrapper.select(HospitalConfig.TBL_HOSPITAL_NAME,name);
        hospitalConfigQueryWrapper.last("limit 10");
        List<HospitalConfig> configs = this.hospitalConfigMapper.selectList(hospitalConfigQueryWrapper);
        List<String> results = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(configs)) {
            configs.forEach(e->{
                results.add(e.getHospitalName());
            });
        }
        return results;
    }
}
