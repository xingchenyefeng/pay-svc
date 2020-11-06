package com.zhch.paysvc.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author luoxiaoming
 * @since 2020-02-05
 */
@Getter
@Setter
@TableName("role_menu")
public class RoleMenu extends Entity {

    private static final long serialVersionUID = -8154274417598696060L;


    /**
     * 角色id
     */
    private Long roleId;


    /**
     * 菜单id
     */
    private Long menuId;




    public static final String TBL_ID = "id";
    public static final String TBL_ROLE_ID = "role_id";
    public static final String TBL_MENU_ID = "menu_id";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATOR = "updator";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
