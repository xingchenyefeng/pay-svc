package com.zhch.paysvc.controller;

import com.zhch.paysvc.core.config.ChannelType;
import com.zhch.paysvc.core.config.ClientType;
import com.zhch.paysvc.core.exception.UserNotFoundException;
import com.zhch.paysvc.core.session.SessionContext;
import com.zhch.paysvc.core.session.SessionService;
import com.zhch.paysvc.core.session.UserSubject;
import com.zhch.paysvc.core.web.ObjRes;
import com.zhch.paysvc.dto.PrivilegeDto;
import com.zhch.paysvc.entity.Role;
import com.zhch.paysvc.entity.User;
import com.zhch.paysvc.exception.NotPermissionException;
import com.zhch.paysvc.service.UserService;
import com.zhch.paysvc.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhch.paysvc.core.config.Constants.*;

/**
 * @author lumos
 * @date 2020/10/11 04
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @PostMapping("/login")
    public ObjRes login(@RequestParam("username") String username, @RequestParam("password") String password) {
        ObjRes res = new ObjRes();
        User user = this.userService.getUserByUsername(username);
        if (user != null) {
            if ( !user.getPassword().equals(MD5Util.string2MD5(password))) {
                throw new NotPermissionException();
            };
            Map<String, Object> dataMap = new HashMap<>();
            List<PrivilegeDto> privilegeDtoList = this.userService.getPrivileges(user.getId());
            List<String> privilegePaths = new ArrayList<>();
            privilegeDtoList.forEach(r -> {
                if (r.getType() == 0 || r.getType() == 1) {
                    privilegePaths.add(r.getPath());
                }
            });
            List<Role> roles = this.userService.getRolesByUserId(user.getId());
            dataMap.put("roles", roles);
            dataMap.put("privilegePaths", privilegePaths);
            UserSubject userSubject = new UserSubject();
            userSubject.setUserId(user.getId());
            userSubject.setName(user.getRealName());
            userSubject.setLoginDateTime(System.currentTimeMillis());
            userSubject.setAvatar(user.getAvatar());
            userSubject.setClientType(ClientType.MANAGER);
            userSubject.setChannelType(ChannelType.WEB);
            String token = sessionService.createSession(userSubject);
            dataMap.put("token", token);
            res.data(dataMap);
        } else {
            throw new UserNotFoundException();
        }
        return res;
    }

    @GetMapping("/getInfo")
    public ObjRes getInfo() {
        UserSubject subject = SessionContext.getCurrentUserSubject();
        List<PrivilegeDto> privilegeDtoList = this.userService.getPrivileges(subject.getUserId());
        User manager = this.userService.getByUserId(subject.getUserId());
        List<String> privilegePaths = new ArrayList<>();
        privilegeDtoList.forEach(r -> {
            if (r.getType() == 0 || r.getType() == 1) {
                privilegePaths.add(r.getPath());
            }
        });
        List<Role> roles = this.userService.getRolesByUserId(subject.getUserId());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("roles", roles);
        dataMap.put("user",manager);
        dataMap.put("privilegePaths", privilegePaths);
        return new ObjRes().data(dataMap);
    }

    @PostMapping("/logout")
    public ObjRes logout() {
        UserSubject subject = SessionContext.getCurrentUserSubject();
        sessionService.clearSession(subject.getUserId());
        return new ObjRes();
    }

}
