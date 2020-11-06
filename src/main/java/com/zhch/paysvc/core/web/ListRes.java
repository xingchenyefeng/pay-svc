package com.zhch.paysvc.core.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lumos
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class ListRes<T> extends BaseRes {

    private static final long serialVersionUID = -4407059140162290037L;
    private List<T> data;

    public ListRes<T> data(List<T> data) {
        this.data = data;
        return this;
    }

}
