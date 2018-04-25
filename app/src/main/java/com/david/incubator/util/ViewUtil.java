package com.david.incubator.util;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableBoolean;

import com.david.R;
import com.david.common.util.Action;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.common.util.SensorRange;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/16 15:19
 * email: 10525677@qq.com
 * description:
 */

public class ViewUtil {
    public static synchronized void changeFragment(FragmentManager fragmentManager, Fragment fromFragment,
                                      Fragment toFragment, int toFragmentId, int resourceID) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (toFragment != null) {
            if (toFragment.isAdded()) {
                fragmentTransaction.show(toFragment);
            } else {
                fragmentTransaction.add(resourceID, toFragment, String.valueOf(toFragmentId));
            }
        }
        fragmentTransaction.commit();

        if (fromFragment != null) {
            fragmentTransaction.remove(fromFragment);
        }
    }

    public static AlertDialog buildConfirmDialog(Context context, int titleId, String message, DialogInterface.OnClickListener listener) {
        String title = ResourceUtil.getString(titleId);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        builder.setTitle(title)//设置对话框标题
                .setMessage(message)//设置显示的内容
                .setPositiveButton(R.string.ok, listener)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
        return builder.show();
    }

    /*设置是否显示SPO2 PR Oxygen Humidity*/
    public static void displaySensor(boolean installed, boolean enabled, ObservableBoolean visible, Action<Boolean> action) {
        if (installed) {
            if (enabled) {
                visible.set(false);
            } else {
                visible.set(true);
                action.accept(false);
            }
        } else {
            visible.set(true);
            action.accept(true);
        }
    }

    public static String formatTempValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_TEMP_STRING;
        } else if (value <= SensorRange.TEMP_DISPLAY_UPPER && value >= SensorRange.TEMP_DISPLAY_LOWER) {
            DecimalFormat decimalFormat = new DecimalFormat("00.0");
            return decimalFormat.format(value / 10.0);
        } else {
            return Constant.SENSOR_TEMP_STRING;
        }
    }

    public static String formatSpo2Value(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_DEFAULT_STRING;
        } else if (value <= SensorRange.SPO2_DISPLAY_UPPER && value >= SensorRange.SPO2_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return Constant.SENSOR_DEFAULT_STRING;
        }
    }

    public static String formatPrValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_DEFAULT_STRING;
        } else if (value <= SensorRange.PR_DISPLAY_UPPER && value >= SensorRange.PR_DISPLAY_LOWER) {
            return String.valueOf(value);
        } else {
            return Constant.SENSOR_DEFAULT_STRING;
        }
    }

    public static String formatHumidityValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_DEFAULT_STRING;
        } else if (value <= SensorRange.HUMIDITY_DISPLAY_UPPER && value >= SensorRange.HUMIDITY_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return Constant.SENSOR_DEFAULT_STRING;
        }
    }

    public static String formatOxygenValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_DEFAULT_STRING;
        } else if (value <= SensorRange.OXYGEN_DISPLAY_UPPER && value >= SensorRange.OXYGEN_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return Constant.SENSOR_DEFAULT_STRING;
        }
    }

    public static String formatScaleValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_SCALE_STRING;
        } else if (value <= SensorRange.SCALE_DISPLAY_UPPER && value >= SensorRange.SCALE_DISPLAY_LOWER) {
            return String.valueOf(value);
        } else {
            return Constant.SENSOR_SCALE_STRING;
        }
    }

    public static String formatPiValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_PI_STRING;
        } else if (value <= SensorRange.PI_DISPLAY_UPPER && value >= SensorRange.PI_DISPLAY_LOWER) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(value / 10000.0) + "%";
        } else {
            return Constant.SENSOR_PI_STRING;
        }
    }

    public static String formatJaunediceTime(int minute, boolean showColon) {
        if (showColon) {
            return String.format(Locale.US, "%02d:%02d", minute / 60, minute % 60);
        } else {
            return String.format(Locale.US, "%02d %02d", minute / 60, minute % 60);
        }
    }

    public static String formatData(int value) {
        DecimalFormat decimalFormat = new DecimalFormat("00.0");
        return decimalFormat.format(value / 10.0);
    }
}