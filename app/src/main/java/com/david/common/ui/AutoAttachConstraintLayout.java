package com.david.common.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * author: Ling Lin
 * created on: 2017/8/3 10:52
 * email: 10525677@qq.com
 * description:
 */

public abstract class AutoAttachConstraintLayout<U extends ViewDataBinding> extends ConstraintLayout implements IViewModel {

    protected U binding;

    public AutoAttachConstraintLayout(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
    }

    public AutoAttachConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch (visibility) {
            case View.VISIBLE:
                attach();
                break;
            default:
                detach();
                break;
        }
    }

    protected abstract int getLayoutId();
}