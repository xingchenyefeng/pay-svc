package com.zhch.paysvc.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhch.paysvc.core.entity.Entity;
import com.zhch.paysvc.core.entity.EntityKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lumos
 */
public abstract class BaseService<T extends Entity> {

    @Autowired
    private BaseMapper<T> mapper;


    /**
     * 通用创建
     *
     * @param t
     */
    protected T insert(T t) {
        // 设置常用属性
        EntityKit.preCreateWithOverrideAll(t);
        mapper.insert(t);
        return t;
    }


    /**
     * 通用更新, 根据主键
     *
     * @param t
     */
    protected T updateById(T t) {
        // 判断主键是否存在
        EntityKit.preUpdate(t);
        mapper.updateById(t);
        return t;
    }

    /**
     * 判断指定条件数据是否已经存在, 防止插入重复数据
     *
     * @param id
     * @param queryWrapper
     * @return
     */
    protected boolean isDuplicate(Long id, QueryWrapper<T> queryWrapper) {
        List<T> found = mapper.selectList(queryWrapper);
        // 不存在
        if (CollectionUtils.isEmpty(found)) {
            return false;
        }
        // 根据传没传id判断是创建还是更新
        boolean isCreate = (id == null);
        // 根据查询条件找到的已存在的数据
        Long foundOneId = found.get(0).getId();
        if (isCreate) {
            return true;
        } else {//update
            if (id.equals(foundOneId)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

}
