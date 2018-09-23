package com.david.incubator.ui.home.cabin;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.david.R;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.FunctionMode;
import com.david.common.ui.AnimationUtil;
import com.david.common.ui.BindingFragment;
import com.david.common.util.AutoUtil;
import com.david.databinding.FragmentHomeBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */
public class HomeFragment extends BindingFragment<FragmentHomeBinding> implements HomeNavigator {

    @Inject
    HomeViewModel homeViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;

    AlphaAnimation animation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        shareMemory.layoutLockable.set(false);

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
                        if (y < 80) {
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
                    if (moduleHardware.is970SAndJaundice()) {
                        if (x > 340 && x < 580 && y > 460 && y < 610) {
                            if (homeViewModel.jaundice.get())
                                binding.jaunediceView.press();
                        }
                    }
                    if (!shareMemory.lockScreen.get() && functionTab != CtrlMode.None.getIndex() && (shareMemory.ohTest.get() == 0)) {
                        shareMemory.functionTag.set(functionTab);
                        shareMemory.currentFragmentId.set(FragmentPage.OBJECTIVE_FRAGMENT);
                    }
                    break;
                }
            }
            return false;
        });
    }

    @Override
    public void attach() {
        homeViewModel.setNavigator(this);
        homeViewModel.attach();

        if (moduleHardware.is970SAndJaundice())
            binding.jaunediceView.attach();
    }

    @Override
    public void detach() {
        if (moduleHardware.is970SAndJaundice())
            binding.jaunediceView.detach();

        homeViewModel.detach();
        homeViewModel.setNavigator(null);
    }

    @Override
    public void setHeatStep(int step) {
        Observable.just(step)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((num) -> {
                    synchronized (this) {
                        ViewGroup.LayoutParams params = binding.ivHomeHeatingHide.getLayoutParams();
                        params.width = (int) ((100 - num) * 1.74);
                        homeViewModel.incPower.set(num + "%");
                    }
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    if (status) {
                        binding.ivHomeSpo2Hide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
                    } else {
                        binding.ivHomeSpo2Hide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_spo2_hide));
                    }
                });
    }

    @Override
    public void oxygenShowBorder(boolean status) {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    if (status) {
                        binding.ivHomeOxygenHide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
                    } else {
                        binding.ivHomeOxygenHide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_oxygen_hide));
                    }
                });
    }

    @Override
    public void humidityShowBorder(boolean status) {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    if (status) {
                        binding.ivHomeHumidityHide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
                    } else {
                        binding.ivHomeHumidityHide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_humidity_hide));
                    }
                });
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

    @Override
    public synchronized void setHumidityPower(boolean status) {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    if (status) {
                        binding.ivHomeHumidityPower
                                .setImageResource(R.mipmap.power_on);
                    } else {
                        binding.ivHomeHumidityPower
                                .setImageResource(R.mipmap.power_off);
                    }
                });
    }

    @Override
    public synchronized void setOxygenPower(boolean status) {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    if (status) {
                        binding.ivHomeOxygenPower
                                .setImageResource(R.mipmap.power_on);
                    } else {
                        binding.ivHomeOxygenPower
                                .setImageResource(R.mipmap.power_off);
                    }
                });
    }
}