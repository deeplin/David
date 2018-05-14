package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.widget.Button;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.SystemSetting;
import com.david.common.mode.LanguageMode;
import com.david.common.ui.BindingConstraintLayout;
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

    SystemSetting systemSetting;
    ObservableInt navigationView;

    public ObservableBoolean chineseSelected = new ObservableBoolean();
    public ObservableBoolean englishSelected = new ObservableBoolean();

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
                    chineseSelected.set(true);
                    englishSelected.set(false);
                    saveLanguage(LanguageMode.Chinese.getIndex());
                    topViewModel.loadUserId();
                    refresh();
                });

        RxView.clicks(binding.languageEnglishButton)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    ResourceUtil.setLocalLanguage(getContext(), Locale.ENGLISH);
                    chineseSelected.set(false);
                    englishSelected.set(true);
                    saveLanguage(LanguageMode.English.getIndex());
                    topViewModel.loadUserId();
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
        if (systemSetting.getLanguageIndex() == LanguageMode.English.getIndex()) {
            chineseSelected.set(false);
            englishSelected.set(true);
        } else if (systemSetting.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
            chineseSelected.set(true);
            englishSelected.set(false);
        }
    }

    @Override
    public void detach() {
    }

    private void saveLanguage(byte languageId) {
        systemSetting.setLanguageIndex(languageId);
        daoControl.saveSystemSetting(systemSetting);
    }

    private void refresh() {
        binding.title.setTitle(R.string.language_setup);
        Button button = binding.buttonControl.findViewById(R.id.ibReturn);
        button.setText(R.string.exit);
    }
}
