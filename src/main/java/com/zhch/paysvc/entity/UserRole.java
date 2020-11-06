package com.zhch.paysvc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Data;

/**
 * @author luoxiaoming
 */
@Data
@TableName("user_role")
public class UserRole extends Entity {


    private static final long serialVersionUID = -3786817362724993366L;
    /**
     * 角色id
     */
    private Long roleId;


    /**
     * 用户id
     */
    private Long userId;


}
