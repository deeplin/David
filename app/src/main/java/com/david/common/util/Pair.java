package com.david.common.util;

/**
 * author: Ling Lin
 * created on: 2017/8/2 20:47
 * email: 10525677@qq.com
 * description:
 */

public class Pair<U, V> {
    U key;
    V value;

    public Pair(U key, V value) {
        this.key = key;
        this.value = value;
    }

    public U getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
