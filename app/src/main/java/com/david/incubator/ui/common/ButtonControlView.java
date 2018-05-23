package com.david.incubator.ui.common;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.LayoutButtonControlBinding;


/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class ButtonControlView extends BindingConstraintLayout<LayoutButtonControlBinding> {

    public ButtonControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_button_control;
    }

    public void setViewModel(ButtonControlViewModel buttonControlViewModel) {
        binding.setViewModel(buttonControlViewModel);
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}
