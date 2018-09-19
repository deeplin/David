package com.david.common.ui.camera;

public class Camera2Config {

    public static final String EXTERNAL_ROOT = "/mnt/external_sd";
    public static final String IMAGE_DIRECTORY = "image";
    public static final String VIDEO_DIRECTORY = "video";

    public static String buildFile() {
        long fileName = System.currentTimeMillis();
        return String.format("%s", fileName);
    }

    public static String buildFile(String path, String fileName) {
        return String.format("%s/%s", buildDirectory(path), fileName);
    }

    public static String buildDirectory(String path) {
        return String.format("%s/%s/", EXTERNAL_ROOT, path);
    }
}