package com.zhch.paysvc.core.web;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zhch.paysvc.core.config.ChannelType;
import com.zhch.paysvc.core.config.ClientType;
import com.zhch.paysvc.core.config.Constants;
import com.zhch.paysvc.core.exception.NotAuthedException;
import com.zhch.paysvc.core.exception.RequestExtractParamsException;
import com.zhch.paysvc.core.session.*;
import com.zhch.paysvc.core.utils.UrlTools;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static org.springframework.util.StringUtils.hasLength;

/**
 * @author luoxiaoming
 * @since 2020/8/3 上午8:58
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@ConditionalOnProperty(name = "server.enable-mvc", havingValue = "true")
@Log4j2
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${server.ignore-paths}")
    private Set<String> urlWhitelist;

    @Autowired
    SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        // 先删除
        MDC.clear();
        MDC.put(Constants.SERIAL, IdWorker.getIdStr());

        String clientType = request.getHeader(Constants.CLIENT_TYPE);
        String channelType = request.getHeader(Constants.CHANNEL_TYPE);
        //判断url是否需要验证
        String path = request.getRequestURI();
        if (!UrlTools.checkWhiteList(urlWhitelist, path)) {
            if (!hasLength(clientType) || !hasLength(channelType)) {
                throw new RequestExtractParamsException();
            }
            String token = request.getHeader(Constants.TOKEN);
            log.info("visit path {} with token {}", path, token);
            if (!hasLength(token)) {
                throw new RequestExtractParamsException();
            }
            UserSubject subject = sessionService.getSession(token, true);
            if (subject == null) {
                throw new NotAuthedException();
            }
            subject.setChannelType(ChannelType.getChannelType(channelType));
            subject.setClientType(ClientType.getClientType(clientType));
            sessionService.checkPermission(subject);
            SessionContext.setUserSubject(subject);
        } else {
            // 无需验证可以mock一个代表未登录用户的对象
            AnonymousUserSubject currentAnonymous = new AnonymousUserSubject();
            if (clientType == null) {
                currentAnonymous.setClientType(ClientType.ANONYMOUS);
            } else {
                currentAnonymous.setClientType(ClientType.getClientType(clientType));
                currentAnonymous.setChannelType(ChannelType.getChannelType(channelType));
            }
            SessionContext.setUserSubject(currentAnonymous);
        }
        return true;
    }
}
