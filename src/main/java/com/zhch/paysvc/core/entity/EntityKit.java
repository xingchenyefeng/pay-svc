package com.zhch.paysvc.core.entity;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zhch.paysvc.core.exception.NotAuthedException;
import com.zhch.paysvc.core.session.SessionContext;
import com.zhch.paysvc.core.session.UserSubject;

import java.util.Date;

/**
 * entity 工具类
 *
 * @author lumos
 */
public class EntityKit {

    private EntityKit() {
    }


    /**
     * 用于插入数据前,设置创建人等字段
     * 数据全部都覆盖
     * 有session时候用
     *
     * @param m
     * @return
     */
    public static <M extends Entity> void preCreateWithOverrideAll(M m) {
        //从session中获取当前用户的信息
        UserSubject userSubject = SessionContext.getCurrentUserSubject();
        if (userSubject == null) {
            throw new NotAuthedException();
        }
        Long userId = userSubject.getUserId();

        Date now = DateUtil.date();
        if (m.getId() == null || m.getId().equals(0L)) {
            m.setId(IdWorker.getId());
        }
        m.setCreator(userId);
        m.setCreatedAt(now);
        m.setUpdater(userId);
        m.setUpdatedAt(now);
    }


    /**
     * 用于更新数据前
     * 有session时候用
     *
     * @param m
     * @return
     */
    public static <M extends Entity> void preUpdate(M m) {
        //从session中获取当前用户的信息
        UserSubject userSubject = SessionContext.getCurrentUserSubject();
        if (userSubject == null) {
            throw new NotAuthedException();
        }
        Long userId = userSubject.getUserId();

        Date now = DateUtil.date();
        m.setUpdater(userId);
        m.setUpdatedAt(now);
    }
}
