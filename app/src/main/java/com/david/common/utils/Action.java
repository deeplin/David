package com.david.common.utils;

/**
 * author: Ling Lin
 * created on: 2017/7/25 21:08
 * email: 10525677@qq.com
 * description:
 */

public interface Action<T> {
    void accept(T t);
}
