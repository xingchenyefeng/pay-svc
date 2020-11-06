package com.zhch.paysvc.core.web;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lumos
 */
@Data
@NoArgsConstructor
public class ObjRes<T> extends BaseRes {

    private static final long serialVersionUID = 5391100732674149180L;
    private T data;

    public ObjRes<T> data(T data) {
        this.data = data;
        return this;
    }

}
