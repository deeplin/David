package com.david.common.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.david.incubator.ui.main.MainActivity;

import java.util.Objects;

/**
 * author: Ling Lin
 * created on: 2017/8/2 16:39
 * email: 10525677@qq.com
 * description:
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).endsWith(ACTION_BOOT)) {
            Intent bootIntent = new Intent(context, MainApplication.class);
            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootIntent);
        }
    }
}