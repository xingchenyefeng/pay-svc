package com.zhch.paysvc.core.web.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lumos
 * @since 2020/7/18 下午4:09
 */
@Setter
@Getter
public class PageQueryParams implements Serializable {

    private static final long serialVersionUID = -7040287795853274914L;
    private Integer pagesize = 20;

    @JSONField(deserialize = true)
    private Integer current = 1;

    private Page page;

    public <T> Page<T> builderPage() {
        this.page = new Page<T>(getCurrent(), getPagesize());
        return page;
    }
}
