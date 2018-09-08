package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutVideoBinding;
import com.david.incubator.util.FragmentPage;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

public class VideoLayout extends BindingConstraintLayout<LayoutVideoBinding> {

    ObservableInt navigationView;

    public VideoLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;

        init();
        binding.setViewModel(this);

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_MODEL_DETAIL));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_video;
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    private void init() {
        binding.title.setTitle(R.string.video_data);


    }
}
