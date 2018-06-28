package com.david.incubator.ui.common;

import android.content.Context;
import android.databinding.Observable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
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

    public void setValueTextChangedListener(TextWatcher textWatcher) {
        binding.tvValue.addTextChangedListener(textWatcher);
    }

    public String getValueField() {
        return binding.tvValue.getText().toString();
    }

    public void clearValueField(){
        binding.tvValue.setText("");
    }

    @Override
    public void setEnabled(boolean status) {
        binding.btKeyValue.setEnabled(status);
        binding.tvValue.setEnabled(status);
    }
}