package com.zhch.paysvc.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhch.paysvc.core.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
@TableName("channel_config")
public class ChannelConfig extends Entity {

    private static final long serialVersionUID = 8761087778166901947L;


    /**
     * 渠道编号
     */
    private String channelCode;


    /**
     * 医院编码
     */
    private String hospitalCode;


    /**
     * 应用id
     */
    private String appId;


    /**
     * 支付类型 1:微信，2：支付宝，3：招行一网通，4：银联闪付
     */
    private String payType;


    /**
     * 应用秘钥
     */
    private String appKey;


    /**
     * 商户id
     */
    private String sellerId;


    /**
     * 私钥
     */
    private String ownerPrivateKey;


    /**
     * 阿里公钥
     */
    private String alipayPublicKey;


    /**
     * 公钥
     */
    private String ownerPublicKey;


    /**
     * 回调地址
     */
    private String notifyUrl;


    /**
     * 允许支付的方式
     */
    private String enablePayChannels;


    /**
     * 返佣账号
     */
    private String sysServiceProviderId;

    /**
     * 证书对应的记录id
     */
    private Long certId;
    /**
     * 证书
     */
    @TableField(exist = false)
    @JSONField(serialize = false,deserialize = false)
    private byte[] cert;


    /**
     * 证书到期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date certExpire;


    /**
     * 通道状态
     */
    private Boolean active;


    /**
     * 本次启动时间
     */
    private Date activeAt;


    /**
     * 最后一次修改者
     */
    private Long updater;




    public static final String TBL_ID = "id";
    public static final String TBL_CHANNEL_CODE = "channel_code";
    public static final String TBL_HOSPITAL_CODE = "hospital_code";
    public static final String TBL_APP_ID = "app_id";
    public static final String TBL_PAY_TYPE = "pay_type";
    public static final String TBL_APP_KEY = "app_key";
    public static final String TBL_SELLER_ID = "seller_id";
    public static final String TBL_OWNER_PRIVATE_KEY = "owner_private_key";
    public static final String TBL_ALIPAY_PUBLIC_KEY = "alipay_public_key";
    public static final String TBL_OWNER_PUBLIC_KEY = "owner_public_key";
    public static final String TBL_NOTIFY_URL = "notify_url";
    public static final String TBL_ENABLE_PAY_CHANNELS = "enable_pay_channels";
    public static final String TBL_SYS_SERVICE_PROVIDER_ID = "sys_service_provider_id";
    public static final String TBL_CERT_EXPIRE = "cert_expire";
    public static final String TBL_ACTIVE = "active";
    public static final String TBL_ACTIVE_AT = "active_at";
    public static final String TBL_CERT_ID = "cert_id";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_at";

    ///////////////////////////////////////////////////////
    //////////////  以下为不在表中的字段  ////////////////////
    ///////////////////////////////////////////////////////
}
