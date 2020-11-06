package com.zhch.paysvc.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体抽象类
 *
 * @author lumos
 */
@Getter
@Setter
public class Entity implements Serializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Entity.class);
    private static final long serialVersionUID = -6650345307926231768L;

    @TableId
    protected Long id;

    /**
     * 创建人账号id
     */
    protected Long creator;

    /**
     * 创建时间
     */
    protected Date createdAt;

    /**
     * 更新人账号id
     */
    protected Long updater;

    /**
     * 更新时间
     */
    protected Date updatedAt;


    public static final String TBL_ID = "id";
    public static final String TBL_CREATOR = "creator";
    public static final String TBL_CREATED_AT = "created_at";
    public static final String TBL_UPDATER = "updater";
    public static final String TBL_UPDATED_AT = "updated_At";
}
