package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.widget.Button;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ModuleHardware;
import com.david.common.mode.LanguageMode;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.alarm.AlarmAdapter;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutUserLanguageBinding;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.main.top.TopViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/5 22:11
 * email: 10525677@qq.com
 * description:
 */
public class UserLanguageLayout extends BindingConstraintLayout<LayoutUserLanguageBinding> {

    @Inject
    DaoControl daoControl;
    @Inject
    TopViewModel topViewModel;
    @Inject
    AlarmAdapter alarmAdapter;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    MessageSender messageSender;

    SystemSetting systemSetting;
    ObservableInt navigationView;

    public ObservableBoolean chineseSelected = new ObservableBoolean();
    public ObservableBoolean englishSelected = new ObservableBoolean();
    public ObservableBoolean turkishSelected = new ObservableBoolean();
    public ObservableBoolean polishSelected = new ObservableBoolean();
    public ObservableBoolean russiaSelected = new ObservableBoolean();

    public UserLanguageLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        binding.title.setTitle(R.string.language_setup);
        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showUpDown.set(false);
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.languageChineseButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), Locale.SIMPLIFIED_CHINESE);
                    setUI(LanguageMode.Chinese.getIndex());
                    saveLanguage(LanguageMode.Chinese.getIndex());
                    invalidate();
                    refresh();
                });

        RxView.clicks(binding.languageEnglishButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), Locale.ENGLISH);
                    setUI(LanguageMode.English.getIndex());
                    saveLanguage(LanguageMode.English.getIndex());
                    invalidate();
                    refresh();
                });

        RxView.clicks(binding.languageTurkishButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), new Locale("tr", "TR"));
                    setUI(LanguageMode.Turkish.getIndex());
                    saveLanguage(LanguageMode.Turkish.getIndex());
                    invalidate();
                    refresh();
                });

        RxView.clicks(binding.languagePolishButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), new Locale("pl", "PL"));
                    setUI(LanguageMode.Polish.getIndex());
                    saveLanguage(LanguageMode.Polish.getIndex());
                    invalidate();
                    refresh();
                });

        RxView.clicks(binding.languageRussiaButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), new Locale("ru", "RU"));
                    setUI(LanguageMode.Russia.getIndex());
                    saveLanguage(LanguageMode.Russia.getIndex());
                    invalidate();
                    refresh();
                });

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_HOME));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_language;
    }

    @Override
    public void attach() {
        systemSetting = daoControl.getSystemSetting();
        setUI(systemSetting.getLanguageIndex());
    }

    private void setUI(int languageId) {
        englishSelected.set(languageId == LanguageMode.English.getIndex());
        chineseSelected.set(languageId == LanguageMode.Chinese.getIndex());
        turkishSelected.set(languageId == LanguageMode.Turkish.getIndex());
        polishSelected.set(languageId == LanguageMode.Polish.getIndex());
        russiaSelected.set(languageId == LanguageMode.Russia.getIndex());
    }

    @Override
    public void detach() {
    }

    private void saveLanguage(byte languageId) {
        systemSetting.setLanguageIndex(languageId);
        daoControl.saveSystemSetting(systemSetting);
        if (moduleHardware.is2000S()) {
            messageSender.setLanguage(LanguageMode.values()[languageId].getName(), null);
        }
    }

    private void refresh() {
        binding.title.setTitle(R.string.language_setup);
        Button button = binding.buttonControl.findViewById(R.id.ibReturn);
        button.setText(R.string.exit);

        alarmAdapter.notifyDataSetChanged();
        topViewModel.refresh();
        topViewModel.updateAlarm();
    }
}