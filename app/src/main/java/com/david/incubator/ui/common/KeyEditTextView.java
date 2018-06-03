package com.david.incubator.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.david.databinding.LayoutKeyEditTextBinding;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class KeyEditTextView extends LinearLayout {

    LayoutKeyEditTextBinding binding;

    public KeyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = LayoutKeyEditTextBinding.inflate(layoutInflater, this, true);
        binding.tvValue.setCursorVisible(false);
    }

    public void setViewModel(KeyEditTextViewModel keyEditTextViewModel) {
        binding.setViewModel(keyEditTextViewModel);
    }

    public String getValueField() {
        return binding.tvValue.getText().toString();
    }

    public void clearValueField(){
        binding.tvValue.setText("");
    }
}