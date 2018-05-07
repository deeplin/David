package com.david.common.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public abstract class TabConstraintLayout<U extends ViewDataBinding> extends ConstraintLayout implements IViewModel {

    protected U binding;

    public TabConstraintLayout(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
    }

    public TabConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
    }

    protected abstract int getLayoutId();
}
