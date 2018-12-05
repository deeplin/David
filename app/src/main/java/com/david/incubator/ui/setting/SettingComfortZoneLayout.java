package com.david.incubator.ui.setting;

import android.content.Context;
import android.databinding.ObservableField;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.ui.FastUpdateLayout;
import com.david.common.util.FileUtil;
import com.david.databinding.LayoutSettingComfortZoneBinding;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.common.KeyValueViewModel;

/**
 * author: Ling Lin
 * created on: 2018/1/3 18:59
 * email: 10525677@qq.com
 * description:
 */
public class SettingComfortZoneLayout extends FastUpdateLayout<LayoutSettingComfortZoneBinding> {

    KeyValueViewModel ageKeyValueViewModel;
    KeyValueViewModel gestationKeyValueViewModel;
    KeyValueViewModel weightKeyValueViewModel;

    public ObservableField<String> proposedValue = new ObservableField<>();

    private String[] mGestationDataArray;
    private String[] mWeightDataArray;

    private int age;
    private int gestational;
    private int weight;

    public SettingComfortZoneLayout(Context context) {
        super(context);

        binding.setViewModel(this);

        setButton(binding.buttonControl.findViewById(R.id.ibUp), binding.buttonControl.findViewById(R.id.ibDown));
        init();

        ageKeyValueViewModel.isSelected.set(true);

        binding.comfortZoneAge.setListener(o -> {
            ageKeyValueViewModel.isSelected.set(true);
            gestationKeyValueViewModel.isSelected.set(false);
            weightKeyValueViewModel.isSelected.set(false);
        });

        binding.comfortZoneGestation.setListener(o -> {
            ageKeyValueViewModel.isSelected.set(false);
            gestationKeyValueViewModel.isSelected.set(true);
            weightKeyValueViewModel.isSelected.set(false);
        });

        binding.comfortZoneWeight.setListener(o -> {
            ageKeyValueViewModel.isSelected.set(false);
            gestationKeyValueViewModel.isSelected.set(false);
            weightKeyValueViewModel.isSelected.set(true);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_comfort_zone;
    }

    @Override
    protected void increaseValue() {
        if (ageKeyValueViewModel.isSelected.get()) {
            if (age < 35) {
                age++;
            }
        } else if (gestationKeyValueViewModel.isSelected.get()) {
            if (gestational < 37) {
                gestational++;
            }
        } else if (weightKeyValueViewModel.isSelected.get()) {
            if (weight < 2600) {
                weight += 100;
            }
        }
        display();
    }

    @Override
    protected void decreaseValue() {
        if (ageKeyValueViewModel.isSelected.get()) {
            if (age > 1) {
                age--;
            }
        } else if (gestationKeyValueViewModel.isSelected.get()) {
            if (gestational > 28) {
                gestational--;
            }
        } else if (weightKeyValueViewModel.isSelected.get()) {
            if (weight > 900) {
                weight -= 100;
            }
        }
        display();
    }

    private void init() {
        binding.title.setTitle(R.string.comfort_zone);

        ageKeyValueViewModel = new KeyValueViewModel(R.string.age);
        ageKeyValueViewModel.setUnitText(R.string.day);
        binding.comfortZoneAge.setViewModel(ageKeyValueViewModel);

        gestationKeyValueViewModel = new KeyValueViewModel(R.string.gestation);
        gestationKeyValueViewModel.setUnitText(R.string.week);
        binding.comfortZoneGestation.setViewModel(gestationKeyValueViewModel);

        weightKeyValueViewModel = new KeyValueViewModel(R.string.weight);
        weightKeyValueViewModel.setUnitText("g");
        binding.comfortZoneWeight.setViewModel(weightKeyValueViewModel);

        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showReturn.set(false);
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);
    }

    @Override
    public void attach() {
        int DEFAULT_DAY = 6;
        age = DEFAULT_DAY;
        int DEFAULT_GESTATIONAL_VALUE = 33;
        gestational = DEFAULT_GESTATIONAL_VALUE;
        int DEFAULT_WEIGHT_VALUE = 1600;
        weight = DEFAULT_WEIGHT_VALUE;

        loadAssets();
        display();
    }

    @Override
    public void detach() {
    }

    private void loadAssets() {
        try {
            String GESTATION_DATA_FILE = "ComfortZoneGestation.txt";
            String gestationSource = FileUtil.readTextFileFromAssets(
                    getContext(), GESTATION_DATA_FILE);
            mGestationDataArray = gestationSource.split("\t");

            String WEIGHT_DATA_FILE = "ComfortZoneWeight.txt";
            String weightSource = FileUtil.readTextFileFromAssets(getContext(), WEIGHT_DATA_FILE);
            mWeightDataArray = weightSource.split("\t");
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    private void display() {
        ageKeyValueViewModel.setValueField(String.valueOf(age));
        gestationKeyValueViewModel.setValueField(getGestationString(gestational));
        weightKeyValueViewModel.setValueField(getWeightString(weight));

        if (age <= 7) {
            int index = (age - 1) * 10 + gestational - 28;
            proposedValue.set(String.format(" %s ℃", mGestationDataArray[index]));
        } else {
            int index = (age - 8) * 18 + weight / 100 - 9;
            proposedValue.set(String.format(" %s ℃", mWeightDataArray[index]));
        }
    }

    private String getWeightString(int weightValue) {
        String text = "";
        if (weightValue == 900) {
            text = "500-1000";
        } else if (weightValue == 2600) {
            text = "2600-4000";
        } else if (weightValue < 2600 && weightValue > 900) {
            text = String.valueOf(weightValue) +
                    "-" +
                    (weightValue + 100);
        }
        return text;
    }

    private String getGestationString(int gestationValue) {
        String text = "";
        if (gestationValue == 28) {
            text = "<29";
        } else if (gestationValue == 37) {
            text = ">36";
        } else if ((gestationValue < 37) && (gestationValue > 28)) {
            text = String.valueOf(gestationValue);
        }
        return text;
    }
}