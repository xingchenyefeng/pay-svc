package com.zhch.paysvc.service;


import com.zhch.paysvc.config.Handler;
import com.zhch.paysvc.entity.ChannelConfig;
import com.zhch.paysvc.support.*;
import com.zhch.paysvc.support.alipay.AlipaySupport;
import com.zhch.paysvc.support.alipay.AlipaySupportImpl;
import com.zhch.paysvc.support.wepay.WepaySupport;
import com.zhch.paysvc.support.wepay.WepaySupportImpl;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 * @author lumos
 * @date 2017/11/12
 */


@Component
@Log4j2
public class DefaultMessageProcessor implements MessageProcessor {


    @Autowired
    private ApplicationContext applicationContext;
    private ServicePool<String, MessageHandler> messageHandlerServicePool;
    private volatile PaySupportServicePool paySupportServicePool;
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Value("${handle.scan.package}")
    private String basePackage;
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    private MetadataReaderFactory metadataReaderFactory;



    @PostConstruct
    public void init() throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("init message processor......");
        }
        /*
         * 初始化messageHandlerServicePool对象
         */
        messageHandlerServicePool = new SimpleMessageHandlerServicePool();
        /*
         * 初始化paySupportServicePool对象
         */
        paySupportServicePool = new PaySupportServicePool();

        ChannelConfigService channelConfigService = applicationContext.getBean(ChannelConfigService.class);
        /*
         * 获取可用支付渠道配置数据列表
         */
        List<ChannelConfig> channelConfigs = channelConfigService.findAvailableChannelConfig();
        /*
         * 迭代每一个渠道配置
         */
        if (channelConfigs != null && channelConfigs.size() > 0) {
            for (ChannelConfig channelConfig : channelConfigs) {
                try {
                    releasePaySupport(channelConfig);
                } catch (Exception e) {
                    log.error(channelConfig.getChannelCode()+" 支付渠道注册异常："+e.getMessage()!=null?e.getMessage():"空指针异常");
                    e.printStackTrace();
                }
            }
        }

        /**
         * 开始初始化handler
         */
        this.metadataReaderFactory = new CachingMetadataReaderFactory();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
        Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                if (annotationMetadata.hasAnnotation(Handler.class.getName())) {
                    String className = metadataReader.getClassMetadata().getClassName();
                    val handlerClass = Class.forName(className);
                    MessageHandler handler = (MessageHandler) handlerClass.getConstructor().newInstance();
                    Handler annotation = handlerClass.getDeclaredAnnotation(Handler.class);
                    IPayAgent agent = applicationContext.getBean(IPayAgent.class);
                    agent.setPaySupportServicePool(this.paySupportServicePool);
                    handler.setPayAgent(agent);
                    handler.setFunctionCode(annotation.code());
                    handler.setHandlerName(annotation.name());
                    this.messageHandlerServicePool.release(annotation.code(), handler);
                }
            }
        }
    }

    @Override
    public void releasePaySupport(ChannelConfig channelConfig) throws Exception{
        Assert.isTrue(channelConfig!=null  ,"支付渠道配置不可用");
        String payType = channelConfig.getPayType();
        switch (payType) {
            case "1":
                WepaySupport wepaySupport = new WepaySupportImpl(channelConfig);
                paySupportServicePool.release(channelConfig.getChannelCode(), wepaySupport);
                break;
            case "2":
                AlipaySupport alipaySupport = new AlipaySupportImpl(channelConfig);
                paySupportServicePool.release(channelConfig.getChannelCode(), alipaySupport);
                break;
            default:
                if (log.isInfoEnabled()) {
                    log.error(channelConfig.getChannelCode()+" 支付渠道类别还未实现.");
                }
                break;
        }
    }

    @Override
    public void destroyPaySupport(ChannelConfig channelConfig) throws Exception{
        Assert.isTrue(channelConfig!=null && channelConfig.getActive() ,"支付渠道配置不可用");
        paySupportServicePool.remove(channelConfig.getChannelCode());
    }



    @Override
    public ResponseMessage handler(RequestMessage requestMessage) throws Exception {
        ResponseMessage responseMessage = null;
        MessageHandler handler = null;
        String funcode = requestMessage.getFuncode();
        Assert.hasLength(funcode, "funcode 参数为空.");
        handler = messageHandlerServicePool.acquire(funcode);
        if (handler == null) {
            throw new RuntimeException(funcode + "所对应的handler没注册.");
        }
        responseMessage = handler.handler(requestMessage);
        return responseMessage;
    }


    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }
}
