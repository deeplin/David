package com.david.common.ui;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseNavigatorModel<T> implements IViewModel {

    protected T navigator;

    public void setNavigator(T navigator) {
        this.navigator = navigator;
    }
}
