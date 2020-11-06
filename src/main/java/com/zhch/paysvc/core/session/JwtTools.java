package com.zhch.paysvc.core.session;

import com.zhch.paysvc.core.config.ChannelType;
import com.zhch.paysvc.core.config.ClientType;
import com.zhch.paysvc.core.exception.NotAuthedException;
import com.zhch.paysvc.core.exception.RequestExtractParamsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.Assert;

import java.security.Key;

import static com.zhch.paysvc.core.config.Constants.*;


/**
 * @author lumos
 */
public class JwtTools {

    private static String base64Keys = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";

    static final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Keys));


    public static JwtData trans2JwtData(UserSubject userSubject) {
        JwtData jwtData = new JwtData();
        jwtData.setUserId(String.valueOf(userSubject.getUserId()));
        jwtData.setChannelType(userSubject.getChannelType().getValue());
        jwtData.setClientType(userSubject.getClientType().getValue());
        jwtData.setLoginTime(userSubject.getLoginDateTime());
        return jwtData;
    }

    public static String generatorJwt(UserSubject subject) {
        Assert.notNull(subject, "认证信息已失效,请重新登录");
        Long userId = subject.getUserId();
        if (userId == null) {
            throw new NotAuthedException();
        }
        ClientType clientType = subject.getClientType();
        ChannelType channelType = subject.getChannelType();
        if (clientType == null || channelType == null) {
            throw new RequestExtractParamsException();
        }
        return Jwts.builder().setSubject(userId.toString()).
                claim(CLIENT_TYPE, clientType.getValue()).
                claim(CHANNEL_TYPE, channelType.getValue()).
                claim(LOGIN_DATE_TIME, subject.getLoginDateTime())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public static JwtData getJwtData(String jwt) {
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String userId = body.getSubject();
        Long loginDateTime = body.get(LOGIN_DATE_TIME, Long.class);
        String clientType = body.get(CLIENT_TYPE, String.class);
        String channelType = body.get(CHANNEL_TYPE, String.class);
        return new JwtData(userId, clientType, channelType, loginDateTime);
    }
}
