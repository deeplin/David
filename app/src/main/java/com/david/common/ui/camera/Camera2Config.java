package com.david.common.ui.camera;

import android.os.Environment;

public class Camera2Config {
    public static final String IMAGE_DIRECTORY = "Pictures";
    public static final String VIDEO_DIRECTORY = "Video";

    public static String buildFileName(String extension) {
        long fileName = System.currentTimeMillis();
        return String.format("%s.%s", fileName, extension);
    }

    public static String buildPath(String path, String fileName) {
        return String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), path, fileName);
    }
}
