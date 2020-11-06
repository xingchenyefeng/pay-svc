package com.zhch.paysvc.controller;

import com.zhch.paysvc.core.web.ListRes;
import com.zhch.paysvc.core.web.ObjRes;
import com.zhch.paysvc.core.web.PageRes;
import com.zhch.paysvc.dto.HospitalQueryParams;
import com.zhch.paysvc.entity.HospitalConfig;
import com.zhch.paysvc.service.HospitalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lumos
 * @date 2020/10/12 01
 */
@RestController
@RequestMapping("/hospital")
public class HospitalConfigController {

    @Autowired
    HospitalConfigService hospitalConfigService;

    @GetMapping("/getHospitals")
    public PageRes<HospitalConfig> getHospitals(HospitalQueryParams params) {
        List<HospitalConfig> hospitalConfigs = this.hospitalConfigService.getHospitals(params);
        return new PageRes<>(params.getPage().getTotal(),params.getPagesize(),hospitalConfigs);
    }

    @GetMapping("/getHosNames")
    public ListRes<String> getHospitals(String name) {
        List<String> hosNames = this.hospitalConfigService.getHosNames(name);
        return new ListRes<String>().data(hosNames);
    }

    @PostMapping("/saveHospitalConfig")
    public ObjRes<HospitalConfig> saveHospitalConfig(@RequestBody HospitalConfig config) {
        this.hospitalConfigService.saveHospitalConfig(config);
        return new ObjRes<HospitalConfig>().data(config);
    }

    @GetMapping("/getHosInfoById")
    public ObjRes<HospitalConfig> getHosInfoById(Long id) {
        HospitalConfig config  = this.hospitalConfigService.getHosInfoById(id);
        return new ObjRes<HospitalConfig>().data(config);
    }

}
