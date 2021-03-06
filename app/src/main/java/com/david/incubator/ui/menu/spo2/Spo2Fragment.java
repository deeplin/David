package com.david.incubator.ui.menu.spo2;

import com.david.R;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingFragment;
import com.david.databinding.FragmentSpo2Binding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/2 20:19
 * email: 10525677@qq.com
 * description:
 */

public class Spo2Fragment extends BindingFragment<FragmentSpo2Binding> {

    @Inject
    MessageSender messageSender;
    @Inject
    ShareMemory shareMemory;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spo2;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public void attach() {
        shareMemory.layoutLockable.set(false);
        binding.sllLeft.attach();
        binding.spo2SurfaceView.attach();
        binding.spo2SettingLayout.attach();
        messageSender.getSpo2(false, shareMemory);
    }

    @Override
    public void detach() {
        binding.spo2SettingLayout.detach();
        binding.spo2SurfaceView.detach();
        binding.sllLeft.detach();
    }
}
