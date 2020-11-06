package com.zhch.paysvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhch.paysvc.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lumos
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from role where id in (select role_id from user_role where user_id=#{userId})")
    List<Role> getRolesByUserId(@Param("userId") Long userId);

}
