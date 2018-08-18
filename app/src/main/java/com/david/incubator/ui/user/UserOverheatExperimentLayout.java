package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.incubator.util.FragmentPage;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.david.databinding.LayoutUserOverheatExperimentBinding;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/19 19:18
 * email: 10525677@qq.com
 * description:
 */
public class UserOverheatExperimentLayout extends BindingConstraintLayout<LayoutUserOverheatExperimentBinding> {

    @Inject
    MessageSender messageSender;
    @Inject
    public ShareMemory shareMemory;

    public ObservableBoolean airAbove37Selected = new ObservableBoolean(false);
    public ObservableBoolean airBelow37Selected = new ObservableBoolean(false);
    public ObservableBoolean skinAbove37Selected = new ObservableBoolean(false);
    public ObservableBoolean stableStateSelected = new ObservableBoolean(false);

    ObservableInt navigationView;

    public UserOverheatExperimentLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;

        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        binding.title.setTitle(R.string.overheat_experiment);
        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showUpDown.set(false);
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.overheatAirAbove37)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> selectAirAbove37());

        RxView.clicks(binding.overheatAirBelow37)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> selectAirBelow37());

        RxView.clicks(binding.overheatSkinAbove37)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> selectSkinAbove37());

        RxView.clicks(binding.overheatStableState)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> selectStableState());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_HOME));
    }

    private void selectAirAbove37() {
        messageSender.setCtrlSet(SystemMode.Cabin.getName(), CtrlMode.Air.getName(), 390, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                messageSender.setCtrlOverride((aBoolean1, baseSerialMessage1) -> {
                    airAbove37Selected.set(true);
                    airBelow37Selected.set(false);
                    skinAbove37Selected.set(false);
                    stableStateSelected.set(false);
                });
            }
        });
    }

    private void selectAirBelow37() {
        messageSender.setCtrlSet(SystemMode.Cabin.getName(), CtrlMode.Air.getName(), 370, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                messageSender.setCtrlOverride((aBoolean1, baseSerialMessage1) -> {
                    airAbove37Selected.set(false);
                    airBelow37Selected.set(true);
                    skinAbove37Selected.set(false);
                    stableStateSelected.set(false);
                });
            }
        });
    }

    private void selectSkinAbove37() {
        messageSender.setCtrlSet(SystemMode.Cabin.getName(), CtrlMode.Skin.getName(), 380, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                messageSender.setCtrlOverride((aBoolean1, baseSerialMessage1) -> {
                    airAbove37Selected.set(false);
                    airBelow37Selected.set(false);
                    skinAbove37Selected.set(true);
                    stableStateSelected.set(false);
                });
            }
        });
    }

    private void selectStableState() {
        messageSender.setCtrlSet(SystemMode.Warmer.getName(), CtrlMode.Skin.getName(), 380, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                messageSender.setCtrlOverride((aBoolean1, baseSerialMessage1) -> {
                    airAbove37Selected.set(false);
                    airBelow37Selected.set(false);
                    skinAbove37Selected.set(false);
                    stableStateSelected.set(true);
                });
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_overheat_experiment;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}
