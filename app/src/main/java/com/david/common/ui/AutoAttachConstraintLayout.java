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

public abstract class AutoAttachConstraintLayout<U extends ViewDataBinding> extends TabConstraintLayout<U> {

    public AutoAttachConstraintLayout(Context context) {
        super(context);
    }

    public AutoAttachConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
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
}