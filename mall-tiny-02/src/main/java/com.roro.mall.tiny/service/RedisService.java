package com.roro.mall.tiny.service;

import java.util.concurrent.TimeUnit;

/**
 * redis操作Service,
 * 对象和数组都以json形式进行存储
 * Created by macro on 2018/8/7.
 */
public interface RedisService {

    /**
     * 存储数据
     */
    void set(String key, String value);

    void set(String key, String value, long expire, TimeUnit unit);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire, TimeUnit unit);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

}
