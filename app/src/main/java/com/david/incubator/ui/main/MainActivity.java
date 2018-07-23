package com.david.incubator.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.david.R;
import com.david.common.control.AutomationControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.ui.AutoAttachFragment;
import com.david.common.util.AutoUtil;
import com.david.common.util.FragmentPage;
import com.david.databinding.ActivityMainBinding;
import com.david.incubator.ui.home.cabin.HomeFragment;
import com.david.incubator.ui.home.warmer.WarmerHomeFragment;
import com.david.incubator.ui.menu.MenuViewModel;
import com.david.incubator.ui.menu.camera.CameraFragment;
import com.david.incubator.ui.menu.chart.ChartFragment;
import com.david.incubator.ui.menu.scale.ScaleFragment;
import com.david.incubator.ui.menu.spo2.Spo2Fragment;
import com.david.incubator.ui.objective.cabin.ObjectiveHomeFragment;
import com.david.incubator.ui.objective.warmer.WarmerObjectiveHomeFragment;
import com.david.incubator.ui.setting.SettingHomeFragment;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class MainActivity extends AppCompatActivity implements MainNavigator {

    @Inject
    MainViewModel mainViewModel;
    @Inject
    MenuViewModel menuViewModel;
    @Inject
    AutomationControl automationControl;
    @Inject
    ShareMemory shareMemory;

    ActivityMainBinding binding;
    private AutoAttachFragment[] fragmentArray;
    private AutoAttachFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        AutoUtil.setSize(this, 1024, 768);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(mainViewModel);

        mainViewModel.setNavigator(this);

        binding.getRoot().setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    double x = event.getX();
                    double y = event.getY() / AutoUtil.heightRadio + 0.5f;
                    if (x < 616 && y < 120) {
                        shareMemory.enableAlertList.set(!shareMemory.enableAlertList.get());
                    } else {
                        shareMemory.enableAlertList.set(false);
                    }
                    break;
                }
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.HIDE_NAVIGATION_BAR");
//        MainActivity.this.sendBroadcast(intent);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initFragment();

        binding.avAlarm.attach();
        mainViewModel.attach();
        automationControl.attach();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (currentFragment != null) {
//            Log.e("deeplin3","detach);");
//            currentFragment.detach();
//            currentFragment = null;
//        }

//        automationControl.detach();
//        mainViewModel.detach();
//        binding.avAlarm.detach();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.getInstance().stop();
        System.exit(0);
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

    private void initFragment() {
        fragmentArray = new AutoAttachFragment[FragmentPage.WARMER_OBJECTIVE_FRAGMENT + 1];
        fragmentArray[FragmentPage.HOME_FRAGMENT] = new HomeFragment();
        fragmentArray[FragmentPage.OBJECTIVE_FRAGMENT] = new ObjectiveHomeFragment();
        fragmentArray[FragmentPage.SETTING_FRAGMENT] = new SettingHomeFragment();
        fragmentArray[FragmentPage.CHART_FRAGMENT] = new ChartFragment();
        fragmentArray[FragmentPage.SPO2_FRAGMENT] = new Spo2Fragment();
        fragmentArray[FragmentPage.SCALE_FRAGMENT] = new ScaleFragment();
        fragmentArray[FragmentPage.CAMERA_FRAGMENT] = new CameraFragment();

        fragmentArray[FragmentPage.WARMER_HOME_FRAGMENT] = new WarmerHomeFragment();
        fragmentArray[FragmentPage.WARMER_OBJECTIVE_FRAGMENT] = new WarmerObjectiveHomeFragment();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (int index = 0; index < fragmentArray.length; index++) {
            Fragment fragment = fragmentArray[index];
            if (fragment != null) {
                transaction.add(R.id.flHome, fragment);
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    /*
     * 碎片换页
     * */
    private synchronized void rotate(byte position) {
        shareMemory.enableAlertList.set(false);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (currentFragment != null) {
            transaction.hide(currentFragment);
            currentFragment.detach();
            currentFragment = null;
        }

        AutoAttachFragment toFragment = fragmentArray[position];
        if (toFragment != null) {
            transaction.show(toFragment);
            transaction.commit();
            currentFragment = toFragment;
            currentFragment.attach();
        }

        switch (position) {
            case FragmentPage.HOME_FRAGMENT: {
                menuViewModel.clearButtonBorder();
            }
            break;
            case FragmentPage.OBJECTIVE_FRAGMENT: {
                menuViewModel.clearButtonBorder();
            }
            break;
            case FragmentPage.WARMER_HOME_FRAGMENT: {
                menuViewModel.clearButtonBorder();
            }
            break;
            case FragmentPage.WARMER_OBJECTIVE_FRAGMENT: {
                menuViewModel.clearButtonBorder();
            }
            break;
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