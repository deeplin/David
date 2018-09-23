package com.david.incubator.ui.main;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:47
 * email: 10525677@qq.com
 * description:
 */
public interface MainNavigator {

    /*转换碎片*/
    void changeFragment(byte position);

    void setLight(int brightness);
}
