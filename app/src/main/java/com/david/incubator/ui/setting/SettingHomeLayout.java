package com.david.incubator.ui.setting;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.view.View;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.ITabConstraintLayout;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.databinding.LayoutSettingHomeBinding;
import com.david.incubator.ui.system.SystemHomeLayout;
import com.david.incubator.ui.system.calibration.SystemCalibrationLayout;
import com.david.incubator.ui.system.deviation.SystemDeviationWarningLayout;
import com.david.incubator.ui.system.factory.SystemFactoryLayout;
import com.david.incubator.ui.system.otherparameter.SystemOtherParameterLayout;
import com.david.incubator.ui.system.overheat.SystemOverheatWarningLayout;
import com.david.incubator.ui.system.print.SystemPrintLayout;
import com.david.incubator.ui.system.range.SystemRangeLayout;
import com.david.incubator.ui.user.UserHomeLayout;
import com.david.incubator.ui.user.UserLanguageLayout;
import com.david.incubator.ui.user.UserOverheatExperimentLayout;
import com.david.incubator.ui.user.UserScreenLuminanceLayout;
import com.david.incubator.ui.user.UserTimeLayout;
import com.david.incubator.ui.user.UserWarningVolumeLayout;
import com.david.incubator.ui.user.usermodel.ImageLayout;
import com.david.incubator.ui.user.usermodel.UserModelDetailLayout;
import com.david.incubator.ui.user.usermodel.UserModelLayout;
import com.david.incubator.ui.user.usermodel.VideoLayout;
import com.david.incubator.util.FragmentPage;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:26
 * email: 10525677@qq.com
 * description:
 */

public class SettingHomeLayout extends BindingConstraintLayout<LayoutSettingHomeBinding> {

    ObservableInt navigationView = new ObservableInt();

    Observable.OnPropertyChangedCallback settingViewCallback;

    View previousViewModel = null;

    public SettingHomeLayout(Context context) {
        super(context);

        settingViewCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (previousViewModel != null) {
                    if (previousViewModel instanceof IViewModel) {
                        ((IViewModel) previousViewModel).detach();
                    }
                    if (previousViewModel instanceof ITabConstraintLayout) {
                        ((ITabConstraintLayout) previousViewModel).stopDisposable();
                    }
                }

                int num = ((ObservableInt) observable).get();
                View view = null;

                switch (num) {
                    case (FragmentPage.SETTING_LOGIN):
                        view = new SettingLoginLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_HOME):
                        view = new UserHomeLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_MODEL):
                        view = new UserModelLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_MODEL_DETAIL):
                        view = new UserModelDetailLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_MODEL_IMAGE):
                        view = new ImageLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_MODEL_VIDEO):
                        view = new VideoLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_TIME_SETUP):
                        view = new UserTimeLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_LANGUAGE_SETUP):
                        view = new UserLanguageLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_WARNING_VOLUME):
                        view = new UserWarningVolumeLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_SCREEN_LUMINANCE):
                        view = new UserScreenLuminanceLayout(context, navigationView);
                        break;
                    case (FragmentPage.USER_OVERHEAT_EXPERIMENT):
                        view = new UserOverheatExperimentLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_HOME):
                        view = new SystemHomeLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_DEVIATION_WARNING):
                        view = new SystemDeviationWarningLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_OVERHEAT_WARNING):
                        view = new SystemOverheatWarningLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_RANGE):
                        view = new SystemRangeLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_CALIBRATION):
                        view = new SystemCalibrationLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_OTHER_PARAMETER):
                        view = new SystemOtherParameterLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_DATA_PRINT):
                        view = new SystemPrintLayout(context, navigationView);
                        break;
                    case (FragmentPage.SYSTEM_FACTORY):
                        view = new SystemFactoryLayout(context, navigationView);
                        break;
                }
                if (view != null) {
                    previousViewModel = view;
                    if (previousViewModel instanceof IViewModel) {
                        ((IViewModel) previousViewModel).attach();
                    }
                    binding.settingHome.removeAllViews();
                    binding.settingHome.addView(view);
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_home;
    }

    @Override
    public void attach() {
        //todo
        if (Constant.RELEASE_TO_DAVID) {
            navigationView.set(FragmentPage.SETTING_LOGIN);
        } else {
            navigationView.set(FragmentPage.USER_HOME);
        }
        navigationView.addOnPropertyChangedCallback(settingViewCallback);
        navigationView.notifyChange();
    }

    @Override
    public void detach() {
        if (previousViewModel != null) {
            if (previousViewModel instanceof IViewModel) {
                ((IViewModel) previousViewModel).detach();
            }
            if (previousViewModel instanceof ITabConstraintLayout) {
                ((ITabConstraintLayout) previousViewModel).stopDisposable();
            }
            previousViewModel = null;
        }
        navigationView.removeOnPropertyChangedCallback(settingViewCallback);
        binding.settingHome.removeAllViews();
    }
}
