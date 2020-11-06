package com.zhch.paysvc.core.config;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zhch.paysvc.core.web.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @author lumos
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(DisableCircularReferenceDetect,
                WriteNullListAsEmpty,
                WriteMapNullValue,
                WriteNullStringAsEmpty,
                WriteNullNumberAsZero,
                WriteNullBooleanAsFalse);
        config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        config.setSerializeFilters((ValueFilter) (object, name, value) -> {
            // 针对包装类型value为null时输出""
            if (Objects.isNull(value)) {
                return Constants.EMPTY_STRING;
            }
            // 数字类型转字符串
            if (value instanceof Number) {
                return String.valueOf(value);
            }
            return value;
        });
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypes);
        converter.setFastJsonConfig(config);
        return converter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthInterceptor authInterceptor = applicationContext.getBean(AuthInterceptor.class);
        InterceptorRegistration addInterceptor = registry.addInterceptor(authInterceptor);
        addInterceptor.addPathPatterns("/**").excludePathPatterns("/doc.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
