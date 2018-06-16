package com.david.incubator.ui.menu.sensorlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.AnimationUtil;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutSensorListBinding;
import com.david.incubator.util.ViewUtil;

import java.util.Objects;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:32
 * email: 10525677@qq.com
 * description:
 */

public class SensorListLayout extends BindingConstraintLayout<LayoutSensorListBinding> implements SensorListNavigator {

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
        return R.layout.layout_sensor_list;
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
    public void setSystemMode(boolean isCabin, int humidityObjective, int oxygenObjective, String timingMode) {
        if (isCabin) {
            binding.sensorListFirst.valueColor.set(ResourceUtil.getColor(R.color.c_air));
            binding.sensorListFirst.rightTopIcon.set(R.mipmap.color_air);
            binding.sensorListFirst.rightBottom.set("℃");
            binding.sensorListFirst.setImageIcon.set(R.mipmap.set_air);

            binding.sensorListSecond.valueColor.set(ResourceUtil.getColor(R.color.skin1));
            binding.sensorListSecond.rightBottom.set("℃");
            binding.sensorListSecond.setImageIcon.set(R.mipmap.set_skin1);

            binding.sensorListThird.valueColor.set(ResourceUtil.getColor(R.color.humidity));
            binding.sensorListThird.rightTopIcon.set(R.mipmap.color_humidity);
            binding.sensorListThird.rightTopText.set(null);
            binding.sensorListThird.rightBottom.set("%");
            binding.sensorListThird.setImageIcon.set(R.mipmap.set_humidity);
            binding.sensorListThird.objective.set(ViewUtil.formatHumidityValue(humidityObjective));

            binding.sensorListForth.valueColor.set(ResourceUtil.getColor(R.color.oxygen));
            binding.sensorListForth.rightTopIcon.set(R.mipmap.color_o2);
            binding.sensorListForth.rightBottom.set("%");
            binding.sensorListForth.setImageIcon.set(R.mipmap.set_o2);
            binding.sensorListForth.objective.set(ViewUtil.formatOxygenValue(oxygenObjective));

        } else {
            binding.sensorListFirst.valueColor.set(ResourceUtil.getColor(R.color.skin1));
            binding.sensorListFirst.rightTopIcon.set(R.mipmap.color_skin);
            binding.sensorListFirst.rightBottom.set("℃");
            binding.sensorListFirst.setImageIcon.set(R.mipmap.set_skin1);

            binding.sensorListSecond.valueColor.set(ResourceUtil.getColor(R.color.warmer));
            binding.sensorListSecond.rightBottom.set("%");
            binding.sensorListSecond.setImageIcon.set(R.mipmap.set_manual);

            binding.sensorListThird.valueColor.set(ResourceUtil.getColor(R.color.humidity));
            binding.sensorListThird.rightTopIcon.set(0);
            binding.sensorListThird.rightTopText.set(timingMode);
            binding.sensorListThird.rightBottom.set(null);
            binding.sensorListThird.objective.set(null);

            binding.sensorListForth.valueColor.set(ResourceUtil.getColor(R.color.oxygen));
            binding.sensorListForth.rightTopIcon.set(R.mipmap.color_jaunedice);
            binding.sensorListForth.rightBottom.set(null);
            binding.sensorListForth.objective.set(null);
        }
        binding.sensorListThird.invalidate();
        binding.sensorListForth.invalidate();
    }

