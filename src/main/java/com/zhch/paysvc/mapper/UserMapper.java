package com.zhch.paysvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhch.paysvc.dto.PrivilegeDto;
import com.zhch.paysvc.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lumos
 * @date 2020/10/11 00
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select a.type,a.path from menu a\n" +
            "        inner join role_menu rm on a.id = rm.menu_id\n" +
            "        inner join user_role ur on rm.role_id = ur.role_id\n" +
            "        where ur.user_id = #{userId}")
    List<PrivilegeDto> getPrivileges(@Param("userId") Long userId);

}
