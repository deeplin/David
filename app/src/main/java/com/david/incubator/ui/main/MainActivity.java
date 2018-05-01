package com.david.incubator.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.david.R;
import com.david.common.control.AutomationControl;
import com.david.common.control.MainApplication;
import com.david.common.ui.AutoAttachFragment;
import com.david.common.util.AutoUtil;
import com.david.common.util.FragmentPage;
import com.david.databinding.IncubatorActivityMainBinding;
import com.david.incubator.ui.home.cabin.HomeFragment;
import com.david.incubator.ui.menu.MenuViewModel;
import com.david.incubator.ui.objective.cabin.ObjectiveFragment;

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

    IncubatorActivityMainBinding binding;
    private AutoAttachFragment[] fragmentArray;
    private AutoAttachFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        AutoUtil.setSize(this, 1024, 768);

        binding = DataBindingUtil.setContentView(this, R.layout.incubator_activity_main);
        binding.setViewModel(mainViewModel);

        mainViewModel.setNavigator(this);
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

        binding.avAlarm.attach();
        mainViewModel.attach();
        automationControl.attach();

        initFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentFragment.detach();

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
        mainViewModel.showAlertList.set(false);
        Observable.just(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::rotate);
    }

    private void initFragment() {
        fragmentArray = new AutoAttachFragment[FragmentPage.WARMER_OBJECTIVE_FRAGMENT];
        fragmentArray[FragmentPage.HOME_FRAGMENT] = new HomeFragment();
        fragmentArray[FragmentPage.OBJECTIVE_FRAGMENT] = new ObjectiveFragment();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.flHome, fragmentArray[FragmentPage.OBJECTIVE_FRAGMENT]);
        transaction.hide(fragmentArray[FragmentPage.OBJECTIVE_FRAGMENT]);
        transaction.add(R.id.flHome, fragmentArray[FragmentPage.HOME_FRAGMENT]);
        transaction.hide(fragmentArray[FragmentPage.HOME_FRAGMENT]);
        transaction.commit();
    }

    /*
     * 碎片换页
     * */
    private synchronized void rotate(byte position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AutoAttachFragment toFragment = fragmentArray[position];
        if(currentFragment == null){
            transaction.show(toFragment);
            transaction.commit();
        }else if (toFragment != currentFragment) {
            transaction.hide(currentFragment).show(toFragment);
            transaction.commit();

            switch (position) {
                case FragmentPage.HOME_FRAGMENT: {
                    menuViewModel.clearButtonBorder();
                }
                break;
                case FragmentPage.OBJECTIVE_FRAGMENT: {
                    menuViewModel.clearButtonBorder();
                }
                break;
//            case FragmentPage.CHART_FRAGMENT: {
//                toFragment = new ChartFragment();
//            }
//            break;
//            case FragmentPage.SPO2_FRAGMENT: {
//                toFragment = new Spo2Fragment();
//            }
//            break;
//            case FragmentPage.SCALE_FRAGMENT: {
//                toFragment = new ScaleFragment();
//            }
//            break;
//            case FragmentPage.CAMERA_FRAGMENT: {
//                toFragment = new CameraFragment();
//            }
//            break;
//            case FragmentPage.SETTING_FRAGMENT: {
//                toFragment = new SettingFragment();
//            }
//            break;
//            case FragmentPage.WARMER_HOME_FRAGMENT: {
//                menuViewModel.clearButtonBorder();
//                toFragment = new WarmerHomeFragment();
//            }
//            break;
//            case FragmentPage.WARMER_OBJECTIVE_FRAGMENT: {
//                menuViewModel.clearButtonBorder();
//                toFragment = new WarmerObjectiveFragment();
//            }
//            break;
            }
        }
        currentFragment = toFragment;
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