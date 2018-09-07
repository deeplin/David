package com.david.common.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

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

    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CameraCaptureSession previewSession;
    private MediaRecorder mediaRecorder;

    private final TextureView.SurfaceTextureListener surfaceTextureListener;
    private final CameraDevice.StateCallback stateCallback;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest.Builder previewBuilder;

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
            public void onOpened(CameraDevice cameraDevice) {
                CameraView.this.cameraDevice = cameraDevice;
                startPreview();
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }
        };

        RxView.clicks(binding.ivLeft)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (cameraViewModel.isRecordingVideo.get()) {
                        cameraViewModel.isRecordingVideo.set(false);
                        stopRecordingVideo();
                        ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_confirm), recordingFileName));
                    } else {
                        String fileName = Constant.buildFileName("jpg");
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
                        recordingFileName = Constant.buildFileName("mp4");
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

        if (binding.tvCamera.isAvailable()) {
            openCamera();
        } else {
            binding.tvCamera.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void detach() {
        cameraViewModel.detach();
        closeCamera();
        closeBackgroundThread();
    }

    void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mediaRecorder = new MediaRecorder();
            CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            manager.openCamera("0", stateCallback, backgroundHandler);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
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
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void startPreview() {
        try {
            closePreviewSession();

            SurfaceTexture surfaceTexture = binding.tvCamera.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(640, 480);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(surfaceTexture);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            try {
                                if (cameraViewModel.isRecordingVideo.get()) {
                                    previewSession = cameraCaptureSession;
                                    updatePreview();
                                } else {
                                    CaptureRequest captureRequest = captureRequestBuilder.build();
                                    CameraView.this.cameraCaptureSession = cameraCaptureSession;
                                    CameraView.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                            null, backgroundHandler);
                                }
                            } catch (CameraAccessException e) {
                                LogUtils.e(e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            ViewUtil.showToast(ResourceUtil.getString(R.string.capture_failed));
                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void saveImage(String fileName) {
        lock();
        File file = new File(Constant.buildPath(Constant.IMAGE_DIRECTORY, fileName));
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
            cameraCaptureSession.capture(captureRequestBuilder.build(),
                    null, backgroundHandler);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void unlock() {
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),
                    null, backgroundHandler);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void startRecordingVideo(String fileName) {
        try {
            closePreviewSession();
            setUpMediaRecorder(Constant.buildPath(Constant.VIDEO_DIRECTORY, fileName));
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
                    updatePreview();
                    mediaRecorder.start();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_failed)));
                }
            }, backgroundHandler);
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
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setOutputFile(fullFileName);
        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.prepare();
    }

    private void updatePreview() {
        try {
            previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordingVideo() {
        // Stop recording
        mediaRecorder.stop();
        mediaRecorder.reset();

//        Activity activity = getActivity();
//        if (null != activity) {
//            Toast.makeText(activity, "Video saved: " + mNextVideoAbsolutePath,
//                    Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
//        }
//        closeCamera();
//        openCamera();
        startPreview();
    }
}
