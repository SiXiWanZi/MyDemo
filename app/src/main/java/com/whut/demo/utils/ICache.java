package com.whut.demo.utils;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/27
 *     desc   :
 * </pre>
 */
public interface ICache {
    void put(String var1, Object var2);

    Object get(String var1);

    void remove(String var1);

    boolean contains(String var1);

    void clear();
}
