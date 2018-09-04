package com.david.common.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.ObservableInt;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.content.ContextCompat;

import com.david.R;
import com.david.common.ui.IViewModel;
import com.david.incubator.control.MainApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class CameraViewModel implements IViewModel {

    public ObservableInt rightImage = new ObservableInt();
    public ObservableInt leftImage = new ObservableInt();

    @Override
    public void attach() {
        showCaptureButton();
    }

    @Override
    public void detach() {
    }

    private void showCaptureButton() {
        leftImage.set(R.drawable.ic_photo_camera);
        rightImage.set(R.drawable.ic_videocam);
    }

    private void showRecordButton() {
        leftImage.set(R.drawable.ic_stop);
        rightImage.set(R.drawable.ic_not_interested);
    }
}
