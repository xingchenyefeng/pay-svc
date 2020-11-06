package com.zhch.paysvc.core.web;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.zhch.paysvc.core.config.Constants.*;

/**
 * @author lumos
 */
@Data
@NoArgsConstructor
public class BaseRes implements Serializable {
    private static final long serialVersionUID = -1817549328124764765L;
    private String code = SUCCESS;
    private String message = OK;

    public BaseRes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isSuccess() {
        return SUCCESS.equals(code);
    }

    public BaseRes code(String code) {
        this.code = code;
        return this;
    }

    public BaseRes message(String message) {
        this.message = message;
        return this;
    }
}
