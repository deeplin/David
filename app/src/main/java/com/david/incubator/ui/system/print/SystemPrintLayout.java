package com.david.incubator.ui.system.print;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.FastUpdateLayout;
import com.david.common.util.Constant;
import com.david.incubator.util.FragmentPage;
import com.david.databinding.LayoutSystemPrintBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/4 20:06
 * email: 10525677@qq.com
 * description:
 */
public class SystemPrintLayout extends FastUpdateLayout<LayoutSystemPrintBinding> {

    SystemPrintViewModel systemPrintViewModel;

    ObservableInt navigationView;

    public SystemPrintLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        systemPrintViewModel = new SystemPrintViewModel();
        binding.setViewModel(systemPrintViewModel);

        init();

        RxView.clicks(binding.printChart)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemPrintViewModel.chartSelected.set(true));

        RxView.clicks(binding.printScale)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemPrintViewModel.chartSelected.set(false));

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> Observable.just(this)
                        .observeOn(Schedulers.io())
                        .subscribe((num) -> systemPrintViewModel.startPrint()));

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    @Override
    protected void increaseValue() {
        systemPrintViewModel.increase();
    }

    @Override
    protected void decreaseValue() {
        systemPrintViewModel.decrease();
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
        stopDisposable();
    }

    private void init() {
        setButton(binding.buttonControl.findViewById(R.id.ibUp),
                binding.buttonControl.findViewById(R.id.ibDown));

        binding.title.setTitle(R.string.data_print);

        binding.buttonControl.setViewModel(
                systemPrintViewModel.buttonControlViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_print;
    }
}
