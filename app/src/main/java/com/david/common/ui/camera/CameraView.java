package com.david.common.ui.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.view.Surface;
import android.view.TextureView;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.ViewUtil;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.databinding.ViewCameraBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CameraView extends BindingConstraintLayout<ViewCameraBinding> {

    private CameraViewModel cameraViewModel;

    //Camera2
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private CameraCaptureSession cameraCaptureSession;

    //handler
//    private HandlerThread backgroundThread;
//    private Handler backgroundHandler;

    private MediaRecorder mediaRecorder;

    private final TextureView.SurfaceTextureListener surfaceTextureListener;
    private final CameraDevice.StateCallback stateCallback;

    private String recordingFileName;

    public CameraView(Context context) {
        super(context);
        cameraViewModel = new CameraViewModel();
        binding.setViewModel(cameraViewModel);

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
                CameraView.this.cameraDevice = cameraDevice;
                startPreview();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }
        };

        RxView.clicks(binding.ivLeft)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (!cameraViewModel.isRecordingVideo.get()) {
                        String fileName = Camera2Config.buildFileName("jpg");
                        saveImage(fileName);
                        ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_confirm), fileName));
                    }
                });

        RxView.clicks(binding.ivRight)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (cameraViewModel.isRecordingVideo.get()) {
                        cameraViewModel.isRecordingVideo.set(false);
                        stopRecordingVideo();
                    } else {
                        cameraViewModel.isRecordingVideo.set(true);
                        recordingFileName = Camera2Config.buildFileName("mp4");
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
        cameraViewModel.attach();
        openBackgroundThread();
        binding.tvCamera.setSurfaceTextureListener(surfaceTextureListener);
    }

    @Override
    public void detach() {
        closeCamera();
        closeBackgroundThread();
        cameraViewModel.detach();
    }

    void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mediaRecorder = new MediaRecorder();
            CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            cameraManager.openCamera("0", stateCallback, null);
        } catch (Exception e) {
            ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_failed)));
            LogUtils.e(e);
        }
    }

    private void openBackgroundThread() {
//        backgroundThread = new HandlerThread("CameraThread");
//        backgroundThread.start();
//        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void closeCamera() {
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
    }

    private void closeBackgroundThread() {
//        if (backgroundHandler != null) {
//            backgroundThread.quitSafely();
//            backgroundThread = null;
//            backgroundHandler = null;
//        }
    }

    private void startPreview() {
        try {
            closePreviewSession();

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
                                        null, null);
                            } catch (CameraAccessException e) {
                                ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                                LogUtils.e(e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                        }
                    }, null);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void saveImage(String fileName) {
        lock();
        File file = new File(Camera2Config.buildPath(Camera2Config.IMAGE_DIRECTORY, fileName));
        FileOutputStream outputPhoto = null;
        try {
            outputPhoto = new FileOutputStream(file);
            binding.tvCamera.getBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            unlock();
            try {
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
    }

    private void lock() {
        try {
            cameraCaptureSession.capture(previewBuilder.build(),
                    null, null);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void unlock() {
        try {
            cameraCaptureSession.setRepeatingRequest(previewBuilder.build(),
                    null, null);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void startRecordingVideo(String fileName) {
        try {
            closePreviewSession();
            setUpMediaRecorder(Camera2Config.buildPath(Camera2Config.VIDEO_DIRECTORY, fileName));
            SurfaceTexture texture = binding.tvCamera.getSurfaceTexture();
            assert texture != null;
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
                        previewSession.setRepeatingRequest(previewBuilder.build(), null, null);
                    } catch (Exception e) {
                        LogUtils.e(e);
                    }
                    mediaRecorder.start();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                }
            }, null);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }
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

        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setVideoEncodingBitRate(2500000);
        mediaRecorder.setOutputFile(fullFileName);
        mediaRecorder.prepare();
    }

    private void stopRecordingVideo() {
        // Stop recording
        mediaRecorder.stop();
        mediaRecorder.reset();

//        closeCamera();
//        openCamera();
        startPreview();
    }
}
