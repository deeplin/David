package com.david.incubator.ui.menu.sensorlist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.common.util.AnimationUtil;
import com.david.databinding.IncubatorLayoutSensorListBinding;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:32
 * email: 10525677@qq.com
 * description:
 */

public class SensorListLayout extends AutoAttachConstraintLayout<IncubatorLayoutSensorListBinding> implements SensorListNavigator {

    @Inject
    SensorListViewModel viewModel;
    AlphaAnimation animation;

    public SensorListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        animation = AnimationUtil.getAlphaAnimation();
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(viewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.incubator_layout_sensor_list;
    }

    @Override
    public void attach() {
        viewModel.setNavigator(this);
        viewModel.attach();
    }

    @Override
    public void detach() {
        viewModel.detach();
        viewModel.setNavigator(null);
    }

    @Override
    public void setBackground(boolean isCabin) {
        io.reactivex.Observable.just(isCabin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        this.setBackgroundResource(R.mipmap.sensor_list_background);
                        binding.tvSensorListOxygenObjective.setVisibility(View.VISIBLE);
                        binding.tvSensorListHumidityObjective.setVisibility(View.VISIBLE);

                        binding.ivSensorListTemp1Set.setImageResource(R.mipmap.set_air);
                    } else {
                        this.setBackgroundResource(R.mipmap.heating_sensor_list_background);
                        binding.tvSensorListOxygenObjective.setVisibility(View.GONE);
                        binding.tvSensorListHumidityObjective.setVisibility(View.GONE);

                        binding.ivSensorListTemp1Set.setImageResource(R.mipmap.set_skin1);
                    }
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        binding.ivSensorListSpo2Hide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.color.border));
                    } else {
                        binding.ivSensorListSpo2Hide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.mipmap.sensor_list_spo2_hide));
                    }
                });
    }

    @Override
    public void oxygenShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        binding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.color.border));
                    } else {
                        binding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.mipmap.sensor_list_o2_hide));
                    }
                });
    }

    @Override
    public void scaleShowBorder(boolean status) {
//        io.reactivex.Observable.just(status)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((st) -> {
//                    if (st) {
//                        binding.ivSensorListOxygenHide
//                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.color.border));
//                    } else {
//                        binding.ivSensorListOxygenHide
//                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.mipmap.sensor_list_scale_hide));
//                    }
//                });
    }

    @Override
    public void humidityShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        binding.ivSensorListHumidityHide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.color.border));
                    } else {
                        binding.ivSensorListHumidityHide
                                .setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.mipmap.sensor_list_humidity_hide));
                    }
                });
    }

    @Override
    public void displayOxygenValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> binding.tvSensorListOxygen.setText(text));
    }

    @Override
    public void displayHumidityValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> binding.tvSensorListHumidity.setText(text));
    }

    @Override
    public void setTimingValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> binding.tvSensorListTiming.setText(text));
    }

    @Override
    public void displayTemp1Value(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> binding.tvSensorListTemp1.setText(text));
    }

    @Override
    public void displayTemp1Objective(String value) {
        int visibility;
        String text;
        if (value != null) {
            visibility = View.VISIBLE;
            text = value;
        } else {
            visibility = View.INVISIBLE;
            text = "";
        }
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    binding.ivSensorListTemp1Set.setVisibility(visibility);
                    binding.tvSensorListTemp1Objective.setText(text);
                });
    }

    @Override
    public void displayTemp2Value(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> binding.tvSensorListTemp2.setText(text));
    }

    @Override
    public void displayTemp2Objective(String value) {
        int visibility;
        String text;
        if (value != null) {
            visibility = View.VISIBLE;
            text = value;
        } else {
            visibility = View.INVISIBLE;
            text = "";
        }
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    binding.ivSensorListTemp2Set.setVisibility(visibility);
                    binding.tvSensorListTemp2Objective.setText(text);
                });
    }

    @Override
    public void displayAirAnimation(boolean isCabin) {
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    synchronized (this) {
                        binding.ivSkinAnimation.clearAnimation();
                        binding.ivSkinAnimation.setVisibility(View.GONE);

                        if (isCabin) {
                            binding.ivAirAnimation.setImageResource(R.mipmap.animation_air);
                            binding.ivAirAnimation.startAnimation(animation);
                            binding.ivAirAnimation.setVisibility(View.VISIBLE);
                        } else {
                            binding.ivAirAnimation.setImageResource(R.mipmap.animation_skin);
                            binding.ivAirAnimation.startAnimation(animation);
                            binding.ivAirAnimation.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void displaySkinAnimation() {
        io.reactivex.Observable.just(this)
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
    public void clearSkinAnimation() {
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    synchronized (this) {
                        binding.ivAirAnimation.clearAnimation();
                        binding.ivAirAnimation.setVisibility(View.GONE);
                        binding.ivSkinAnimation.clearAnimation();
                        binding.ivSkinAnimation.setVisibility(View.GONE);
                    }
                });
    }
}
