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
 * @author lumos
 * @since 2020-10-12
 */
@Getter
@Setter
@TableName("hospital_config")
public class HospitalConfig extends Entity {

    private static final long serialVersionUID = -912820848847582806L;


    /**
     * 医院编码
     */
    private String hospitalCode;


    /**
     * 医院名称
     */
    private String hospitalName;


    /**
     * 联系人姓名
     */
    private String contactName;


    /**
     * 联系电话
     */
    private String contactPhone;


    /**
     * 状态 1可用，0 不可用
     */
    private Boolean active;



    public static final String TBL_ID = "id";
    public static final String TBL_HOSPITAL_CODE = "hospital_code";
    public static final String TBL_HOSPITAL_NAME = "hospital_name";
    public static final String TBL_CONTACT_NAME = "contact_name";
    public static final String TBL_CONTACT_PHONE = "contact_phone";
    public static final String TBL_ACTIVE = "active";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
