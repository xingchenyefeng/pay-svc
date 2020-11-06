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
 * @since 2020-10-15
 */
@Getter
@Setter
@TableName("channel_cert")
public class ChannelCert extends Entity {

    private static final long serialVersionUID = 3865185485677702591L;


    /**
     * 证书
     */
    private byte[] cert;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 最后一次修改者
     */
    private Long updater;




    public static final String TBL_ID = "id";
    public static final String TBL_CERT = "cert";
    public static final String TBL_FILE_NAME ="file_name";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
