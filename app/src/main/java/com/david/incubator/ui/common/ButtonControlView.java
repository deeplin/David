package com.david.incubator.ui.common;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.david.databinding.LayoutButtonControlBinding;


/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class ButtonControlView extends ConstraintLayout {

    LayoutButtonControlBinding layoutButtonControlBinding;

    public ButtonControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutButtonControlBinding = LayoutButtonControlBinding.inflate(layoutInflater, this, true);
    }

    public void setViewModel(ButtonControlViewModel buttonControlViewModel) {
        layoutButtonControlBinding.setViewModel(buttonControlViewModel);
    }
}
