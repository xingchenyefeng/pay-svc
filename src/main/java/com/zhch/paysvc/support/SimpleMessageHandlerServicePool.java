package com.zhch.paysvc.support;


import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lumos
 */
@Log4j2
public class SimpleMessageHandlerServicePool implements ServicePool<String, MessageHandler> {



    protected final ConcurrentMap<String, MessageHandler> pool = new ConcurrentHashMap<>();


    @Override
    public MessageHandler addAndAcquire(String functionCode, MessageHandler messageHandler) {
        MessageHandler handler = pool.get(functionCode);
        if (handler != null) {
            throw new RuntimeException(functionCode + " messageHandler已经存在.");
        }
        pool.put(functionCode, messageHandler);
        if (log.isInfoEnabled()){
            log.info(functionCode +" messageHandler已注册..");
        }
        return messageHandler;
    }

    @Override
    public MessageHandler acquire(String functionCode) {
        return pool.get(functionCode);
    }

    @Override
    public void release(String functionCode, MessageHandler messageHandler) {
        pool.put(functionCode, messageHandler);
        if (log.isInfoEnabled()){
            log.info(functionCode +" messageHandler已注册..");
        }
    }

    @Override
    public boolean remove(String functionCode) {
        MessageHandler remove = pool.remove(functionCode);
        if (log.isInfoEnabled()){
            log.info(functionCode +" messageHandler已注销..");
        }
        return remove != null;
    }

    @Override
    public boolean isExist(String functionCode) {
        return pool.containsKey(functionCode);
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
    public List<MessageHandler> getServices() {
        List<MessageHandler> services = new ArrayList<>();
        for (Map.Entry<String, MessageHandler> entry : this.pool.entrySet()) {
            services.add(entry.getValue());
        }
        return services;
    }

    @Override
    public <T> T getPaySupport(String functionCode, Class<T> type) {
        return type.cast(this.pool.get(functionCode));
    }


    @Override
    public MessageHandler getPaySupport(String channelCode) {
        return this.pool.get(channelCode);
    }
}