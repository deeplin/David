package com.david.incubator.ui.main.side;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.mode.SystemMode;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.IncubatorLayoutSideBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
public class SideLayout extends AutoAttachConstraintLayout {

    @Inject
    SideViewModel viewModel;
    @Inject
    ShareMemory shareMemory;

    IncubatorLayoutSideBinding binding;

    public SideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = IncubatorLayoutSideBinding.inflate(layoutInflater, this, true);
        binding.setViewModel(viewModel);

        RxView.clicks(binding.btSideHome)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    if (shareMemory.isCabin()) {
                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    } else if (shareMemory.isWarmer()) {
                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                });

        RxView.clicks(binding.btSideLockScreen)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (!Objects.equals(systemMode, SystemMode.Transit)) {
                        shareMemory.lockScreen.set(!shareMemory.lockScreen.get());
                    }
                });

        RxView.clicks(binding.btSideClearAlarm)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (!Objects.equals(systemMode, SystemMode.Transit)) {
                        viewModel.clearAlarm();
                    }
                });

        RxView.clicks(binding.btSideMuteAlarm)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (!Objects.equals(systemMode, SystemMode.Transit)) {
                        viewModel.muteAlarm();
                    }
                });
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