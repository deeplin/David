package com.david.incubator.ui.main.top;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutTopBinding;
import com.david.incubator.ui.main.MainViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
public class TopLayout extends AutoAttachConstraintLayout<LayoutTopBinding> {

    @Inject
    TopViewModel topViewModel;
    @Inject
    MainViewModel mainViewModel;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(topViewModel);

        RxView.clicks(binding.tvTopAlarm)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> mainViewModel.showAlertList.set(!mainViewModel.showAlertList.get()));
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