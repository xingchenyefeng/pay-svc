package com.zhch.paysvc.core.session;

import com.zhch.paysvc.core.config.ChannelType;
import com.zhch.paysvc.core.config.ClientType;
import com.zhch.paysvc.core.exception.RequestExtractParamsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lumos
 */
@Component
@Slf4j
public class SessionContext {

    private static final ThreadLocal<UserSubject> USER_SUBJECT_THREAD_LOCAL = new ThreadLocal<>();

    public static UserSubject getCurrentUserSubject() {
        return USER_SUBJECT_THREAD_LOCAL.get();
    }

    public static void setUserSubject(UserSubject userSubject) {
        SessionContext.USER_SUBJECT_THREAD_LOCAL.set(userSubject);
    }

    public static void clearSession() {
        USER_SUBJECT_THREAD_LOCAL.remove();
    }

    public static void setUserSubject(String channelType, String clientType, Long userId) {
        ClientType clientTypeNum = ClientType.getClientType(clientType);
        ChannelType channelTypeNum = ChannelType.getChannelType(channelType);
        if (channelTypeNum == null || clientTypeNum == null || userId == null) {
            throw new RequestExtractParamsException();
        }
        UserSubject userSubject = new UserSubject();
        userSubject.setChannelType(channelTypeNum);
        userSubject.setClientType(clientTypeNum);
        userSubject.setUserId(userId);
        USER_SUBJECT_THREAD_LOCAL.set(userSubject);
    }
}
