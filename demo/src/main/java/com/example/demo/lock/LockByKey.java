package com.example.demo.lock;

/***
 * User:niumengliang
 * Date:2024/9/23
 * Time:09:16
 */
public interface LockByKey<T> {
    /**
     * 加锁
     */
    void lock(T key);

    /**
     * 解锁
     */
    void unlock(T key);
}
