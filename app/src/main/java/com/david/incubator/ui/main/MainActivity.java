package com.david.incubator.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.david.R;
import com.david.common.control.AutomationControl;
import com.david.common.control.MainApplication;
import com.david.common.util.AutoUtil;
import com.david.databinding.IncubatorActivityMainBinding;
import com.david.incubator.ui.menu.MenuViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class MainActivity extends Activity implements MainNavigator {

    @Inject
    MainViewModel mainViewModel;
    @Inject
    MenuViewModel menuViewModel;
    @Inject
    AutomationControl automationControl;

    IncubatorActivityMainBinding binding;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        AutoUtil.setSize(this, 1024, 768);

        binding = DataBindingUtil.setContentView(this, R.layout.incubator_activity_main);
        binding.setViewModel(mainViewModel);
        currentFragment = null;
        mainViewModel.setNavigator(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.HIDE_NAVIGATION_BAR");
        MainActivity.this.sendBroadcast(intent);

        binding.avAlarm.attach();
        mainViewModel.attach();
        automationControl.attach();
    }

    @Override
    public void onPause() {
        super.onPause();
        automationControl.detach();
        mainViewModel.detach();
        binding.avAlarm.detach();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* 鼠标电击，自动调用*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                automationControl.initializeTimeOut();
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void changeFragment(byte position) {
        Observable.just(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::rotate);
    }

    /*
     * 碎片换页
     * */
    private void rotate(byte position) {

        FragmentManager fragmentManager = this.getFragmentManager();
        Fragment toFragment = fragmentManager.findFragmentByTag(String.valueOf(position));
        if (toFragment == null) {
            switch (position) {
//                case FragmentPage.HOME_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new HomeFragment();
//                }
//                break;
//                case FragmentPage.OBJECTIVE_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new ObjectiveFragment();
//                }
//                break;
//                case FragmentPage.CHART_FRAGMENT: {
//                    toFragment = new ChartFragment();
//                }
//                break;
//                case FragmentPage.SPO2_FRAGMENT: {
//                    toFragment = new Spo2Fragment();
//                }
//                break;
//                case FragmentPage.SCALE_FRAGMENT: {
//                    toFragment = new ScaleFragment();
//                }
//                break;
//                case FragmentPage.CAMERA_FRAGMENT: {
//                    toFragment = new CameraFragment();
//                }
//                break;
//                case FragmentPage.SETTING_FRAGMENT: {
//                    toFragment = new SettingFragment();
//                }
//                break;
//                case FragmentPage.WARMER_HOME_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new WarmerHomeFragment();
//                }
//                break;
//                case FragmentPage.WARMER_OBJECTIVE_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new WarmerObjectiveFragment();
//                }
//                break;
            }
            ViewUtil.changeFragment(fragmentManager, currentFragment, toFragment, position, R.id.flHome);
            currentFragment = toFragment;
        }
    }

    @Override
    public boolean isLockableFragment() {
        return currentFragment instanceof IFragmentLockable;
    }

    @Override
    public void setLight(int brightness) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.screenBrightness = (float) brightness / 100;
        this.getWindow().setAttributes(lp);
    }
}