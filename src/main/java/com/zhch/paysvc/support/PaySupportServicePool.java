package com.zhch.paysvc.support;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
public class PaySupportServicePool implements ServicePool<String, PaySupport> {


    protected final ConcurrentMap<String, PaySupport> pool = new ConcurrentHashMap<>();


    @Override
    public PaySupport addAndAcquire(String channelCode, PaySupport PaySupport) {
        PaySupport handler = pool.get(channelCode);
        if (handler != null) {
            throw new RuntimeException(channelCode + " PaySupport已经存在.");
        }
        pool.put(channelCode, PaySupport);
        if (log.isInfoEnabled()){
            log.info(channelCode +" 支付渠道已注册..");
        }
        return PaySupport;
    }

    @Override
    public PaySupport acquire(String channelCode) {
        return pool.get(channelCode);
    }

    @Override
    public void release(String channelCode, PaySupport PaySupport) {
        pool.put(channelCode, PaySupport);
        if (log.isInfoEnabled()){
            log.info(channelCode +" 支付渠道已注册..");
        }
    }

    @Override
    public boolean remove(String channelCode) {
        PaySupport remove = pool.remove(channelCode);
        if (log.isInfoEnabled()){
            log.info(channelCode +" 支付渠道已注销..");
        }
        return remove != null;
    }

    @Override
    public boolean isExist(String channelCode) {
        return pool.containsKey(channelCode);
    }

    @Override
    public int size() {
        return this.pool.size();
    }

    @Override
    public void purge() {
        this.pool.clear();
    }

    @Override
    public List<PaySupport> getServices() {
        List<PaySupport> services = new ArrayList<>();
        for (Map.Entry<String, PaySupport> entry : this.pool.entrySet()) {
            services.add(entry.getValue());
        }
        return services;
    }

    @Override
    public <T> T getPaySupport(String channelCode,Class<T> type){
        return type.cast(this.pool.get(channelCode));
    }

    @Override
    public PaySupport getPaySupport(String channelCode){
        PaySupport paySupport =this.pool.get(channelCode);
        Assert.notNull(paySupport,channelCode+"支付渠道未注册");
        return paySupport;
    }
}