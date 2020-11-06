package com.zhch.paysvc.entity;


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
public class Role extends Entity {

    private static final long serialVersionUID = -7925474143060778545L;


    /**
     * 菜单名称
     */
    private String name;


    /**
     * 备注
     */
    private String remark;


    /**
     * 数据权限
     */
    private String dataScope;


    /**
     * 角色级别
     */
    private Integer level;


    /**
     * 权限
     */
    private String permission;


    /**
     * 是否可用
     */
    private Integer enabled;

    /**
     * 默认标记
     */
    private Integer defaultTag;




    public static final String TBL_ID = "id";
    public static final String TBL_NAME = "name";
    public static final String TBL_REMARK = "remark";
    public static final String TBL_DATA_SCOPE = "data_scope";
    public static final String TBL_LEVEL = "level";
    public static final String TBL_PERMISSION = "permission";
    public static final String TBL_ENABLED = "enabled";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";
    public static final String TBL_DEFAULT_TAG = "default_tag";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
