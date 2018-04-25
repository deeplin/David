package com.david.common.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-08  9:03
 */

public class BitmapUtil {

    static Bitmap getScaledBitmap(Bitmap bmp, int width, int height, Bitmap.Config bitmapConfig) {
        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, bitmapConfig);

        float ratioX = 1.0f;
        float ratioY = 1.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static void compressImage(Bitmap bitmap, String path, String fileName) throws FileNotFoundException{
        BitmapUtil.compressImage(bitmap, 640, 480,
                Bitmap.CompressFormat.JPEG, Bitmap.Config.ARGB_8888, 80, path, fileName);
    }

    private static void compressImage(Bitmap bitmap, int maxWidth, int maxHeight,
                                      Bitmap.CompressFormat compressFormat, Bitmap.Config bitmapConfig,
                                      int quality, String parentPath, String fileName) throws FileNotFoundException {
        FileOutputStream out = null;

        fileName = generateFilePath(parentPath, compressFormat.name().toLowerCase(), fileName);
        try {
            out = new FileOutputStream(fileName);
            // 通过文件名写入
            Bitmap newBmp = BitmapUtil.getScaledBitmap(bitmap, maxWidth, maxHeight, bitmapConfig);
            if (newBmp != null) {
                newBmp.compress(compressFormat, quality, out);
            }

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    private static String generateFilePath(String parentPath, String extension, String fileName) {
        File file = new File(parentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        /** reset fileName by prefix and custom file name */
        return file.getAbsolutePath() + File.separator + fileName + "." + extension;
    }
}
