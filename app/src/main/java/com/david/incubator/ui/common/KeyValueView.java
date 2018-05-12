package com.david.incubator.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.david.common.util.Constant;
import com.david.databinding.LayoutKeyValueBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class KeyValueView extends LinearLayout {

    LayoutKeyValueBinding layoutKeyValueBinding;

    public KeyValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutKeyValueBinding = LayoutKeyValueBinding.inflate(layoutInflater, this, true);
    }

    public void setViewModel(KeyValueViewModel keyValueViewModel) {
        layoutKeyValueBinding.setViewModel(keyValueViewModel);
    }

    public KeyValueViewModel getViewModel() {
        return layoutKeyValueBinding.getViewModel();
    }

    public void setListener(Consumer consumer) {
        RxView.clicks(layoutKeyValueBinding.btKeyValue)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(consumer);

        RxView.clicks(layoutKeyValueBinding.tvValue)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }
}
