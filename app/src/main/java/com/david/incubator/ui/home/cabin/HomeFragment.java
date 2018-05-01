package com.david.incubator.ui.home.cabin;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.FunctionMode;
import com.david.common.ui.AutoAttachFragment;
import com.david.common.util.AnimationUtil;
import com.david.common.util.AutoUtil;
import com.david.common.util.FragmentPage;
import com.david.databinding.IncubatorFragmentHomeBinding;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */
public class HomeFragment extends AutoAttachFragment<IncubatorFragmentHomeBinding> implements HomeNavigator {

    @Inject
    HomeViewModel homeViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;

    AlphaAnimation animation;

    @Override
    protected int getLayoutId() {
        return R.layout.incubator_fragment_home;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(homeViewModel);
        animation = AnimationUtil.getAlphaAnimation();

        View view = binding.getRoot();

        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    double x = event.getX();
                    double y = event.getY() / AutoUtil.heightRadio + 0.5f;
                    int functionTab = CtrlMode.None.getIndex();
                    if (x < 30) {
                    } else if (x < 580) {
                        if (y < 10) {
                        } else if (y < 190) {
                            functionTab = CtrlMode.Air.getIndex();
                        } else if (y < 230) {
                        } else if (y < 490) {
                            functionTab = CtrlMode.Skin.getIndex();
                        }
                    } else if (x < 630) {
                    } else if (x < 890) {
                        if (y < 10) {
                        } else if (y < 150) {
                            if (moduleHardware.isHUM())
                                functionTab = FunctionMode.Humidity.getIndex();
                        } else if (y < 180) {
                        } else if (y < 300) {
                            if (moduleHardware.isO2())
                                functionTab = FunctionMode.Oxygen.getIndex();
                        } else if (y < 330) {
                        } else if (y < 460) {
                            if (moduleHardware.isSPO2())
                                functionTab = FunctionMode.Spo2.getIndex();
                        } else if (y < 480) {
                        } else if (y < 600) {
                            if (moduleHardware.isSPO2())
                                functionTab = FunctionMode.Pr.getIndex();
                        }
                    }
                    if (!shareMemory.lockScreen.get() && functionTab != CtrlMode.None.getIndex()) {
                        shareMemory.functionTag.set(functionTab);
                        shareMemory.currentFragmentID.set(FragmentPage.OBJECTIVE_FRAGMENT);
                    }
                    break;
                }
            }
            return true;
        });
    }

    @Override
    public void attach() {
        homeViewModel.setNavigator(this);
        homeViewModel.attach();
    }

    @Override
    public void detach() {
        homeViewModel.detach();
        homeViewModel.setNavigator(null);
    }

    @Override
    public void setHeatStep(int step) {
        Observable.just(step)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((num) -> {
                    ViewGroup.LayoutParams params = binding.ivHomeHeatingHide.getLayoutParams();
                    params.width = (int) ((100 - num) * 1.74);
                    binding.ivHomeHeatingHide.setVisibility(View.GONE);
                    binding.ivHomeHeatingHide.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        if (status) {
            binding.ivHomeSpo2Hide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            binding.ivHomeSpo2Hide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_spo2_hide));
        }
    }

    @Override
    public void oxygenShowBorder(boolean status) {
        if (status) {
            binding.ivHomeOxygenHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            binding.ivHomeOxygenHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_oxygen_hide));
        }
    }

    @Override
    public void humidityShowBorder(boolean status) {
        if (status) {
            binding.ivHomeHumidityHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            binding.ivHomeHumidityHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_humidity_hide));
        }
    }

    @Override
    public void showAir() {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    synchronized (this) {
                        binding.ivSkinAnimation.clearAnimation();
                        binding.ivSkinAnimation.setVisibility(View.GONE);
                        binding.ivAirAnimation.startAnimation(animation);
                        binding.ivAirAnimation.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void showSkin() {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    synchronized (this) {
                        binding.ivAirAnimation.clearAnimation();
                        binding.ivAirAnimation.setVisibility(View.GONE);
                        binding.ivSkinAnimation.startAnimation(animation);
                        binding.ivSkinAnimation.setVisibility(View.VISIBLE);
                    }
                });
    }
}
