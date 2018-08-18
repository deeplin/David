package com.david.common.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.apkfuns.logutils.LogUtils;
import com.david.common.control.DaoControl;
import com.david.common.dao.SystemSetting;
import com.david.common.mode.LanguageMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:29
 * email: 10525677@qq.com
 * description:
 */

public class FileUtil {

    public static String readTextFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line).append("\t");
        }

        bufferedReader.close();
        inputReader.close();
        inputStream.close();

        return result.toString();
    }

    public static int[] readBinaryFileFromAssets(Context context, String fileName, boolean is16Bit) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);

            int length = inputStream.available() / 2;
            int[] result = new int[length];

            int index = 0;
            int dataLow;
            int dataHigh;
            while ((dataLow = inputStream.read()) != -1) {
                if ((dataHigh = inputStream.read()) != -1) {
                    int data = (dataLow + dataHigh * 256);
                    if (is16Bit) {
                        data = (((data & 0x8000) > 0) ? (-((~(data - 1)) & 0x7FFF)) : data);
                    } else {
                        data &= 0x7FFF;
                        data = (((data & 0x4000) > 0) ? (-((~(data - 1)) & 0x7FFF)) : data);
                    }
                    result[index] = data;
                    index++;
                } else {
                    break;
                }
            }
            inputStream.close();
            return result;
        } catch (Exception e) {
            LogUtils.e(e);
            return new int[0];
        }
    }

    public static void copyFile(String fromFileName, String toFileName) throws Exception {
        File fromFile = new File(fromFileName);
        if (fromFile.exists()) { //文件存在时
            InputStream inputStream = new FileInputStream(fromFileName); //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(toFileName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.close();
        }
    }

    public static void setLanguage(Context context, DaoControl daoControl) {
        SystemSetting sensorRange = daoControl.getSystemSetting();
        if (sensorRange.getLanguageIndex() == LanguageMode.English.getIndex()) {
            ResourceUtil.setLocalLanguage(context, Locale.ENGLISH);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
            ResourceUtil.setLocalLanguage(context, Locale.SIMPLIFIED_CHINESE);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Turkish.getIndex()) {
            Locale turkish = new Locale("tr", "TR");
            ResourceUtil.setLocalLanguage(context, turkish);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Polish.getIndex()) {
            Locale spanish = new Locale("pl", "PL");
            ResourceUtil.setLocalLanguage(context, spanish);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Russia.getIndex()) {
            Locale russia = new Locale("ru", "RU");
            ResourceUtil.setLocalLanguage(context, russia);
        }
    }
}