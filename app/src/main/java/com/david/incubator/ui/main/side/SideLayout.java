package com.david.incubator.ui.main.side;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.david.common.alert.AlertControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.incubator.ui.main.MainViewModel;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
public class SideLayout extends AutoAttachConstraintLayout {

    @Inject
    SideViewModel sideViewModel;
    @Inject
    MainViewModel mainViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    AlertControl alertControl;

//    LayoutSideBinding layoutBinding;

    public SideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutBinding = LayoutSideBinding.inflate(layoutInflater, this, true);
//        layoutBinding.setViewModel(sideViewModel);

//        RxView.clicks(layoutBinding.btSideHome)
//                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
//                .subscribe((Object Void) -> {
//                    if (shareMemory.isCabin()) {
//                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
//                    } else if (shareMemory.isWarmer()) {
//                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
//                    }
//                });
//
//        RxView.clicks(layoutBinding.btSideLockScreen)
//                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
//                .subscribe((Object Void) -> {
//                    SystemMode systemMode = shareMemory.systemMode.get();
//                    if (!systemMode.equals(SystemMode.Transit)) {
//                        mainViewModel.lockClick();
//                    }
//                });
//
//        RxView.clicks(layoutBinding.btSideStopAlarm)
//                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
//                .subscribe((Object Void) -> sideViewModel.muteAlarm());
    }

    @Override
    public void attach() {
        sideViewModel.attach();
    }

    @Override
    public void detach() {
        sideViewModel.detach();
    }
}