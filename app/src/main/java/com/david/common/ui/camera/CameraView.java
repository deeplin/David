package com.david.common.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
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
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CameraView extends BindingConstraintLayout<ViewCameraBinding> {



    private CameraViewModel cameraViewModel;

    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;

    private final TextureView.SurfaceTextureListener surfaceTextureListener;
    private final CameraDevice.StateCallback stateCallback;

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
                Log.e("deeplin", "changed");
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
                createPreviewSession();
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
                    String fileName = System.currentTimeMillis() + ".jpg";
                    saveImage(fileName);
                    ViewUtil.showToast(String.format(ResourceUtil.getString(R.string.capture_image_confirm), fileName));
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
//        setUpCameraOutputs(width, height);
//        configureTransform(width, height);

        try {
//            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
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
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = binding.tvCamera.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(640, 480);
            Surface previewSurface = new Surface(surfaceTexture);
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            try {
                                CaptureRequest captureRequest = captureRequestBuilder.build();
                                CameraView.this.cameraCaptureSession = cameraCaptureSession;
                                CameraView.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                        null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                LogUtils.e(e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            LogUtils.e(e);
        }
    }

    private void saveImage(String fileName) {
        File file = new File(Constant.IMAGE_DIRECTORY, fileName);
        FileOutputStream outputPhoto = null;
        try {
            outputPhoto = new FileOutputStream(file);
            binding.tvCamera.getBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
    }
}
