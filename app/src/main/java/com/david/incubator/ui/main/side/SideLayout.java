package com.david.incubator.ui.main.side;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;
import com.david.databinding.LayoutSideBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
public class SideLayout extends BindingConstraintLayout<LayoutSideBinding> {

    @Inject
    SideViewModel viewModel;
    @Inject
    ShareMemory shareMemory;

    public SideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(viewModel);

        RxView.clicks(binding.btSideHome)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    if (shareMemory.isCabin()) {
                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    } else if (shareMemory.isWarmer()) {
                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                    shareMemory.enableAlertList.set(false);
                });

        RxView.clicks(binding.btSideLockScreen)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    if (!shareMemory.isTransit()) {
                        shareMemory.lockScreen.set(!shareMemory.lockScreen.get());
                    }
                    shareMemory.enableAlertList.set(false);
                });

        RxView.clicks(binding.btSideClearAlarm)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    if (!shareMemory.isTransit()) {
                        viewModel.clearAlarm();
                        shareMemory.enableAlertList.set(false);
                    }
                });

        RxView.clicks(binding.btSideMuteAlarm)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    if (!shareMemory.isTransit()) {
                        viewModel.muteAlarm();
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_side;
    }

    @Override
    public void attach() {
        viewModel.attach();
    }

    @Override
    public void detach() {
        viewModel.detach();
    }
}