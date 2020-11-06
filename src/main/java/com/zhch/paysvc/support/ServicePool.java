/*
 *
 *  *****************************************************************************
 *  * Copyright ( c ) 2016 Heren Tianjin Inc. All Rights Reserved.
 *  *
 *  * This software is the confidential and proprietary information of Heren Tianjin Inc
 *  * ("Confidential Information").  You shall not disclose such Confidential Information
 *  *  and shall use it only in accordance with the terms of the license agreement
 *  *  you entered into with Heren Tianjin or a Heren Tianjin authorized
 *  *  reseller (the "License Agreement").
 *  ****************************************************************************
 *  *
 */

package com.zhch.paysvc.support;


import java.util.List;

/**
 *
 * @author lumos
 * @date 16-8-1
 */
public interface ServicePool<Key, Service> {

    /**
     * Adds the given service to the pool and acquires it.
     *
     * @param key     the key
     * @param service the service
     * @return the acquired service, is newer <tt>null</tt>
     * @throws IllegalStateException if the queue is full (capacity has been reached)
     */
    Service addAndAcquire(Key key, Service service);

    /**
     * Tries to acquire the service with the given key
     *
     * @param key the key
     * @return the acquired service, or <tt>null</tt> if no free in pool
     */
    Service acquire(Key key);

    /**
     * Releases the service back to the pool
     *
     * @param key     the key
     * @param service the service
     */
    void release(Key key, Service service);

    /**
     * remove this value which is abandon use the key
     *
     * @param key the key
     * @return
     */
    boolean remove(Key key);

    /**
     * query the pool to determine is exist this key
     *
     * @param key key
     * @return
     */
    boolean isExist(Key key);

    /**
     * Returns the current size of the pool
     *
     * @return the current size of the pool
     */
    int size();

    /**
     * Purges the pool.
     */
    void purge();

    /**
     * Returns all services of the pool
     */
    List<Service> getServices();


    <T> T getPaySupport(Key key, Class<T> type);


    <T> T getPaySupport(String channelCode);
}