    @Override
    public void setCtrlMode(SystemMode systemMode, CtrlMode ctrlMode, int airObjective, int skinObjective, int manObjective) {
        if (Objects.equals(ctrlMode, CtrlMode.Skin)) {
            if (Objects.equals(systemMode, SystemMode.Cabin)) {
                binding.sensorListFirst.showAnimation(false, 0, null);
                binding.sensorListFirst.objective.set(null);

                binding.sensorListSecond.showAnimation(true, R.mipmap.animation_skin, animation);
                binding.sensorListSecond.objective.set(ViewUtil.formatTempValue(skinObjective));
                binding.sensorListSecond.rightTopIcon.set(R.mipmap.color_skin);
            } else if (Objects.equals(systemMode, SystemMode.Warmer)) {
                binding.sensorListFirst.showAnimation(true, R.mipmap.animation_skin, animation);
                binding.sensorListFirst.objective.set(ViewUtil.formatTempValue(skinObjective));

                binding.sensorListSecond.showAnimation(false, 0, null);
                binding.sensorListSecond.objective.set(null);
                binding.sensorListSecond.rightTopIcon.set(0);
            }
        } else if (Objects.equals(ctrlMode, CtrlMode.Air)) {
            binding.sensorListFirst.showAnimation(true, R.mipmap.animation_air, animation);
            binding.sensorListFirst.objective.set(ViewUtil.formatTempValue(airObjective));

            binding.sensorListSecond.showAnimation(false, 0, null);
            binding.sensorListSecond.objective.set(null);
            binding.sensorListSecond.rightTopIcon.set(R.mipmap.color_skin);
        } else if (Objects.equals(ctrlMode, CtrlMode.Manual)) {
            binding.sensorListFirst.showAnimation(false, 0, null);
            binding.sensorListFirst.objective.set(null);

            binding.sensorListSecond.showAnimation(true, R.mipmap.animation_manual, animation);
            binding.sensorListSecond.objective.set(String.valueOf(manObjective));
            binding.sensorListSecond.rightTopIcon.set(R.mipmap.color_manual);
        } else if (Objects.equals(ctrlMode, CtrlMode.Prewarm)) {
            binding.sensorListFirst.showAnimation(false, 0, null);
            binding.sensorListFirst.objective.set(null);

            binding.sensorListSecond.showAnimation(true, R.mipmap.animation_manual, animation);
            binding.sensorListSecond.objective.set(null);
            binding.sensorListSecond.rightTopIcon.set(R.mipmap.color_prewarm);
        }
    }

    @Override
    public void setSpo2Limit(String upperLimit, String lowerLimit) {
        binding.sensorListSpo2.upperLimit.set(upperLimit);
        binding.sensorListSpo2.lowerLimit.set(lowerLimit);
    }

    @Override
    public void setPrLimit(String upperLimit, String lowerLimit) {
        binding.sensorListPr.upperLimit.set(upperLimit);
        binding.sensorListPr.lowerLimit.set(lowerLimit);
    }

    @Override
    public void displayFirstValue(String value) {
        binding.sensorListFirst.value.set(value);
    }

    @Override
    public void displaySecondValue(String value) {
        binding.sensorListSecond.value.set(value);
    }

    @Override
    public void displayThirdValue(String value) {
        binding.sensorListThird.value.set(value);
    }

    @Override
    public void displayForthValue(String value) {
        binding.sensorListForth.value.set(value);
    }

    @Override
    public void displaySpo2Value(String value) {
        binding.sensorListSpo2.value.set(value);
    }

    @Override
    public void displayPrValue(String value) {
        binding.sensorListPr.value.set(value);
    }

    @Override
    public void setManObjective(int manObjective){
        binding.sensorListSecond.objective.set(String.valueOf(manObjective));
    }

    @Override
    public void showHumidity(boolean isHardware, boolean isSoftware) {
        if (isHardware) {
            if (isSoftware) {
                binding.sensorListThird.showView.set(true);
                binding.sensorListThird.setBackground(0);
            } else {
                binding.sensorListThird.showView.set(false);
                binding.sensorListThird.setBackground(R.mipmap.sensor_list_humidity_hide);
            }
        } else {
            binding.sensorListThird.showView.set(false);
            binding.sensorListThird.setBackground(0);
        }
    }

    @Override
    public void showOxygen(boolean isHardware, boolean isSoftware) {
        if (isHardware) {
            if (isSoftware) {
                binding.sensorListForth.showView.set(true);
                binding.sensorListForth.setBackground(0);
            } else {
                binding.sensorListForth.showView.set(false);
                binding.sensorListForth.setBackground(R.mipmap.sensor_list_o2_hide);
            }
        } else {
            binding.sensorListForth.showView.set(false);
            binding.sensorListForth.setBackground(0);
        }
    }

    @Override
    public void showSpo2(boolean isHardware, boolean isSoftware) {
        if (isHardware) {
            if (isSoftware) {
                binding.sensorListSpo2.showView.set(true);
                binding.sensorListSpo2.setBackground(R.mipmap.sensor_list_spo2);

                binding.sensorListPr.showView.set(true);
                binding.sensorListPr.setBackground(R.mipmap.sensor_list_pr);
            } else {
                binding.sensorListSpo2.showView.set(false);
                binding.sensorListSpo2.setBackground(R.mipmap.sensor_list_spo2_hide);

                binding.sensorListPr.showView.set(false);
                binding.sensorListPr.setBackground(R.mipmap.sensor_list_pr_hide);
            }
        } else {
            binding.sensorListSpo2.showView.set(false);
            binding.sensorListSpo2.setBackground(0);

            binding.sensorListPr.showView.set(false);
            binding.sensorListPr.setBackground(0);
        }
    }
}