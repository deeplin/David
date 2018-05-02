package com.david.incubator.ui.home.warmer;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
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
import com.david.databinding.WarmerFragmentHomeBinding;
import com.david.incubator.util.TimingData;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:16
 * email: 10525677@qq.com
 * description:
 */
public class WarmerHomeFragment extends AutoAttachFragment<WarmerFragmentHomeBinding> implements WarmerHomeNavigator {

    @Inject
    WarmerViewModel warmerViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    TimingData timingData;

    AlphaAnimation animation;

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        animation = AnimationUtil.getAlphaAnimation();

        binding.setViewModel(warmerViewModel);

        View view = binding.getRoot();
        view.setOnTouchListener((v, event) -> {
            synchronized (this) {
                boolean lockScreen = shareMemory.lockScreen.get();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        double x = event.getX();
                        double y = event.getY() / AutoUtil.heightRadio + 0.5f;
                        int functionTab = CtrlMode.None.getIndex();
                        if (x < 30) {
                        } else if (x < 580) {
                            if (y < 10) {
                            } else if (y < 190) {
                                functionTab = CtrlMode.Skin.getIndex();
                            } else if (y < 230) {
                            } else if (y < 590) {
                                functionTab = CtrlMode.Manual.getIndex();
                            }
                        } else if (x < 630) {
                        } else if (x < 890) {
                            if (y < 10) {
                            } else if (y < 150) {
                                lockScreen = false;
                                if (timingData.isStarted()) {
                                    timingData.stop();
                                } else {
                                    timingData.start();
                                }
                            } else if (y < 180) {
                            } else if (y < 300) {
                                if (warmerViewModel.jaundice.get())
                                    binding.jaunediceView.press();
                            } else if (y < 330) {
                            } else if (y < 460) {
                                if (moduleHardware.isSPO2())
                                    functionTab = FunctionMode.Spo2.getIndex() - 1;
                            } else if (y < 480) {
                            } else if (y < 600) {
                                if (moduleHardware.isSPO2())
                                    functionTab = FunctionMode.Pr.getIndex() - 1;
                            }
                        }
                        if ((!lockScreen) && functionTab != CtrlMode.None.getIndex()) {
                            shareMemory.functionTag.set(functionTab);
                            shareMemory.currentFragmentID.set(FragmentPage.WARMER_OBJECTIVE_FRAGMENT);
                        }
                        break;
                    }
                }
            }
            return true;
        });
    }

    @Override
    public void attach() {
        warmerViewModel.setNavigator(this);
        warmerViewModel.attach();
        binding.heatingTimeView.attach();
        binding.jaunediceView.attach();
    }

    @Override
    public void detach() {
        binding.jaunediceView.detach();
        binding.heatingTimeView.detach();
        warmerViewModel.detach();
        warmerViewModel.setNavigator(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.warmer_fragment_home;
    }

    @Override
    public void setHeatStep(int step) {
        Observable.just(step)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((num) -> {
                    binding.heatingView.setHeat(num);
                    binding.heatingView.setVisibility(View.GONE);
                    binding.heatingView.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        binding.ivHomeSpo2Hide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
                    } else {
                        binding.ivHomeSpo2Hide
                                .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_spo2_hide));
                    }
                });

    }

    @Override
    public void showSkin() {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    binding.ivManualAnimation.clearAnimation();
                    binding.ivManualAnimation.setVisibility(View.GONE);
                    binding.ivSkinAnimation.startAnimation(animation);
                    binding.ivSkinAnimation.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void showManual() {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    binding.ivSkinAnimation.clearAnimation();
                    binding.ivSkinAnimation.setVisibility(View.GONE);
                    binding.ivManualAnimation.startAnimation(animation);
                    binding.ivManualAnimation.setVisibility(View.VISIBLE);
                });
    }
}