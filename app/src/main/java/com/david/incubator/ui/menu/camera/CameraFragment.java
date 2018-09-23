package com.david.incubator.ui.menu.camera;

import com.david.R;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingFragment;
import com.david.databinding.FragmentCameraBinding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/2 9:37
 * email: 10525677@qq.com
 * description:
 */
public class CameraFragment extends BindingFragment<FragmentCameraBinding> {

    @Inject
    ShareMemory shareMemory;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public synchronized void attach() {
        shareMemory.layoutLockable.set(false);
        binding.sllLeft.attach();
        binding.frCameraRight.attach();
    }

    @Override
    public synchronized void detach() {
        binding.frCameraRight.detach();
        binding.sllLeft.detach();
    }
}