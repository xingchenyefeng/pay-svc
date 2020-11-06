package com.zhch.paysvc.core.session;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhch.paysvc.core.config.Constants;
import com.zhch.paysvc.core.exception.AuthFailedException;
import com.zhch.paysvc.core.exception.NoPermissionException;
import com.zhch.paysvc.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author lumos
 */
@Slf4j
@Service
@ConditionalOnClass(StringRedisTemplate.class)
public class SessionService {

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private ListOperations<String, String> listOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisService redisService;

    @Value("${spring.profiles.active:pro}")
    private String activeProfile;

    /**
     * 限制有效token数量
     */
    private static final int LOGIN_ACCOUNT_LIMIT_SIZE = 10;


    public String createSession(UserSubject userSubject) {
        String token = RandomUtil.randomString(64);
        String loginKey = String.format(Constants.LOGIN_TOKEN_TEMPLATE, token);
        String accountKey = String.format(Constants.LOGIN_ACCOUNT_TEMPLATE, userSubject.getUserId());
        List<Consumer<StringRedisConnection>> cs = new ArrayList<>();
        final Expiration expiration;
        if (Constants.PRO_PROFILES.equals(activeProfile)) {
            expiration = Expiration.seconds(userSubject.getChannelType().getExpireSeconds());
        } else {
            expiration = Expiration.seconds(24 * 60 * 60);
        }
        cs.add(c -> c.lPush(accountKey, token));
        cs.add(c -> c.lTrim(accountKey, 0, LOGIN_ACCOUNT_LIMIT_SIZE - 1));
        if (expiration.getExpirationTimeInSeconds() > 0) {
            cs.add(c -> c.expire(accountKey, expiration.getExpirationTimeInSeconds()));
            cs.add(c -> c.set(loginKey, JSONObject.toJSONString(userSubject), expiration, RedisStringCommands.SetOption.SET_IF_ABSENT));
        }else {
            cs.add(c -> c.set(loginKey, JSONObject.toJSONString(userSubject)));
        }
        redisService.batchOps(cs);
        return token;
    }

    public UserSubject getSession(String token, boolean... refreshToken) {
        UserSubject userSubject = null;
        String loginKey = String.format(Constants.LOGIN_TOKEN_TEMPLATE, token);
        String content;
        if (stringRedisTemplate.hasKey(loginKey) && StringUtils.hasLength(content = valueOperations.get(loginKey))) {
            userSubject = JSONObject.parseObject(content, UserSubject.class);
            if ((refreshToken != null) && (refreshToken.length > 0) && refreshToken[0]) {
                String accountKey = String.format(Constants.LOGIN_ACCOUNT_TEMPLATE, userSubject.getUserId());
                List<Consumer<StringRedisConnection>> cs = new ArrayList<>();
                final Expiration expiration;
                if (Constants.PRO_PROFILES.equals(activeProfile)) {
                    expiration = Expiration.seconds(userSubject.getChannelType().getExpireSeconds());
                } else {
                    expiration = Expiration.seconds(24 * 60 * 60);
                }
                cs.add(c -> c.expire(loginKey, expiration.getExpirationTimeInSeconds()));
                cs.add(c -> c.expire(accountKey, expiration.getExpirationTimeInSeconds()));
                redisService.batchOps(cs);
            }
        }
        return userSubject;
    }

    public List<UserSubject> getToken(Long userId) {
        List<UserSubject> subjects = new ArrayList<>();
        String accountKey = String.format(Constants.LOGIN_ACCOUNT_TEMPLATE, userId);
        List<String> tokenList = listOperations.range(accountKey, 0, -1);
        if (!CollectionUtils.isEmpty(tokenList)) {
            for (String e : tokenList) {
                UserSubject userSubject = getSession(e);
                if (userSubject != null) {
                    subjects.add(getSession(e));
                }
            }
        }
        return subjects;
    }

    public void clearSession(String token) {
        String loginKey = String.format(Constants.LOGIN_TOKEN_TEMPLATE, token);
        stringRedisTemplate.delete(loginKey);
        UserSubject userSubject = getSession(token);
        if (userSubject != null) {
            String accountKey = String.format(Constants.LOGIN_ACCOUNT_TEMPLATE, userSubject.getUserId());
            listOperations.remove(accountKey, 0, token);
        }
    }

    public void clearSession(Long userId) {
        String accountKey = String.format(Constants.LOGIN_ACCOUNT_TEMPLATE, userId);
        List<String> tokenList = listOperations.range(accountKey, 0, -1);
        if (!CollectionUtils.isEmpty(tokenList)) {
            for (String e : tokenList) {
                String loginKey = String.format(Constants.LOGIN_TOKEN_TEMPLATE, e);
                stringRedisTemplate.delete(loginKey);
            }
            stringRedisTemplate.delete(accountKey);
        }
    }

    public void refreshSession(String token) {
        String loginKey = String.format(Constants.LOGIN_TOKEN_TEMPLATE, token);
        UserSubject userSubject = getSession(token);
        valueOperations.set(loginKey, JSONObject.toJSONString(userSubject), userSubject.getChannelType().getExpireSeconds(), TimeUnit.SECONDS);
    }

    public void checkPermission(UserSubject subject) {
        if (subject == null) {
            throw new AuthFailedException();
        }
        boolean flag = true;
        if (!flag) {
            throw new NoPermissionException();
        }
    }
}
