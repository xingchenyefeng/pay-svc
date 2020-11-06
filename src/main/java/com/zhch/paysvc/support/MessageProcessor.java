package com.zhch.paysvc.support;


import com.zhch.paysvc.entity.ChannelConfig;

/**
 *
 * @author lumos
 * @date 2017/9/12
 */
public interface MessageProcessor {

    void releasePaySupport(ChannelConfig channelConfig) throws Exception;

    void destroyPaySupport(ChannelConfig channelConfig) throws Exception;

    ResponseMessage handler(RequestMessage requestMessage) throws Exception;

}
