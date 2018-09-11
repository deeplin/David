package com.david.common.ui.camera;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.IViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class CameraViewModel implements IViewModel {

    public ObservableBoolean isRecordingVideo = new ObservableBoolean(false);

    public ObservableBoolean hasError = new ObservableBoolean(false);

    public ObservableInt rightImage = new ObservableInt();
    public ObservableInt leftImage = new ObservableInt();

    public ObservableBoolean recordIcon = new ObservableBoolean();
    public ObservableField<String> recordString = new ObservableField<>();

    Disposable disposable = null;

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

        recordTime(false);
    }

    private void showRecordButton() {
        leftImage.set(0);
        rightImage.set(R.drawable.ic_stop);

        recordTime(true);
    }

    private synchronized void recordTime(boolean status) {
        if (status) {
            if (disposable != null) {
                disposable.dispose();
                disposable = null;
            }
            recordIcon.set(true);
            disposable = io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Long aLong) ->
                            recordString.set(String.format(Locale.US, "%02d:%02d:%02d",
                                    aLong / 3600, aLong / 60, aLong % 60)));
        } else {
            recordIcon.set(false);
            if (disposable != null) {
                disposable.dispose();
                disposable = null;
            }
        }
    }
}
