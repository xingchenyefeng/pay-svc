package com.zhch.paysvc.entity;


import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lumos
 * @since 2020-10-11
 */
@Getter
@Setter
public class User extends Entity {

    private static final long serialVersionUID = 3321187038005159823L;


    /**
     * 用户名
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 真实姓名
     */
    private String realName;


    /**
     * 所在医院/单位
     */
    private String hospitalCode;


    /**
     * 状态 1: 有效,0: 无效
     */
    private String status;


    /**
     * 头像
     */
    private String avatar;

    public static final String TBL_ID = "id";
    public static final String TBL_USERNAME = "username";
    public static final String TBL_PASSWORD = "password";
    public static final String TBL_REAL_NAME = "real_name";
    public static final String TBL_AVATAR = "avatar";
    public static final String TBL_HOSPITAL_CODE = "hospital_code";
    public static final String TBL_STATUS = "status";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
