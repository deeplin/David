package com.david.common.ui.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.AutomationControl;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.ViewUtil;
import com.david.common.util.Constant;
import com.david.common.util.FileUtil;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;
import com.david.databinding.ViewCameraBinding;
import com.david.incubator.control.MainApplication;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class CameraView extends BindingConstraintLayout<ViewCameraBinding> implements Consumer<Long> {

    @Inject
    AutomationControl automationControl;

    private Semaphore cameraOpenCloseLock = new Semaphore(1);

    public ObservableBoolean isRecordingVideo = new ObservableBoolean(false);

    private CameraViewModel cameraViewModel;
    //Camera2
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private CameraCaptureSession cameraCaptureSession;

    //handler
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private final TextureView.SurfaceTextureListener surfaceTextureListener;
    private final CameraDevice.StateCallback stateCallback;

    private MediaRecorder mediaRecorder;
    private String recordingFileName;
    private int startTime;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        cameraViewModel = new CameraViewModel();
        binding.setViewModel(cameraViewModel);

        if (!FileUtil.makeDirectory(Camera2Config.buildDirectory(Camera2Config.VIDEO_DIRECTORY))) {
            cameraViewModel.enableCapture.set(false);
            cameraViewModel.showNoButton();
        }
        if (!FileUtil.makeDirectory(Camera2Config.buildDirectory(Camera2Config.IMAGE_DIRECTORY))) {
            cameraViewModel.enableCapture.set(false);
            cameraViewModel.showNoButton();
        }

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };

        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                cameraOpenCloseLock.release();
                CameraView.this.cameraDevice = cameraDevice;
                try {
                    startPreview();
                } catch (Exception e) {
                    cameraViewModel.hasError.set(true);
                    LogUtils.e(e);
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                cameraOpenCloseLock.release();
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                cameraOpenCloseLock.release();
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }
        };

        isRecordingVideo.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                if (isRecordingVideo.get()) {
                    cameraViewModel.showRecordButton();
                } else {
                    cameraViewModel.showCaptureButton();
                }
            }
        });

        RxView.clicks(binding.ivLeft)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (!isRecordingVideo.get()) {
                        String fileName = TimeUtil.getFileName();
                        saveImage(fileName);
                        ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_confirm), fileName));
                    }
                });

        RxView.clicks(binding.ivRight)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (isRecordingVideo.get()) {
                        isRecordingVideo.set(false);
                        stopRecordingVideo();
                        ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_confirm), recordingFileName));

                        automationControl.removeConsumer(this);
                    } else {
                        startTime = 0;
                        cameraViewModel.recordString.set("00:00:00");
                        automationControl.addConsumer(this);

                        isRecordingVideo.set(true);
                        recordingFileName = TimeUtil.getFileName();
                        startRecordingVideo(recordingFileName);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_camera;
    }

    @Override
    public void attach() {
        if (!isRecordingVideo.get()) {
            openBackgroundThread();
            if (binding.tvCamera.isAvailable()) {
                openCamera();
            } else {
                binding.tvCamera.setSurfaceTextureListener(surfaceTextureListener);
            }
        }
        binding.tvCamera.setVisibility(View.VISIBLE);
        isRecordingVideo.notifyChange();
    }

    @Override
    public void detach() {
        binding.tvCamera.setVisibility(View.GONE);
        cameraViewModel.showNoButton();
        if (!isRecordingVideo.get()) {
            closeCamera();
            closeBackgroundThread();
        }
    }

    private void openCamera() {
        try {
            cameraOpenCloseLock.acquire();
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                throw new Exception("No camera permission");
            }
            mediaRecorder = new MediaRecorder();
            CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            cameraManager.openCamera("0", stateCallback, backgroundHandler);
        } catch (Exception e) {
            cameraViewModel.hasError.set(true);
            LogUtils.e(e);
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("CameraThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void closeCamera() {
        try {
            cameraOpenCloseLock.acquire();

            closePreviewSession();

            if (cameraCaptureSession != null) {
                cameraCaptureSession.close();
                cameraCaptureSession = null;
            }

            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }

            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (InterruptedException e) {
            LogUtils.e(e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void startPreview() throws Exception {
        SurfaceTexture surfaceTexture = binding.tvCamera.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(640, 480);
        previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

        Surface previewSurface = new Surface(surfaceTexture);
        previewBuilder.addTarget(previewSurface);

        cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                new CameraCaptureSession.StateCallback() {

                    @Override
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        try {
                            CameraView.this.cameraCaptureSession = cameraCaptureSession;
                            cameraCaptureSession.setRepeatingRequest(previewBuilder.build(),
                                    null, backgroundHandler);
                        } catch (CameraAccessException e) {
                            ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                            LogUtils.e(e);
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                    }
                }, backgroundHandler);
    }

    private void saveImage(String fileName) {
        File file = new File(Camera2Config.buildFile(Camera2Config.IMAGE_DIRECTORY, fileName));
        FileOutputStream outputPhoto = null;
        try {
            lock();
            outputPhoto = new FileOutputStream(file);
            binding.tvCamera.getBitmap()
                    .compress(Bitmap.CompressFormat.JPEG, 100, outputPhoto);

            removeImageFile();
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                unlock();
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }

    private void lock() throws Exception {
        cameraCaptureSession.capture(previewBuilder.build(),
                null, backgroundHandler);
    }

    private void unlock() throws Exception {
        cameraCaptureSession.setRepeatingRequest(previewBuilder.build(),
                null, backgroundHandler);
    }

    private void startRecordingVideo(String fileName) throws Exception {
        closePreviewSession();
        setUpMediaRecorder(Camera2Config.buildFile(Camera2Config.VIDEO_DIRECTORY, fileName));
        SurfaceTexture texture = binding.tvCamera.getSurfaceTexture();

        texture.setDefaultBufferSize(640, 480);
        previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
        List<Surface> surfaces = new ArrayList<>();

        // Set up Surface for the camera preview
        Surface previewSurface = new Surface(texture);
        surfaces.add(previewSurface);
        previewBuilder.addTarget(previewSurface);

        // Set up Surface for the MediaRecorder
        Surface recorderSurface = mediaRecorder.getSurface();
        surfaces.add(recorderSurface);
        previewBuilder.addTarget(recorderSurface);

        // Start a capture session
        // Once the session starts, we can update the UI and start recording
        cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                previewSession = cameraCaptureSession;
                try {
                    previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
                } catch (Exception e) {
                    ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                    LogUtils.e(e);
                }
                mediaRecorder.start();
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
            }
        }, backgroundHandler);
    }

    private void closePreviewSession() {
        if (previewSession != null) {
            previewSession.close();
            previewSession = null;
        }
    }

    private void setUpMediaRecorder(String fullFileName) throws IOException {
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncodingBitRate(1024 * 256);
        mediaRecorder.setVideoFrameRate(5);
        mediaRecorder.setVideoSize(320, 240);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(fullFileName);
        mediaRecorder.prepare();
    }

    private void stopRecordingVideo() throws Exception {
//        previewSession.stopRepeating();

        previewSession.abortCaptures();

        // Stop recording
        mediaRecorder.stop();
        // clear recorder configuration
        mediaRecorder.reset();

        startPreview();

        removeVideoFile();
    }

    private void removeImageFile() {
        File[] files = FileUtil.listFile(Camera2Config.buildDirectory(Camera2Config.IMAGE_DIRECTORY));
        if (files != null && files.length > Constant.IMAGE_MAX) {

            Arrays.sort(files, (f1, f2) -> {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
            });

            for (int index = 0; index < files.length - Constant.IMAGE_MAX; index++) {
                files[index].delete();
            }
        }
    }

    private void removeVideoFile() {
        File[] files = FileUtil.listFile(Camera2Config.buildDirectory(Camera2Config.VIDEO_DIRECTORY));
        if (files != null) {

            Arrays.sort(files, (f1, f2) -> {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return -1;
                else if (diff == 0)
                    return 0;
                else
                    return 1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
            });

            int sizeSum = 0;
            for (int index = 0; index < files.length; index++) {
                sizeSum += files[index].length() / 1024;
                if (sizeSum / 1024 > Constant.VIDEO_MAX_SIZE) {
                    files[index].delete();
                }
            }
        }
    }

    @Override
    public void accept(Long aLong) throws Exception {
        cameraViewModel.recordString.set(String.format(Locale.US, "%02d:%02d:%02d",
                startTime / 3600 % 24, startTime / 60 % 60, startTime % 60));
        if (startTime % 3600 == 3599) {
            stopRecordingVideo();
            recordingFileName = TimeUtil.getFileName();
            startRecordingVideo(recordingFileName);
        }
        startTime++;
    }
}