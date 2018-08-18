package com.david.incubator.ui.main.top;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.LayoutTopBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
public class TopLayout extends BindingConstraintLayout<LayoutTopBinding> {

    @Inject
    TopViewModel topViewModel;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(topViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_top;
    }

    @Override
    public void attach() {
        topViewModel.attach();
    }

    @Override
    public void detach() {
        topViewModel.detach();
    }
}