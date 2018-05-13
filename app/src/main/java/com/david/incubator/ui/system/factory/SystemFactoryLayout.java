package com.david.incubator.ui.system.factory;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutSystemFactoryBinding;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SystemFactoryLayout extends TabConstraintLayout<LayoutSystemFactoryBinding> {

    @Inject
    MessageSender messageSender;

    public ObservableBoolean selectLower = new ObservableBoolean(false);
    public ObservableBoolean selectUpper = new ObservableBoolean(false);

    ObservableInt navigationView;

    public SystemFactoryLayout(Context context, ObservableInt navigationView) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        this.navigationView = navigationView;

        binding.setViewModel(this);

        binding.title.setTitle(R.string.factory_setting);
        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showUpDown.set(false);
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.systemFactoryLower)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    messageSender.Factory((aBoolean, serialMessage) -> {
                        if (aBoolean) {
                            selectLower.set(true);
                            selectUpper.set(false);
                        }
                    });
                });

        RxView.clicks(binding.systemFactoryUpper)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    selectLower.set(false);
                    selectUpper.set(true);
                });

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_factory;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {

    }
}
