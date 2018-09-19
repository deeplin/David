package com.david.common.ui.camera;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.R;

public class CameraViewModel {

    public ObservableBoolean hasError = new ObservableBoolean(false);

    public ObservableBoolean enableCapture = new ObservableBoolean(true);

    public ObservableInt rightImage = new ObservableInt();
    public ObservableInt leftImage = new ObservableInt();

    public ObservableBoolean recordIcon = new ObservableBoolean();
    public ObservableField<String> recordString = new ObservableField<>();

    public CameraViewModel() {
    }

    public void showCaptureButton() {
        if (enableCapture.get()) {
            leftImage.set(R.drawable.ic_photo_camera);
            rightImage.set(R.drawable.ic_videocam);
            recordIcon.set(false);
        }
    }

    public void showRecordButton() {
        if (enableCapture.get()) {
            leftImage.set(0);
            rightImage.set(R.drawable.ic_stop);
            recordIcon.set(true);
        }
    }

    public void showNoButton() {
        leftImage.set(0);
        rightImage.set(0);
        recordIcon.set(false);
    }
}
