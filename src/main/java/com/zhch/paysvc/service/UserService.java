package com.zhch.paysvc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhch.paysvc.core.service.BaseService;
import com.zhch.paysvc.dto.PrivilegeDto;
import com.zhch.paysvc.entity.Role;
import com.zhch.paysvc.entity.User;
import com.zhch.paysvc.mapper.RoleMapper;
import com.zhch.paysvc.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lumos
 * @date 2020/10/11 04
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.TBL_USERNAME, username);
        return this.userMapper.selectOne(queryWrapper);
    }

    public List<PrivilegeDto> getPrivileges(Long userId) {
        return this.userMapper.getPrivileges(userId);
    }

    public List<Role> getRolesByUserId(Long userId) {
        return roleMapper.getRolesByUserId(userId);
    }

    public User getByUserId(Long userId) {
        return this.userMapper.selectById(userId);
    }
}
