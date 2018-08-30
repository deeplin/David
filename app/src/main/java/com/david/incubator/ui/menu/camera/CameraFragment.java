package com.david.incubator.ui.menu.camera;

import android.view.View;

import com.david.R;
import com.david.common.ui.BindingFragment;
import com.david.common.util.LogUtil;
import com.david.databinding.FragmentCameraBinding;
import com.david.incubator.ui.main.IFragmentLockable;

/**
 * author: Ling Lin
 * created on: 2018/1/2 9:37
 * email: 10525677@qq.com
 * description:
 */
public class CameraFragment extends BindingFragment<FragmentCameraBinding> implements IFragmentLockable {

    CameraPreview cameraPreview = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void init() {
    }

    @Override
    public synchronized void attach() {
        binding.sllLeft.attach();
        try {
            if (cameraPreview == null) {
                cameraPreview = new CameraPreview(getView().getContext());
                cameraPreview.startCamera();
                binding.frCameraRight.addView(cameraPreview);
            } else {
                cameraPreview.startCamera();
            }
            binding.tvCameraError.setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtil.i(this, "Open camera error.");
        }
    }

    @Override
    public synchronized void detach() {
        if (cameraPreview != null) {
            cameraPreview.stopCamera();
            cameraPreview = null;
            binding.frCameraRight.removeAllViews();
        }
        binding.sllLeft.detach();
    }
}