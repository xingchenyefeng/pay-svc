package com.zhch.paysvc.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
public class Menu extends Entity {

    private static final long serialVersionUID = 7086298820925866503L;


    /**
     * 菜单名称
     */
    private String name;


    /**
     * 组件
     */
    private String component;


    /**
     * 上级菜单ID
     */
    private Long pid;


    /**
     * 排序
     */
    private Integer sort;


    /**
     * 图标
     */
    private String icon;


    /**
     * 链接路径
     */
    private String path;


    /**
     * 是否开启缓存
     */
    private Integer cache;


    /**
     * 是否隐藏
     */
    private Integer hidden;


    /**
     * 组件名称
     */
    private String componentName;


    /**
     * 权限
     */
    private String permission;


    /**
     * 类型
     */
    private Integer type;


    /**
     * 是否可用
     */
    private Integer enabled;

    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private String label;

    public String getLabel() {
        return this.name;
    }

    public static final String TBL_ID = "id";
    public static final String TBL_NAME = "name";
    public static final String TBL_COMPONENT = "component";
    public static final String TBL_PID = "pid";
    public static final String TBL_SORT = "sort";
    public static final String TBL_ICON = "icon";
    public static final String TBL_PATH = "path";
    public static final String TBL_CACHE = "cache";
    public static final String TBL_HIDDEN = "hidden";
    public static final String TBL_COMPONENT_NAME = "component_name";
    public static final String TBL_PERMISSION = "permission";
    public static final String TBL_TYPE = "type";
    public static final String TBL_ENABLED = "enabled";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATOR = "updator";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
