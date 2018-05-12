package com.david.incubator.ui.common;

import android.databinding.ObservableBoolean;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class ButtonControlViewModel {

    public ObservableBoolean showUpDown = new ObservableBoolean(true);
    public ObservableBoolean showReturn = new ObservableBoolean(true);
    public ObservableBoolean showOK = new ObservableBoolean(true);

    public ObservableBoolean okSelected = new ObservableBoolean(false);

}
