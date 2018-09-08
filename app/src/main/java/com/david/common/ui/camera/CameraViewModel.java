package com.david.common.ui.camera;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.IViewModel;

public class CameraViewModel implements IViewModel {

    public ObservableBoolean isRecordingVideo = new ObservableBoolean(false);

    public ObservableBoolean hasError = new ObservableBoolean(false);

    public ObservableInt rightImage = new ObservableInt();
    public ObservableInt leftImage = new ObservableInt();

    public CameraViewModel() {
        isRecordingVideo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (isRecordingVideo.get()) {
                    showRecordButton();
                } else {
                    showCaptureButton();
                }
            }
        });
    }

    @Override
    public void attach() {
        isRecordingVideo.notifyChange();
    }

    @Override
    public void detach() {
    }

    private void showCaptureButton() {
        leftImage.set(R.drawable.ic_photo_camera);
        rightImage.set(R.drawable.ic_videocam);
    }

    private void showRecordButton() {
        leftImage.set(0);
        rightImage.set(R.drawable.ic_stop);
    }
}
