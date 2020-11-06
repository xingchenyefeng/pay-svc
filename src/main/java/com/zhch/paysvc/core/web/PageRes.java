package com.zhch.paysvc.core.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lumos
 */
@Setter
@Getter
@NoArgsConstructor
public class PageRes<T> extends BaseRes {

    private static final long serialVersionUID = -8372486773198602016L;
    private List<T> data;
    /**
     * 总行数
     */
    private long columns;
    /**
     * 总页码数
     */
    private long total;

    public PageRes<T> data(List<T> data) {
        this.data = data;
        return this;
    }

    public PageRes<T> columns(long columns, long pagesize) {
        this.columns = columns;
        if (pagesize == 0) {
            this.total = 0;
        } else {
            this.total = columns / pagesize + (columns % pagesize == 0 ? 0 : 1);
        }
        return this;
    }

    public PageRes<T> total(int total) {
        this.total = total;
        return this;
    }

    public PageRes(Long columns, Integer pagesize, List<T> data) {
        this.columns = columns;
        if (pagesize == 0) {
            this.total = 0;
        } else {
            this.total = columns / pagesize + (columns % pagesize == 0 ? 0 : 1);
        }
        this.data = data;
    }

}
