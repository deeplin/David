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

    File imageFile;

    @Override
    public void attach() {
        showCaptureButton();

        imageFile = new File(MainApplication.getInstance().getExternalFilesDir(null).getPath());
    }

    @Override
    public void detach() {
    }



    private void showCaptureButton() {
        leftImage.set(R.drawable.ic_photo_camera);
        rightImage.set(R.drawable.ic_videocam);
    }

    private void showConfirmButton() {
        leftImage.set(R.drawable.ic_check_circle);
        rightImage.set(R.drawable.ic_cancel);
    }

    private void showRecordButton() {
        leftImage.set(R.drawable.ic_stop);
        rightImage.set(R.drawable.ic_not_interested);
    }
}
