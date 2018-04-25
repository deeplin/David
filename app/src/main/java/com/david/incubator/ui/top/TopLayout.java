package com.david.incubator.ui.top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.david.common.control.MainApplication;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.databinding.LayoutTopBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
public class TopLayout extends AutoAttachConstraintLayout {

    @Inject
    TopViewModel topViewModel;
    LayoutTopBinding monitorTopBinding;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        monitorTopBinding = LayoutTopBinding.inflate(layoutInflater, this, true);

        monitorTopBinding.setViewModel(topViewModel);
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