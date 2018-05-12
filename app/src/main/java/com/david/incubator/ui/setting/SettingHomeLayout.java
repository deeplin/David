package com.david.incubator.ui.setting;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.view.View;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.ITabConstraintLayout;
import com.david.common.ui.IViewModel;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutSettingHomeBinding;
import com.david.incubator.ui.user.UserHomeLayout;
import com.david.incubator.ui.user.UserLanguageLayout;
import com.david.incubator.ui.user.UserScreenLuminanceLayout;
import com.david.incubator.ui.user.UserTimeLayout;
import com.david.incubator.ui.user.UserWarningVolumeLayout;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:26
 * email: 10525677@qq.com
 * description:
 */

public class SettingHomeLayout extends TabConstraintLayout<LayoutSettingHomeBinding> {

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
                        view = new SettingLoginLayout(MainApplication.getInstance(), navigationView);
                        break;
                    case (FragmentPage.USER_HOME):
                        view = new UserHomeLayout(MainApplication.getInstance(), navigationView);
                        break;
//                    case (FragmentPage.USER_MODEL):
//                        view = new UserModelLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.USER_MODEL_DETAIL):
//                        view = new UserModelDetailLayout(MainApplication.getInstance(), navigationView);
//                        break;
                    case (FragmentPage.USER_TIME_SETUP):
                        view = new UserTimeLayout(MainApplication.getInstance(), navigationView);
                        break;
                    case (FragmentPage.USER_LANGUAGE_SETUP):
                        view = new UserLanguageLayout(MainApplication.getInstance(), navigationView);
                        break;
                    case (FragmentPage.USER_WARNING_VOLUME):
                        view = new UserWarningVolumeLayout(MainApplication.getInstance(), navigationView);
                        break;
                    case (FragmentPage.USER_SCREEN_LUMINANCE):
                        view = new UserScreenLuminanceLayout(MainApplication.getInstance(), navigationView);
                        break;
//                    case (FragmentPage.SYSTEM_HOME):
//                        view = new SystemHomeLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_DEVIATION_WARNING):
//                        view = new SystemDeviationWarningLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_OVERHEAT_WARNING):
//                        view = new SystemOverheatWarningLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_RANGE):
//                        view = new SystemRangeLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_CALIBRATION):
//                        view = new SystemCalibrationLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_OTHER_PARAMETER):
//                        view = new SystemOtherParameterLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_OVERHEAT_EXPERIMENT):
//                        view = new SystemOverheatExperimentLayout(MainApplication.getInstance(), navigationView);
//                        break;
//                    case (FragmentPage.SYSTEM_DATA_PRINT):
//                        view = new SystemPrintLayout(MainApplication.getInstance(), navigationView);
//                        break;
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
