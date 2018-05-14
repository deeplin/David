package com.david.incubator.ui.menu.spo2.view;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.LayoutSpo2ViewBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/2 21:04
 * email: 10525677@qq.com
 * description:
 */
public class Spo2ViewLayout extends BindingConstraintLayout<LayoutSpo2ViewBinding> {

    @Inject
    public ShareMemory shareMemory;


    public Spo2ViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_spo2_view;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}
